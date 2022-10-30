package com.example.mailbackend.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;

public class SecurityUtils {
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public static UserPrincipal getUserPrincipal() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null)
            return null;
        if (authentication.getPrincipal() instanceof UserPrincipal) {
            return (UserPrincipal) authentication.getPrincipal();
        }
        return null;
    }

    public static Long getUserId() {
        return isLogin() ? getUserPrincipal().getId() : null;
    }

    public static String getName() {
        return isLogin() ? getUserPrincipal().getName() : null;
    }

    public static boolean isLogin() {
        return getUserPrincipal() != null;
    }

    public static boolean isGuest() {
        return !isLogin();
    }

    public static boolean isAdmin() {
        return isLogin() && hasRole("ROLE_ADMIN");
    }

    public static boolean hasRole(String role) {
        if ("ANONYMOUS".equals(role))
            return true;

        List<String> authorities = getAuthorities();

        if (authorities.size() == 0) {
            if ("GUEST".equals(role)) {
                return true;
            } else {
                return false;
            }
        }

        long matchedCount = authorities.stream().filter(a -> a.equals(role)).count();

        if (matchedCount == 0) {
            return false;
        } else {
            return true;
        }
    }

    public static List<String> getAuthorities() {
        List<String> result = new ArrayList<>();
        if (isLogin()) {
            List<GrantedAuthority> list = (List<GrantedAuthority>) getAuthentication().getAuthorities();
            for (GrantedAuthority grantedAuthority : list) {
                result.add(grantedAuthority.getAuthority());
            }
        }
        return result;
    }


}
