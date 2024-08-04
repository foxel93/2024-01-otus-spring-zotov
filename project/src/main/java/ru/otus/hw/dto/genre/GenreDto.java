package ru.otus.hw.dto.genre;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GenreDto {
    private long id;

    private String name;
}
