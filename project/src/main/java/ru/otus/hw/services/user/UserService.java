package ru.otus.hw.services.user;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.otus.hw.dto.user.UserCreateDto;
import ru.otus.hw.dto.user.UserDto;

public interface UserService extends UserDetailsService {
    UserDto create(UserCreateDto userCreateDto);
}
