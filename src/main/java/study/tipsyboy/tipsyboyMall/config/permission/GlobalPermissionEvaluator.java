package study.tipsyboy.tipsyboyMall.config.permission;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

import java.io.Serializable;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class GlobalPermissionEvaluator implements PermissionEvaluator {

    private final List<PermissionHandler> handlers;

    @Override
    public boolean hasPermission(Authentication authentication,
                                 Serializable targetId,
                                 String targetType,
                                 Object permission) {
        log.info("[Global Permission Evaluator]");

        for (PermissionHandler handler : handlers) {
            if (handler.supports(targetType)) {
                return handler.hasPermission(authentication, (Long) targetId, (String) permission);
            }
        }

        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        return false;
    }
}
