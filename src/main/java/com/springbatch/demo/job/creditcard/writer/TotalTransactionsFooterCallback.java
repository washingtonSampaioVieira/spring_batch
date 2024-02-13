package com.springbatch.demo.job.creditcard.writer;

import com.springbatch.demo.dominio.CreditCardBill;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.file.FlatFileFooterCallback;

import java.io.IOException;
import java.io.Writer;
import java.text.NumberFormat;
import java.util.List;

public class TotalTransactionsFooterCallback implements FlatFileFooterCallback {
	private Double total = 0.0;
	
	@Override
	public void writeFooter(Writer writer) throws IOException {
		writer.write(String.format("\n%121s", "Total: " + NumberFormat.getCurrencyInstance().format(total)));
	}

//	@BeforeChunk
	public void beforeWrite(List<CreditCardBill> creditCardBills) {
		for (CreditCardBill creditCardBill : creditCardBills)
			total += creditCardBill.getTotal();
	}
	
//	@AfterChunk
	public void afterChunk(ChunkContext chunkContext) {
		total = 0.0;
	}
}
