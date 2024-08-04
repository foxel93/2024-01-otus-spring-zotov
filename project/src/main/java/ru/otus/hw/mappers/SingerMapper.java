package ru.otus.hw.mappers;

import java.util.Collection;
import java.util.List;
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

    public Singer toDao(SingerUpdateDto singerUpdateDto, Singer singer) {
        singer.setFullname(singerUpdateDto.getFullname());
        return singer;
    }

    public SingerDto toDto(Singer singer) {
        return SingerDto.builder()
            .fullname(singer.getFullname())
            .id(singer.getId())
            .build();
    }

    public List<SingerDto> toDto(Collection<Singer> singers) {
        return singers
            .stream()
            .map(this::toDto)
            .toList();
    }
}
