package com.nextgen.erp.student_management.client;

import com.nextgen.erp.common.security.AuthHeaderRelay;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    RestClient restClient(RestClient.Builder builder) {
        return builder
                .requestInterceptor((request, body, execution) -> {
                    String authHeader = AuthHeaderRelay.currentAuthorizationHeader();
                    if (authHeader != null) {
                        request.getHeaders().set("Authorization", authHeader);
                    }
                    return execution.execute(request, body);
                })
                .build();
    }

}
