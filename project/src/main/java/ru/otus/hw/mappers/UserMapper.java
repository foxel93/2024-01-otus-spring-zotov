package ru.otus.hw.mappers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.User;
import ru.otus.hw.dto.user.UserCreateDto;
import ru.otus.hw.dto.user.UserDto;

@Component
@AllArgsConstructor
public class UserMapper {
    public User toUserDao(UserCreateDto userCreateDto) {
        return User.builder()
            .username(userCreateDto.getUsername())
            .password(userCreateDto.getPassword())
            .role(userCreateDto.getRole())
            .build();
    }

    public UserDto toUserDto(User user) {
        return UserDto.builder()
            .id(user.getId())
            .username(user.getUsername())
            .role(user.getRole())
            .build();
    }
}
