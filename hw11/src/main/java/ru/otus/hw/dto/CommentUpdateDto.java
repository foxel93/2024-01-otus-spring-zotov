package ru.otus.hw.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentUpdateDto {
    @NotEmpty
    private String id;

    @NotEmpty
    private String text;
}
