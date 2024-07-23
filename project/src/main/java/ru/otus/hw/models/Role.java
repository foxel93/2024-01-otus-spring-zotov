package ru.otus.hw.models;

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
    UNKNOWN("ROLE_UNKNOWN"),
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");

    private static final Map<String, Role> CACHE = Arrays
        .stream(Role.values())
        .collect(Collectors.toMap(e -> e.authority, Function.identity()));

    private final String authority;

    public static Role of(String name) {
        return CACHE.getOrDefault(name.toUpperCase(), UNKNOWN);
    }
}
