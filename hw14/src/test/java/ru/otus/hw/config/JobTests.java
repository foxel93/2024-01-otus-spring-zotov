package ru.otus.hw.config;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.hw.helper.BookJpaRepository;

@SpringBootTest
@SpringBatchTest
public class JobTests {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    @Qualifier("migrateBooksJob")
    private Job bookJob;

    @Autowired
    private JobRepositoryTestUtils jobRepositoryTestUtils;

    @Autowired
    private BookJpaRepository bookJpaRepository;

    @BeforeEach
    void initJobs() {
        jobRepositoryTestUtils.removeJobExecutions();
    }

    @Test
    public void testBookMigrationJob() throws Exception {
        assertThat(bookJpaRepository.findAll().size()).isEqualTo(0);

        jobLauncherTestUtils.setJob(bookJob);

        var job = jobLauncherTestUtils.getJob();
        assertThat(job).isNotNull().extracting(Job::getName).isEqualTo(JobConfig.JOB_NAME);

        var jobExecution = jobLauncherTestUtils.launchJob();
        assertThat(jobExecution.getExitStatus().getExitCode()).isEqualTo("COMPLETED");

        assertThat(bookJpaRepository.findAll().size()).isGreaterThan(0);
    }
}
