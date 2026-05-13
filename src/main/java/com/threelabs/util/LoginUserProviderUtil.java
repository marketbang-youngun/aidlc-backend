package com.threelabs.util;

import com.threelabs.config.security.user_details.CustomUserDetails;
import com.threelabs.entity.AdminUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class LoginUserProviderUtil {

    private LoginUserProviderUtil() {
    }

    public static AdminUser getCurrentAdminUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof CustomUserDetails) {
            return ((CustomUserDetails) principal).getAdminUser();
        }

        return null;
    }

    public static String getCurrentUserId() {
        AdminUser adminUser = getCurrentAdminUser();
        return adminUser != null ? adminUser.getUserId() : null;
    }

    public static Long getCurrentUserNo() {
        AdminUser adminUser = getCurrentAdminUser();
        return adminUser != null ? adminUser.getUserNo() : null;
    }
}
