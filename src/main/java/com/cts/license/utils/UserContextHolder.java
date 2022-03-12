package com.cts.license.utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserContextHolder {
    private static final ThreadLocal<UserContext> userContext = new ThreadLocal<>();

    public static UserContext getContext() {
        log.debug("Getting UserContext from ThreadLocal");
        UserContext context = userContext.get();

        if (context == null) {
            context = createEmptyContext();
            userContext.set(context);
        }
        return userContext.get();
    }

    private static UserContext createEmptyContext() {
        return new UserContext();
    }


}
