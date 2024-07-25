package ru.otus.hw.dto.singer;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SingerDto {
    private long id;

    private String fullname;
}
