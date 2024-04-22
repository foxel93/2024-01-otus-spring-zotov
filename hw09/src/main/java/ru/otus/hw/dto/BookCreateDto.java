package ru.otus.hw.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookCreateDto {
    @NotEmpty
    private String title;

    @NotNull
    private Long authorId;

    @NotEmpty
    private Set<Long> genreIds;
}
