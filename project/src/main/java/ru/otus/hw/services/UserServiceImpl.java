package ru.otus.hw.services;

import jakarta.persistence.EntityExistsException;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.user.UserCreateDto;
import ru.otus.hw.dto.user.UserDto;
import ru.otus.hw.mappers.UserMapper;
import ru.otus.hw.repositories.UserRepository;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    private final UserMapper userMapper;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository
            .findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User with username '%s' not found".formatted(username)));
    }

    @Override
    @Transactional
    public UserDto create(UserCreateDto userCreateDto) {
        var userName = userCreateDto.getUsername();
        if (repository.existsByUsername(userName)) {
            throw new EntityExistsException("User with username '%s' exists".formatted(userName));
        }
        var userDao = userMapper.toUserDao(userCreateDto);
        var savedUserDao = repository.save(userDao);
        return userMapper.toUserDto(savedUserDao);
    }
}
