package com.springbatch.demo.job.creditcard.writer;

import com.springbatch.demo.dominio.CreditCardBill;
import com.springbatch.demo.dominio.Transaction;
import org.springframework.batch.item.file.*;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.builder.MultiResourceItemWriterBuilder;
import org.springframework.batch.item.file.transform.LineAggregator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import java.io.IOException;
import java.io.Writer;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

@Configuration
public class FileCreditCardBillWriterConfig {
	@Bean
	public MultiResourceItemWriter<CreditCardBill> filesCreditCardsBill() {
		return new MultiResourceItemWriterBuilder<CreditCardBill>()
				.name("filesCreditCardsBill")
				.resource(new FileSystemResource("files/bill"))
				.itemCountLimitPerResource(1)
				.resourceSuffixCreator(suffixCreator())
				.delegate(fileCreditCardBill())
				.build();
	}

	private FlatFileItemWriter<CreditCardBill> fileCreditCardBill() {
		return new FlatFileItemWriterBuilder<CreditCardBill>()
				.name("fileCreditCardBill")
				.resource(new FileSystemResource("files/fatura.txt"))
				.lineAggregator(lineAggregator())
				.headerCallback(headerCallback())
				.footerCallback(footerCallback())
				.build();
	}

	@Bean
	public FlatFileFooterCallback footerCallback() {
		return new TotalTransactionsFooterCallback();
	}

	private FlatFileHeaderCallback headerCallback() {
		return new FlatFileHeaderCallback() {
			
			@Override
			public void writeHeader(Writer writer) throws IOException {
				writer.append(String.format("%121s\n", "Cartão XPTO"));
				writer.append(String.format("%121s\n\n", "Rua Vergueiro, 131"));
			}
		};
	}

	private LineAggregator<CreditCardBill> lineAggregator() {
		return new LineAggregator<CreditCardBill>() {

			@Override
			public String aggregate(CreditCardBill creditCardBill) {
				StringBuilder writer = new StringBuilder();
				writer.append(String.format("Nome: %s\n", creditCardBill.getClient().getName()));
				writer.append(String.format("Endereço: %s\n\n\n", creditCardBill.getClient().getAddress()));
				writer.append(String.format("Fatura completa do cartão %d\n", creditCardBill.getCreditCard().getNumberCreditCard()));
				writer.append("-------------------------------------------------------------------------------------------------------------------------\n");
				writer.append("DATA DESCRICAO VALOR\n");
				writer.append("-------------------------------------------------------------------------------------------------------------------------\n");
				
				for (Transaction transacao : creditCardBill.getTransactions()) {
					writer.append(String.format("\n[%10s] %-80s - %s",
							new SimpleDateFormat("dd/MM/yyyy").format(transacao.getData()),
							transacao.getDescription(),
							NumberFormat.getCurrencyInstance(new Locale("pt","BR")).format(transacao.getValue())));
				}
				return writer.toString();
			}
			
		};
	}

	private ResourceSuffixCreator suffixCreator() {
		return new ResourceSuffixCreator() {
			
			@Override
			public String getSuffix(int index) {
				return index + ".txt";
			}
		};
	}
}
