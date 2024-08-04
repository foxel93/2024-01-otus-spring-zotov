package ru.otus.hw.services.singer;

import java.util.List;
import org.springframework.data.domain.Pageable;
import ru.otus.hw.dto.singer.SingerCreateDto;
import ru.otus.hw.dto.singer.SingerDto;
import ru.otus.hw.dto.singer.SingerUpdateDto;

public interface SingerService {
    List<SingerDto> findAll(Pageable pageable);

    SingerDto findByName(String name);

    SingerDto findById(long id);

    SingerDto update(long id, SingerUpdateDto singerUpdateDto);

    SingerDto create(SingerCreateDto singerCreateDto);

    void deleteById(long id);
}
