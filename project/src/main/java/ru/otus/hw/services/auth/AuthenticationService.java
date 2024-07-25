package ru.otus.hw.services.auth;

import ru.otus.hw.dto.jwt.JwtAuthenticationResponse;
import ru.otus.hw.dto.jwt.SignUpRequest;
import ru.otus.hw.dto.jwt.SingInRequest;

public interface AuthenticationService {
    JwtAuthenticationResponse signUp(SignUpRequest request);

    JwtAuthenticationResponse signIn(SingInRequest request);
}
