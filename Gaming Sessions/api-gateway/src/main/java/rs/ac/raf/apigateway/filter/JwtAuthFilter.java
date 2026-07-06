package rs.ac.raf.apigateway.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Arrays;
import java.util.List;

@Component
public class JwtAuthFilter extends ZuulFilter {

    private static final List<String> PUBLIC_PATHS = Arrays.asList(
            "/api/users/register",
            "/api/users/login",
            "/api/users/activate",
            "/api/users/forgot-password",
            "/api/users/reset-password"
    );

    private final Key signingKey;

    public JwtAuthFilter(@Value("${app.jwt.secret}") String secret) {
        this.signingKey = Keys.hmacShaKeyFor(secret.getBytes());
    }

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        return PUBLIC_PATHS.stream().noneMatch(p -> request.getRequestURI().startsWith(p));
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();

        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            reject(ctx, "Nedostaje Authorization header");
            return null;
        }

        String token = header.substring(7);
        try {
            Claims claims = Jwts.parserBuilder().setSigningKey(signingKey).build()
                    .parseClaimsJws(token).getBody();

            ctx.addZuulRequestHeader("X-User-Id", claims.getSubject());
            ctx.addZuulRequestHeader("X-Username", claims.get("username", String.class));
            ctx.addZuulRequestHeader("X-User-Role", claims.get("role", String.class));
        } catch (JwtException | IllegalArgumentException ex) {
            reject(ctx, "Nevazeci ili istekao token");
        }

        return null;
    }

    private void reject(RequestContext ctx, String message) {
        ctx.setSendZuulResponse(false);
        ctx.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
        ctx.setResponseBody("{\"error\":\"" + message + "\"}");
        ctx.getResponse().setContentType("application/json");
    }
}
