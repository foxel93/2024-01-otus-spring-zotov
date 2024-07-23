package ru.otus.hw.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.annotation.Nullable;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum Role implements GrantedAuthority {
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");

    private static final Map<String, Role> CACHE = Arrays
        .stream(Role.values())
        .collect(Collectors.toMap(e -> e.authority, Function.identity()));

    private final String authority;

    @JsonCreator
    public static Role of(@Nullable String name) {
        if (name == null) {
            return USER;
        }
        return CACHE.getOrDefault(name.toUpperCase(), USER);
    }
}
