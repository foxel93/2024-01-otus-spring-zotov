package ru.otus.hw.mappers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.dao.AlbumDao;
import ru.otus.hw.dto.album.AlbumCreateDto;
import ru.otus.hw.dto.album.AlbumDto;
import ru.otus.hw.dto.album.AlbumUpdateDto;

@Component
@AllArgsConstructor
public class AlbumMapper {
    public AlbumDao toDao(AlbumCreateDto albumCreateDto) {
        return AlbumDao.builder()
            .name(albumCreateDto.getName())
            .build();
    }

    public AlbumDao toDao(AlbumUpdateDto albumUpdateDto, long id) {
        return AlbumDao.builder()
            .name(albumUpdateDto.getName())
            .id(id)
            .build();
    }

    public AlbumDto toDto(AlbumDao albumDao) {
        return AlbumDto.builder()
            .name(albumDao.getName())
            .id(albumDao.getId())
            .build();
    }
}
