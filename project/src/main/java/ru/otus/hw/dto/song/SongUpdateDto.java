package ru.otus.hw.dto.song;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SongUpdateDto {
    private String name;

    @NotNull
    private Long singerId;

    @NotNull
    private Long albumId;

    @NotNull
    private Long genreId;
}
