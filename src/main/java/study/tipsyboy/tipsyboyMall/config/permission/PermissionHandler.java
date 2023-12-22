package study.tipsyboy.tipsyboyMall.config.permission;

import org.springframework.security.core.Authentication;

public interface PermissionHandler {
    boolean supports(String targetType);

    boolean hasPermission(Authentication authentication, Long targetId, String permission);
}
