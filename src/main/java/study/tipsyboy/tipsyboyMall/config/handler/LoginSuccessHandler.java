package study.tipsyboy.tipsyboyMall.config.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import study.tipsyboy.tipsyboyMall.auth.dto.LoginMember;
import study.tipsyboy.tipsyboyMall.auth.dto.MemberInfoResponseDto;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@RequiredArgsConstructor
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        LoginMember principal = (LoginMember) authentication.getPrincipal();
        log.info("[인증 성공] member e-mail={}", principal.getUsername());

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setStatus(HttpServletResponse.SC_OK);

        MemberInfoResponseDto memberInfoResponseDto = MemberInfoResponseDto.ofPrincipal(principal);
        objectMapper.writeValue(response.getWriter(), memberInfoResponseDto);
    }
}
