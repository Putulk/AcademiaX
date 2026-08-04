package com.nextgen.erp.common.security.feign;

import com.nextgen.erp.common.security.AuthHeaderRelay;
import feign.RequestInterceptor;
import feign.RequestTemplate;

public class FeignAuthRelayInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {

        String authHeader = AuthHeaderRelay.currentAuthorizationHeader();

        if (authHeader != null) {
            template.header("Authorization", authHeader);
        }
    }
}
