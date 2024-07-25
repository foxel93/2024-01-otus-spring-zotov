package ru.otus.hw.mappers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.dao.SingerDao;
import ru.otus.hw.dto.singer.SingerCreateDto;
import ru.otus.hw.dto.singer.SingerDto;
import ru.otus.hw.dto.singer.SingerUpdateDto;

@Component
@AllArgsConstructor
public class SingerMapper {
    public SingerDao toDao(SingerCreateDto singerDto) {
        return SingerDao.builder()
            .fullname(singerDto.getFullname())
            .build();
    }

    public SingerDao toDao(SingerUpdateDto singerUpdateDto, long id) {
        return SingerDao.builder()
            .fullname(singerUpdateDto.getFullname())
            .id(id)
            .build();
    }

    public SingerDto toDto(SingerDao singerDao) {
        return SingerDto.builder()
            .fullname(singerDao.getFullname())
            .id(singerDao.getId())
            .build();
    }
}
