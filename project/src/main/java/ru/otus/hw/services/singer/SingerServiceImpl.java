package ru.otus.hw.services.singer;

import java.util.Comparator;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.singer.SingerCreateDto;
import ru.otus.hw.dto.singer.SingerDto;
import ru.otus.hw.dto.singer.SingerUpdateDto;
import ru.otus.hw.exceptions.NotFoundException;
import ru.otus.hw.mappers.SingerMapper;
import ru.otus.hw.repositories.SingerRepository;

@Service
@AllArgsConstructor
public class SingerServiceImpl implements SingerService {
    private final SingerMapper singerMapper;

    private final SingerRepository singerRepository;

    @Override
    @Transactional(readOnly = true)
    public List<SingerDto> findAll() {
        return singerRepository.findAll()
            .stream()
            .map(singerMapper::toDto)
            .sorted(Comparator.comparing(SingerDto::getFullname))
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public SingerDto findByName(String name) {
        return singerRepository.findByFullname(name)
            .map(singerMapper::toDto)
            .orElseThrow(() -> new NotFoundException("Singer with name={%s} not found".formatted(name)));
    }

    @Override
    public SingerDto findById(long id) {
        return singerRepository.findById(id)
            .map(singerMapper::toDto)
            .orElseThrow(() -> new NotFoundException("Singer with id={%d} not found".formatted(id)));
    }

    @Override
    @Transactional
    public SingerDto update(long id, SingerUpdateDto singerUpdateDto) {
        return singerRepository
            .findById(id)
            .map(singer -> singerMapper.toDao(singerUpdateDto, singer))
            .map(singerRepository::save)
            .map(singerMapper::toDto)
            .orElseThrow(() -> new NotFoundException("Singer with id={%d} not found".formatted(id)));
    }

    @Override
    public SingerDto create(SingerCreateDto singerCreateDto) {
        var singerCreate = singerMapper.toDao(singerCreateDto);
        var createdSinger = singerRepository.save(singerCreate);
        return singerMapper.toDto(createdSinger);
    }

    @Override
    public void deleteById(long id) {
        singerRepository.deleteById(id);
    }
}
