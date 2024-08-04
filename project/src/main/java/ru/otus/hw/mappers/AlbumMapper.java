package ru.otus.hw.mappers;

import java.util.Collection;
import java.util.List;
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

    public Album toDao(AlbumUpdateDto albumUpdateDto, Album album) {
        album.setName(albumUpdateDto.getName());
        return album;
    }

    public AlbumDto toDto(Album album) {
        return AlbumDto.builder()
            .name(album.getName())
            .id(album.getId())
            .build();
    }

    public List<AlbumDto> toDto(Collection<Album> albums) {
        return albums
            .stream()
            .map(this::toDto)
            .toList();
    }
}
