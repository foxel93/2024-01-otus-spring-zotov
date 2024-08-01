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
import ru.otus.hw.dao.jpa.BookJapDao;
import ru.otus.hw.dao.mongo.BookMongoDao;
import ru.otus.hw.processor.ProcessorManager;

@Configuration
public class BookStepConfig {
    @Bean
    public MongoPagingItemReader<BookMongoDao> bookReader(MongoTemplate template) {
        return new MongoPagingItemReaderBuilder<BookMongoDao>()
            .name("bookMongoReader")
            .template(template)
            .jsonQuery("{}")
            .targetType(BookMongoDao.class)
            .sorts(new HashMap<>())
            .build();
    }

    @Bean
    public ItemProcessor<BookMongoDao, BookJapDao> bookProcessor(ProcessorManager processorManager) {
        return processorManager::bookProcess;
    }

    @Bean
    public JpaItemWriter<BookJapDao> bookWriter(EntityManager entityManager) {
        return new JpaItemWriterBuilder<BookJapDao>()
            .entityManagerFactory(entityManager.getEntityManagerFactory())
            .build();
    }

    @Bean
    public Step transformBooksStepBean(
        ItemReader<BookMongoDao> reader,
        JpaItemWriter<BookJapDao> writer,
        ItemProcessor<BookMongoDao, BookJapDao> itemProcessor,
        PlatformTransactionManager platformTransactionManager,
        JobRepository jobRepository
    ) {
        return new StepBuilder("transformBooksStep", jobRepository)
            .<BookMongoDao, BookJapDao>chunk(CHUNK_SIZE, platformTransactionManager)
            .reader(reader)
            .processor(itemProcessor)
            .writer(writer)
            .build();
    }
}
