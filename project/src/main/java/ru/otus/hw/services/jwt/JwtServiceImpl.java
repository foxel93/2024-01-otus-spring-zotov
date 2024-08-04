package ru.otus.hw.services.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.otus.hw.configurations.JwtConfiguration;

@Service
@AllArgsConstructor
public class JwtServiceImpl implements JwtService {
    private static final String USERNAME_CLAIM_NAME = "username";

    private static final String AUTHORITIES_CLAIM_NAME = "authorities";

    private final JwtConfiguration jwtConfiguration;

    @Override
    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public String generateToken(UserDetails userDetails) {
        var claims = Map.of(
            USERNAME_CLAIM_NAME, userDetails.getUsername(),
            AUTHORITIES_CLAIM_NAME, userDetails.getAuthorities()
        );
        return generateToken(claims, userDetails);
    }

    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        return extractUserName(token).equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
        var claims = extractAllClaims(token);
        return claimsResolvers.apply(claims);
    }

    private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        var currentTimeMs = System.currentTimeMillis();
        var expirationTimeMs = currentTimeMs + jwtConfiguration.getExpirationSigningTokenTtl().toMillis();

        return Jwts.builder()
            .claims(extraClaims)
            .subject(userDetails.getUsername())
            .issuedAt(new Date(currentTimeMs))
            .expiration(new Date(expirationTimeMs))
            .signWith(jwtConfiguration.getSigningKey())
            .compact();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
            .verifyWith(jwtConfiguration.getSigningKey())
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }
}
