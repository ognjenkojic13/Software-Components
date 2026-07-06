package rs.ac.raf.userservice.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GatewayPrincipal {
    private Long userId;
    private String username;
}
