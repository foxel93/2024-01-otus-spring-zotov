package ru.otus.hw.configurations;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import javax.crypto.SecretKey;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfiguration {
    @Value("${jwt.token.signing.key}")
    private String signingKey;

    @Value("${jwt.token.signing.base64}")
    private boolean isSigningBase64Key;

    @Value("${jwt.token.signing.expiration-ttl}")
    @Getter
    private Duration expirationSigningTokenTtl;

    @Value("${jwt.bearer-prefix}")
    @Getter
    private String bearerPrefix;

    @Value("${jwt.token.signing.header}")
    @Getter
    private String tokenHeader;

    /**
     * Получение ключа для подписи токена
     *
     * @return ключ
     */
    public SecretKey getSigningKey() {
        var keyBytes = isSigningBase64Key
            ? Decoders.BASE64.decode(signingKey)
            : signingKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
