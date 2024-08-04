package ru.otus.hw.dto.song;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SongUpdateDto {
    @NotBlank
    private String name;

    @NotEmpty
    private Set<@NotNull Long> singerIds;

    @NotEmpty
    private Set<@NotNull Long> albumIds;

    @NotEmpty
    private Set<@NotNull Long> genreIds;
}
