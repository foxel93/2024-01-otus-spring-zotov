package ru.otus.hw.dto.album;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AlbumDto {
    private long id;

    private String name;
}
