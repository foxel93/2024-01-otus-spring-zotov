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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.album.AlbumCreateDto;
import ru.otus.hw.dto.album.AlbumDto;
import ru.otus.hw.dto.album.AlbumUpdateDto;
import ru.otus.hw.services.album.AlbumService;
import ru.otus.hw.utils.UtilsController;

@RestController
@RequestMapping("api/v1/albums")
@AllArgsConstructor(onConstructor_ = @Autowired)
public class AlbumController {
    private final AlbumService albumService;

    @GetMapping
    public List<AlbumDto> getAll(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "3") int size,
        @RequestParam(defaultValue = "id,desc", name = "sort_by") String[] sorts
    ) {
        var pagingSort = UtilsController.pageRequest(page, size, sorts);
        return albumService.findAll(pagingSort);
    }

    @GetMapping("/{id}")
    public AlbumDto getById(@PathVariable("id") long id) {
        return albumService.findById(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public AlbumDto create(@RequestBody @Valid AlbumCreateDto albumCreateDto) {
        return albumService.create(albumCreateDto);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public AlbumDto update(@PathVariable long id, @RequestBody @Valid AlbumUpdateDto albumUpdateDto) {
        return albumService.update(id, albumUpdateDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteById(@PathVariable("id") long id) {
        albumService.deleteById(id);
    }
}
