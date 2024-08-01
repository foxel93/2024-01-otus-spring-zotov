package ru.otus.hw.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Log4j2
@RequiredArgsConstructor

public class JobConfig {
    public static final String JOB_NAME = "migrationBookJob";

    public static final int CHUNK_SIZE = 4;

    @Bean
    public Job migrateBooksJob(
        Step transformBooksStepBean,
        Step transformAuthorsStepBean,
        Step transformGenresStepBean,
        Step cleanUpStepBean,
        JobRepository jobRepository
    ) {
        return new JobBuilder(JOB_NAME, jobRepository)
            .incrementer(new RunIdIncrementer())
            .flow(transformAuthorsStepBean)
            .next(transformGenresStepBean)
            .next(transformBooksStepBean)
            .next(cleanUpStepBean)
            .end()
            .build();
    }
}
