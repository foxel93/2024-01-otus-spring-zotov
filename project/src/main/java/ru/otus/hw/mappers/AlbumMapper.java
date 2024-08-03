package ru.otus.hw.mappers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.Album;
import ru.otus.hw.dto.album.AlbumCreateDto;
import ru.otus.hw.dto.album.AlbumDto;
import ru.otus.hw.dto.album.AlbumUpdateDto;

@Component
@AllArgsConstructor
public class AlbumMapper {
    public Album toDao(AlbumCreateDto albumCreateDto) {
        return Album.builder()
            .name(albumCreateDto.getName())
            .build();
    }

    public Album toDao(AlbumUpdateDto albumUpdateDto, long id) {
        return Album.builder()
            .name(albumUpdateDto.getName())
            .id(id)
            .build();
    }

    public AlbumDto toDto(Album album) {
        return AlbumDto.builder()
            .name(album.getName())
            .id(album.getId())
            .build();
    }
}
