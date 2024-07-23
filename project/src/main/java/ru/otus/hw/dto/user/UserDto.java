package ru.otus.hw.dto.user;

import lombok.Builder;
import lombok.Data;
import ru.otus.hw.models.Role;

@Data
@Builder
public class UserDto {
    private long id;

    private String username;

    private Role role;
}
