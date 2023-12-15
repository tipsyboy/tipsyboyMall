package study.tipsyboy.tipsyboyMall.annotation;

import org.springframework.security.test.context.support.WithSecurityContext;
import study.tipsyboy.tipsyboyMall.auth.domain.MemberRole;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = MockUserFactory.class)
public @interface CustomWithMockUser {

    String email() default "tipsyboy@gmial.com";

    String nickname() default "간술맨";

    String password() default "1234";

    MemberRole memberRole() default MemberRole.ADMIN;

}
