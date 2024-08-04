package ru.otus.hw.dto.song;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SongFindDto {
    private Long singerId;

    private Long albumId;

    private Long genreId;
}
