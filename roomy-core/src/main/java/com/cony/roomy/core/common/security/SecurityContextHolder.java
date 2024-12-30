package com.cony.roomy.core.common.security;

import com.cony.roomy.core.user.domain.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SecurityContextHolder {
    private static final ThreadLocal<User> context = new ThreadLocal<>();

    private SecurityContextHolder() {}

    public static User getContext() {
        return context.get();
    }

    public static boolean hasContext() {
        if(context.get() != null) {
            return true;
        }
        return false;
    }

    public static void setContext(User user) {
        context.set(user);
    }

    public static void clear() {
        context.remove();
    }
}
