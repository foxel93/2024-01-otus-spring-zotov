package ru.otus.hw.dto.jwt;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignUpRequest {
    @Size(min = 5, max = 50, message = "Имя пользователя должно содержать от 5 до 50 символов")
    @NotBlank(message = "Имя пользователя не может быть пустым")
    private String username;

    @Size(min = 8, max = 255, message = "Длина пароля должна быть не менее 8 и не более 255 символов")
    @NotBlank(message = "Пароль не может быть пустым")
    private String password;
}
