package study.tipsyboy.tipsyboyMall.auth.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import study.tipsyboy.tipsyboyMall.auth.domain.Member;
import study.tipsyboy.tipsyboyMall.auth.domain.MemberRepository;
import study.tipsyboy.tipsyboyMall.auth.domain.MemberRole;
import study.tipsyboy.tipsyboyMall.auth.dto.SignupRequestDto;
import study.tipsyboy.tipsyboyMall.auth.exception.AuthException;
import study.tipsyboy.tipsyboyMall.auth.exception.AuthExceptionType;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AuthServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private AuthService authService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @AfterEach
    public void after() {
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입을 실패한다. - 이메일 중복")
    public void signupFailureByDuplicatedEmail() throws Exception {
        // given
        Member member = Member.builder()
                .email("tipsyboy@gmail.com")
                .nickname("간술맨")
                .password("1234")
                .memberRole(MemberRole.MEMBER)
                .build();
        memberRepository.save(member);

        // when
        SignupRequestDto requestDto = SignupRequestDto.builder()
                .email("tipsyboy@gmail.com")
                .nickname("혼술맨")
                .password("1234")
                .build();

        // then
        AuthException authException = assertThrows(AuthException.class,
                () -> authService.signup(requestDto));
        assertEquals(AuthExceptionType.EMAIL_ALREADY_EXISTS, authException.getExceptionType());
    }

    @Test
    @DisplayName("회원가입을 실패한다. - 닉네임 중복")
    public void signupFailureByDuplicatedNickname() throws Exception {
        // given
        Member member = Member.builder()
                .email("tipsyboy@gmail.com")
                .nickname("간술맨")
                .password("1234")
                .memberRole(MemberRole.MEMBER)
                .build();
        memberRepository.save(member);

        // when
        SignupRequestDto requestDto = SignupRequestDto.builder()
                .email("tipsyboy1@gmail.com")
                .nickname("간술맨")
                .password("1234")
                .build();

        // then
        AuthException authException = assertThrows(AuthException.class,
                () -> authService.signup(requestDto));
        assertEquals(AuthExceptionType.NICKNAME_ALREADY_EXISTS, authException.getExceptionType());
    }

    @Test
    @DisplayName("회원가입을 성공한다.")
    public void signup() throws Exception {
        // given
        SignupRequestDto requestDto = SignupRequestDto.builder()
                .email("tipsyboy@gmail.com")
                .nickname("간술맨")
                .password("1234")
                .build();

        // when
        Long memberId = authService.signup(requestDto);

        // then
        assertEquals(1, memberRepository.count());

        Member findMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new AuthException(AuthExceptionType.AUTH_NOT_FOUND));
        assertEquals("tipsyboy@gmail.com", findMember.getEmail());
        assertEquals("간술맨", findMember.getNickname());
        assertEquals(MemberRole.MEMBER, findMember.getMemberRole());
        assertTrue(passwordEncoder.matches("1234", findMember.getPassword()));
    }
}