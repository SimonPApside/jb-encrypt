package com.example.batchprocessing;

import java.io.File;

import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class FileEncryptionJobConfig {

    private static final Logger log = LoggerFactory.getLogger(FileEncryptionJobConfig.class);

    @Bean
    Job encryptFilesJob(JobRepository jobRepository, Step encryptFilesStep) {
        return new JobBuilder("encryptFilesJob", jobRepository)
                .start(encryptFilesStep)
                .build();
    }

    @Bean
    public Step encryptFilesStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
            AppProperties appProperties)
            throws Exception {

        log.info("Mode: " + appProperties.getMode());
        log.info("Input Dir: " + appProperties.getInputDir());
        log.info("Output Dir: " + appProperties.getOutputDir());

        SecretKey key = AESUtil.generateKey(appProperties.getPassword());

        ItemReader<File> reader = new DirectoryFileReader(appProperties.getInputDir());

        ItemProcessor<File, CustomFile> processor;

        if ("decrypt".equalsIgnoreCase(appProperties.getMode())) {
            processor = new FileDecryptProcessor(key);
        } else {
            processor = new FileEncryptProcessor(key);
        }

        ItemWriter<CustomFile> writer = new FileEncryptWriter(appProperties.getOutputDir());

        return new StepBuilder("encryptFilesStep", jobRepository).<File, CustomFile>chunk(5,
                transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }
}
