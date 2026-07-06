package rs.ac.raf.sessionservice.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class UserServiceClient {

    private static final String USER_SERVICE_URL = "http://user-service/internal/users";

    private final RestTemplate restTemplate;
    private final String internalSecret;

    public UserServiceClient(RestTemplate restTemplate, @Value("${app.internal-secret}") String internalSecret) {
        this.restTemplate = restTemplate;
        this.internalSecret = internalSecret;
    }

    @Retryable(value = {ResourceAccessException.class, RestClientException.class, IllegalStateException.class},
            maxAttempts = 3, backoff = @Backoff(delay = 500, multiplier = 2))
    public EligibilityResponse getEligibility(Long userId) {
        HttpEntity<Void> entity = new HttpEntity<>(authHeaders());
        return restTemplate.exchange(USER_SERVICE_URL + "/{id}/eligibility", HttpMethod.GET, entity,
                EligibilityResponse.class, userId).getBody();
    }

    @Retryable(value = {ResourceAccessException.class, RestClientException.class, IllegalStateException.class},
            maxAttempts = 3, backoff = @Backoff(delay = 500, multiplier = 2))
    public void markJoined(Long userId) {
        HttpEntity<Void> entity = new HttpEntity<>(authHeaders());
        restTemplate.exchange(USER_SERVICE_URL + "/{id}/joined", HttpMethod.PUT, entity, Void.class, userId);
    }

    @Retryable(value = {ResourceAccessException.class, RestClientException.class, IllegalStateException.class},
            maxAttempts = 3, backoff = @Backoff(delay = 500, multiplier = 2))
    public void applyAttendanceBatch(AttendanceBatchRequest request) {
        HttpEntity<AttendanceBatchRequest> entity = new HttpEntity<>(request, authHeaders());
        restTemplate.exchange(USER_SERVICE_URL + "/attendance-batch", HttpMethod.PUT, entity, Void.class);
    }

    private HttpHeaders authHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Internal-Secret", internalSecret);
        return headers;
    }
}
