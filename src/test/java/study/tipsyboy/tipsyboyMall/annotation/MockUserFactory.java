package study.tipsyboy.tipsyboyMall.annotation;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import study.tipsyboy.tipsyboyMall.auth.domain.Member;
import study.tipsyboy.tipsyboyMall.auth.domain.MemberRepository;
import study.tipsyboy.tipsyboyMall.auth.dto.LoginMember;

public class MockUserFactory implements WithSecurityContextFactory<CustomWithMockUser> {

    private final MemberRepository memberRepository;

    public MockUserFactory(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public SecurityContext createSecurityContext(CustomWithMockUser annotation) {
        Member member = Member.builder()
                .email(annotation.email())
                .nickname(annotation.nickname())
                .password(annotation.password())
                .memberRole(annotation.memberRole())
                .build();
        memberRepository.save(member);

        LoginMember loginMember = new LoginMember(member);
        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(loginMember, loginMember.getPassword(), loginMember.getAuthorities());

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authenticationToken);

        return context;
    }
}
