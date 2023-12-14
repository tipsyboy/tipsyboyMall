package study.tipsyboy.tipsyboyMall.config;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import study.tipsyboy.tipsyboyMall.auth.domain.Member;
import study.tipsyboy.tipsyboyMall.auth.domain.MemberRepository;
import study.tipsyboy.tipsyboyMall.auth.dto.LoginMember;
import study.tipsyboy.tipsyboyMall.auth.exception.AuthException;
import study.tipsyboy.tipsyboyMall.auth.exception.AuthExceptionType;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new AuthException(AuthExceptionType.AUTH_NOT_FOUND));

        return new LoginMember(member);
    }

}
