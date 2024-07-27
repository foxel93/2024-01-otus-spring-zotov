package ru.otus.hw.rest;

import jakarta.validation.Valid;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.song.SongCreateDto;
import ru.otus.hw.dto.song.SongDto;
import ru.otus.hw.dto.song.SongUpdateDto;
import ru.otus.hw.services.song.SongService;

@RestController
@RequestMapping("api/v1/songs")
@AllArgsConstructor(onConstructor_ = @Autowired)
public class SongController {
    private final SongService songService;

    @GetMapping
    public List<SongDto> getAll() {
        return songService.findAll();
    }

    @GetMapping("/{id}")
    public SongDto getById(@PathVariable("id") long id) {
        return songService.findById(id);
    }

    @GetMapping(params = "genreId")
    public List<SongDto> getAllByGenreId(long genreId) {
        return songService.findAllByGenre(genreId);
    }

    @GetMapping(params = "albumId")
    public List<SongDto> getAllByAlbumId(long albumId) {
        return songService.findAllByAlbum(albumId);
    }

    @GetMapping(params = "singerId")
    public List<SongDto> getAllBySingerId(long singerId) {
        return songService.findAllBySinger(singerId);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public SongDto create(@RequestBody @Valid SongCreateDto songCreateDto) {
        return songService.create(songCreateDto);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public SongDto update(@PathVariable long id, @RequestBody @Valid SongUpdateDto songUpdateDto) {
        return songService.update(id, songUpdateDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteById(@PathVariable("id") long id) {
        songService.deleteById(id);
    }
}
