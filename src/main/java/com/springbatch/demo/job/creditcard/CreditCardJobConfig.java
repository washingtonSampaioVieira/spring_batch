package com.springbatch.demo.job.creditcard;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CreditCardJobConfig {

    @Bean
    public Job creditCardBillJob(Step creditCardBillStep, JobRepository jobRepository) {
        return new JobBuilder("CreditCardJob", jobRepository)
                .start(creditCardBillStep)
                .incrementer(new RunIdIncrementer())
                .build();
    }
}
