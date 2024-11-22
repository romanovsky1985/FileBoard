package my.fileboard.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class CustomAuthenticationManager implements AuthenticationManager {
    private final Map<String, String> users = Map.of(
            "anton", "7391",
            "kate", "2288"
    );

    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        if (users.get(username) != null && users.get(username).equals(password)) {
            return new UsernamePasswordAuthenticationToken(
                    username, null, List.of(new SimpleGrantedAuthority("USER")));
        }
        return null;
    }
}
