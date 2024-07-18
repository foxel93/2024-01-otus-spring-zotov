package ru.otus.hw.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Accessors(fluent = true)
public enum Role {
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");

    @JsonValue
    private final String roleName;

    @JsonCreator
    public static Role of(String name) {
        return switch (name) {
            case "ROLE_ADMIN", "ADMIN" -> ADMIN;
            default -> USER;
        };
    }
}
