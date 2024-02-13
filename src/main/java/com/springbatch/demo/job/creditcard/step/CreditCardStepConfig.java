package com.springbatch.demo.job.creditcard.step;

import com.springbatch.demo.dominio.CreditCardBill;
import com.springbatch.demo.dominio.Transaction;
import com.springbatch.demo.job.creditcard.reader.CreditCardBillReader;
import com.springbatch.demo.job.creditcard.writer.TotalTransactionsFooterCallback;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class CreditCardStepConfig {


    @Bean
    public Step faturaCartaoCreditoStep(
            ItemStreamReader<Transaction> reader,
            ItemProcessor<CreditCardBill, CreditCardBill> processor,
            ItemWriter<CreditCardBill> writer,
            TotalTransactionsFooterCallback listener,
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager) {

        return new StepBuilder("CreditCardStep", jobRepository)
                .<CreditCardBill, CreditCardBill>chunk(1, transactionManager)
                .reader(new CreditCardBillReader(reader))
                .processor(processor)
                .writer(writer)
//                .listener(listener)
                .build();
    }
}
