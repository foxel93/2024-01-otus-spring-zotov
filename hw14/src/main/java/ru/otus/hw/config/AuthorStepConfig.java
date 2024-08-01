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
import ru.otus.hw.dao.jpa.AuthorJpaDao;
import ru.otus.hw.dao.mongo.AuthorMongoDao;
import ru.otus.hw.processor.ProcessorManager;

@Configuration
public class AuthorStepConfig {
    @Bean
    public MongoPagingItemReader<AuthorMongoDao> authorReader(MongoTemplate template) {
        return new MongoPagingItemReaderBuilder<AuthorMongoDao>()
            .name("authorMongoReader")
            .template(template)
            .jsonQuery("{}")
            .targetType(AuthorMongoDao.class)
            .sorts(new HashMap<>())
            .build();
    }

    @Bean
    public ItemProcessor<AuthorMongoDao, AuthorJpaDao> authorProcessor(ProcessorManager processorManager) {
        return processorManager::authorProcess;
    }

    @Bean
    public JpaItemWriter<AuthorJpaDao> authorWriter(EntityManager entityManager) {
        return new JpaItemWriterBuilder<AuthorJpaDao>()
            .entityManagerFactory(entityManager.getEntityManagerFactory())
            .build();
    }

    @Bean
    public Step transformAuthorsStepBean(
        ItemReader<AuthorMongoDao> reader,
        JpaItemWriter<AuthorJpaDao> writer,
        ItemProcessor<AuthorMongoDao, AuthorJpaDao> itemProcessor,
        PlatformTransactionManager platformTransactionManager,
        JobRepository jobRepository
    ) {
        return new StepBuilder("transformAuthorsStep", jobRepository)
            .<AuthorMongoDao, AuthorJpaDao>chunk(CHUNK_SIZE, platformTransactionManager)
            .reader(reader)
            .processor(itemProcessor)
            .writer(writer)
            .build();
    }
}
