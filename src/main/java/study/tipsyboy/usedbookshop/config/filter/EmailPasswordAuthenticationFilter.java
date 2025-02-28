package study.tipsyboy.usedbookshop.config.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import java.io.IOException;

public class EmailPasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final ObjectMapper objectMapper;

    public EmailPasswordAuthenticationFilter(String loginUrl,
                                             ObjectMapper objectMapper) {
        super(loginUrl);
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException, IOException, ServletException {

        EmailPasswordReader emailPasswordReader = objectMapper.readValue(request.getInputStream(), EmailPasswordReader.class);
        UsernamePasswordAuthenticationToken unauthenticatedToken
                = UsernamePasswordAuthenticationToken.unauthenticated(
                emailPasswordReader.getEmail(),
                emailPasswordReader.getPassword()
        );

        unauthenticatedToken.setDetails(this.authenticationDetailsSource.buildDetails(request));
        return this.getAuthenticationManager().authenticate(unauthenticatedToken);
    }


    @Getter
    private static class EmailPasswordReader {
        private final String email;
        private final String password;

        private EmailPasswordReader(String email, String password) {
            this.email = email;
            this.password = password;
        }
    }
}
