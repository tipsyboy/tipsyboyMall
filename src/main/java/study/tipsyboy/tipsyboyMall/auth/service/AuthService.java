package study.tipsyboy.tipsyboyMall.auth.service;


import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.tipsyboy.tipsyboyMall.auth.domain.Member;
import study.tipsyboy.tipsyboyMall.auth.domain.MemberRepository;
import study.tipsyboy.tipsyboyMall.auth.domain.MemberRole;
import study.tipsyboy.tipsyboyMall.auth.dto.SignupRequestDto;
import study.tipsyboy.tipsyboyMall.auth.exception.AuthException;
import study.tipsyboy.tipsyboyMall.auth.exception.AuthExceptionType;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Long signup(SignupRequestDto signupRequestDto) {

        if (memberRepository.existsByEmail(signupRequestDto.getEmail())) {
            throw new AuthException(AuthExceptionType.EMAIL_ALREADY_EXISTS);
        }

        if (memberRepository.existsByNickname(signupRequestDto.getNickname())) {
            throw new AuthException(AuthExceptionType.NICKNAME_ALREADY_EXISTS);
        }

        Member member = Member.builder()
                .email(signupRequestDto.getEmail())
                .nickname(signupRequestDto.getNickname())
                .password(passwordEncoder.encode(signupRequestDto.getPassword()))
                .memberRole(MemberRole.MEMBER)
                .build();
        memberRepository.save(member);

        return member.getId();
    }
}
