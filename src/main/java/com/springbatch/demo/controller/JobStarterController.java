package com.springbatch.demo.controller;


import com.springbatch.demo.controller.dto.ResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/job/creditcard/v1")
@Slf4j
public class JobStarterController {

    @Autowired
    private Job job;

    @Autowired
    ThreadPoolTaskExecutor poolTaskExecutor;

    @Autowired
    JobLauncher jobLauncher;

    @GetMapping("/start")
    public ResponseEntity<ResponseDTO> startJobCreditCard() {
        var initialTime = System.nanoTime();
        log.info("Starting JOB from method GET - JOB: {}", job.getName());

        poolTaskExecutor.execute(() -> {
            JobParameters jobParameter = new JobParametersBuilder().addString("DateTime", LocalDateTime.now().toString()).toJobParameters();
            try {
                jobLauncher.run(job, jobParameter);
            } catch (Exception e) {
                log.error("An error has occurred during the execution of JOB {}", job.getName());
            }
        });
        log.info("JOB {} started in {} milliseconds", job.getName(), TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - initialTime));

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(
                ResponseDTO.builder().code(HttpStatus.ACCEPTED.value()).status(HttpStatus.ACCEPTED.name())
                        .build());
    }

}

