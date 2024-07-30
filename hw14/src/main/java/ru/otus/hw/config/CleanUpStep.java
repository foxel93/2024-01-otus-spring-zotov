package ru.otus.hw.config;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.MethodInvokingTaskletAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import ru.otus.hw.processor.CleanUpService;

@Configuration
public class CleanUpStep {
    @Bean
    public Step cleanUpStepBean(
        CleanUpService cleanUpService,
        JobRepository jobRepository,
        PlatformTransactionManager platformTransactionManager
    ) {
        return new StepBuilder("cleanUpStep", jobRepository)
            .tasklet(cleanUpTasklet(cleanUpService), platformTransactionManager)
            .build();
    }

    private MethodInvokingTaskletAdapter cleanUpTasklet(CleanUpService cleanUpService) {
        var adapter = new MethodInvokingTaskletAdapter();

        adapter.setTargetObject(cleanUpService);
        adapter.setTargetMethod("cleanUp");

        return adapter;
    }
}
