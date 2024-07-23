package ru.otus.hw.mappers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.dao.UserDao;
import ru.otus.hw.dto.user.UserCreateDto;
import ru.otus.hw.dto.user.UserDto;

@Component
@AllArgsConstructor
public class UserMapper {
    public UserDao toUserDao(UserCreateDto userCreateDto) {
        return UserDao.builder()
            .username(userCreateDto.getUsername())
            .password(userCreateDto.getPassword())
            .role(userCreateDto.getRole())
            .build();
    }

    public UserDto toUserDto(UserDao userDao) {
        return UserDto.builder()
            .id(userDao.getId())
            .username(userDao.getUsername())
            .role(userDao.getRole())
            .build();
    }
}
