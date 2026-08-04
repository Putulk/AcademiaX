package com.nextgen.erp.common.security.autoconfigure;

import com.nextgen.erp.common.security.feign.FeignAuthRelayInterceptor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@ConditionalOnClass(name = "feign.RequestInterceptor")
public class FeignAuthRelayAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(FeignAuthRelayInterceptor.class)
    public FeignAuthRelayInterceptor feignAuthRelayInterceptor() {
        return new FeignAuthRelayInterceptor();
    }
}
