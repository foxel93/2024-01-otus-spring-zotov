package ru.otus.hw.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
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
import ru.otus.hw.dto.singer.SingerCreateDto;
import ru.otus.hw.dto.singer.SingerDto;
import ru.otus.hw.dto.singer.SingerUpdateDto;
import ru.otus.hw.services.SingerService;

@RestController
@RequestMapping("api/v1/singers")
@AllArgsConstructor(onConstructor_ = @Autowired)
public class SingerController {
    private final SingerService singerService;

    @GetMapping
    public List<SingerDto> getAll() {
        return singerService.findAll();
    }

    @GetMapping("/{id}")
    public SingerDto getById(@PathVariable("id") long id) {
        return singerService.findById(id);
    }

    @GetMapping(params = "name")
    public SingerDto getByName(@Size(min = 1, max = 100) String name) {
        return singerService.findByName(name);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public SingerDto create(@RequestBody @Valid SingerCreateDto singerCreateDto) {
        return singerService.create(singerCreateDto);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public SingerDto update(@PathVariable long id, @RequestBody @Valid SingerUpdateDto singerUpdateDto) {
        return singerService.update(id, singerUpdateDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteById(@PathVariable("id") long id) {
        singerService.deleteById(id);
    }
}
