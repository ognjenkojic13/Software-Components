package rs.ac.raf.userservice.security;

import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class InternalSecretFilter extends OncePerRequestFilter {

    public static final String INTERNAL_SECRET_HEADER = "X-Internal-Secret";

    private final String expectedSecret;

    public InternalSecretFilter(String expectedSecret) {
        this.expectedSecret = expectedSecret;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if (request.getRequestURI().startsWith("/internal/")) {
            String provided = request.getHeader(INTERNAL_SECRET_HEADER);
            if (provided == null || !provided.equals(expectedSecret)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid internal secret");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}
