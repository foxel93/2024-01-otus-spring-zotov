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
public class BookUpdateDto {
    @NotEmpty
    private String id;

    @NotEmpty
    private String title;

    @NotEmpty
    private String authorId;

    @NotEmpty
    private Set<@NotNull String> genreIds;
}
