package ru.otus.hw.filters;

import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.startsWith;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.otus.hw.configurations.JwtConfiguration;
import ru.otus.hw.services.jwt.JwtService;
import ru.otus.hw.services.user.UserService;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtConfiguration jwtConfiguration;

    private final JwtService jwtService;

    private final UserService userService;

    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest request,
        @NonNull HttpServletResponse response,
        @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        // Получаем токен из заголовка
        var authHeader = request.getHeader(jwtConfiguration.getTokenHeader());
        var bearerPrefix = jwtConfiguration.getBearerPrefix();

        if (isNotEmpty(authHeader) && startsWith(authHeader, bearerPrefix)) {
            tryAuth(request, authHeader, bearerPrefix);
        }
        filterChain.doFilter(request, response);
    }

    private void tryAuth(HttpServletRequest request, String authHeader, String bearerPrefix) {
        var jwt = authHeader.substring(bearerPrefix.length());
        var username = jwtService.extractUserName(jwt);

        if (isEmpty(username) || nonNull(SecurityContextHolder.getContext().getAuthentication())) {
            return;
        }

        var userDetails = userService.loadUserByUsername(username);

        if (jwtService.isTokenValid(jwt, userDetails)) {
            var authToken = authToken(userDetails, request);
            var context = securityContext(authToken);
            SecurityContextHolder.setContext(context);
        }
    }

    private SecurityContext securityContext(UsernamePasswordAuthenticationToken authToken) {
        var context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authToken);
        return context;
    }

    private UsernamePasswordAuthenticationToken authToken(UserDetails userDetails, HttpServletRequest request) {
        var authToken = new UsernamePasswordAuthenticationToken(
            userDetails,
            null,
            userDetails.getAuthorities()
        );
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        return authToken;
    }
}
