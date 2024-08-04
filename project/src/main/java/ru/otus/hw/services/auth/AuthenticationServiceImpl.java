package ru.otus.hw.services.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.otus.hw.dto.jwt.JwtAuthenticationResponse;
import ru.otus.hw.dto.jwt.SignUpRequest;
import ru.otus.hw.dto.jwt.SingInRequest;
import ru.otus.hw.dto.user.UserCreateDto;
import ru.otus.hw.services.jwt.JwtService;
import ru.otus.hw.services.user.UserService;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserService userService;

    private final JwtService jwtService;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    /**
     * Регистрация пользователя
     *
     * @param request данные пользователя
     * @return токен
     */
    public JwtAuthenticationResponse signUp(SignUpRequest request) {
        var user = UserCreateDto.builder()
            .username(request.getUsername())
            .password(passwordEncoder.encode(request.getPassword()))
            .role(request.getRole())
            .build();
        var createdUser = userService.create(user);
        return new JwtAuthenticationResponse(getToken(createdUser.getUsername()));
    }

    /**
     * Аутентификация пользователя
     *
     * @param request данные пользователя
     * @return токен
     */
    public JwtAuthenticationResponse signIn(SingInRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
            request.getUsername(),
            request.getPassword()
        ));

        return new JwtAuthenticationResponse(getToken(request.getUsername()));
    }

    private String getToken(String username) {
        var user = userService.loadUserByUsername(username);
        return jwtService.generateToken(user);
    }
}
