package com.nextgen.erp.auth.service;

import com.nextgen.erp.auth.dto.request.RegisterRequest;
import com.nextgen.erp.auth.dto.response.AuthenticationResponse;

public interface AuthenticationService {

    AuthenticationResponse register(RegisterRequest request);

}