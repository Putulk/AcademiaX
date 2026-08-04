package com.nextgen.erp.common.security;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Propagates the inbound request's Authorization header onto outgoing
 * downstream calls (Feign, RestClient), so a business service's own JWT
 * check on the receiving end sees the original caller's token rather than
 * an unauthenticated request.
 */
public final class AuthHeaderRelay {

    private AuthHeaderRelay() {
    }

    public static String currentAuthorizationHeader() {

        if (!(RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes attributes)) {
            return null;
        }

        return attributes.getRequest().getHeader("Authorization");
    }
}
