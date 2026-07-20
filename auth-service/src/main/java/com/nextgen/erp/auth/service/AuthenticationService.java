package com.nextgen.erp.auth.service;

import com.nextgen.erp.auth.dto.request.LoginRequest;
import com.nextgen.erp.auth.dto.request.LogoutRequest;
import com.nextgen.erp.auth.dto.request.RefreshToken;
import com.nextgen.erp.auth.dto.request.RegisterRequest;
import com.nextgen.erp.auth.dto.response.AuthenticationResponse;

public interface AuthenticationService {

    AuthenticationResponse register(RegisterRequest request);

    AuthenticationResponse login(LoginRequest request);

    AuthenticationResponse refreshToken(
            RefreshToken request
    );

    void logout(LogoutRequest request);

}