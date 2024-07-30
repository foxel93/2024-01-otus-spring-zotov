package ru.otus.hw.config;

import static ru.otus.hw.config.JobConfig.CHUNK_SIZE;

import jakarta.persistence.EntityManager;
import java.util.HashMap;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.data.MongoPagingItemReader;
import org.springframework.batch.item.data.builder.MongoPagingItemReaderBuilder;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import ru.otus.hw.dao.jpa.GenreJpaDao;
import ru.otus.hw.dao.mongo.GenreMongoDao;
import ru.otus.hw.processor.ProcessorManager;

@Configuration
public class GenreStepConfig {
    @Bean
    public MongoPagingItemReader<GenreMongoDao> genreReader(MongoTemplate template) {
        return new MongoPagingItemReaderBuilder<GenreMongoDao>()
            .name("genreMongoReader")
            .template(template)
            .jsonQuery("{}")
            .targetType(GenreMongoDao.class)
            .sorts(new HashMap<>())
            .build();
    }

    @Bean
    public ItemProcessor<GenreMongoDao, GenreJpaDao> genreProcessor(ProcessorManager processorManager) {
        return processorManager::genreProcess;
    }

    @Bean
    public JpaItemWriter<GenreJpaDao> genreWriter(EntityManager entityManager) {
        return new JpaItemWriterBuilder<GenreJpaDao>()
            .entityManagerFactory(entityManager.getEntityManagerFactory())
            .build();
    }

    @Bean
    public Step transformGenresStepBean(
        ItemReader<GenreMongoDao> reader,
        JpaItemWriter<GenreJpaDao> writer,
        ItemProcessor<GenreMongoDao, GenreJpaDao> itemProcessor,
        PlatformTransactionManager platformTransactionManager,
        JobRepository jobRepository
    ) {
        return new StepBuilder("transformGenresStep", jobRepository)
            .<GenreMongoDao, GenreJpaDao>chunk(CHUNK_SIZE, platformTransactionManager)
            .reader(reader)
            .processor(itemProcessor)
            .writer(writer)
            .build();
    }
}
