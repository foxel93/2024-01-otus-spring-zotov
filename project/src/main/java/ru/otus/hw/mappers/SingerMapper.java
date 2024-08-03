package ru.otus.hw.mappers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.Singer;
import ru.otus.hw.dto.singer.SingerCreateDto;
import ru.otus.hw.dto.singer.SingerDto;
import ru.otus.hw.dto.singer.SingerUpdateDto;

@Component
@AllArgsConstructor
public class SingerMapper {
    public Singer toDao(SingerCreateDto singerDto) {
        return Singer.builder()
            .fullname(singerDto.getFullname())
            .build();
    }

    public Singer toDao(SingerUpdateDto singerUpdateDto, long id) {
        return Singer.builder()
            .fullname(singerUpdateDto.getFullname())
            .id(id)
            .build();
    }

    public SingerDto toDto(Singer singer) {
        return SingerDto.builder()
            .fullname(singer.getFullname())
            .id(singer.getId())
            .build();
    }
}
