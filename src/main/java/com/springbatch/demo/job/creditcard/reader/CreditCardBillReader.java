package com.springbatch.demo.job.creditcard.reader;

import com.springbatch.demo.dominio.CreditCardBill;
import com.springbatch.demo.dominio.Transaction;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamReader;

public class CreditCardBillReader implements ItemStreamReader<CreditCardBill> {
    private ItemStreamReader<Transaction> delegate;
    private Transaction transactionActual;

    @Override
    public CreditCardBill read() throws Exception {
        if (transactionActual == null)
            transactionActual = delegate.read();

        CreditCardBill faturaCartaoCredito = null;
        Transaction transaction = transactionActual;
        transactionActual = null;

        if (transaction != null) {
            faturaCartaoCredito = new CreditCardBill();
            faturaCartaoCredito.setCreditCard(transaction.getCreditCard());
            faturaCartaoCredito.setClient(transaction.getCreditCard().getClient());
            faturaCartaoCredito.getTransactions().add(transaction);

            while (isTransactionRelational(transaction)) {
                faturaCartaoCredito.getTransactions().add(transactionActual);
            }
        }
        return faturaCartaoCredito;
    }

    private boolean isTransactionRelational(Transaction transaction) throws Exception {
        return peek() != null && transaction.getCreditCard().getNumberCreditCard() == transactionActual.getCreditCard().getNumberCreditCard();
    }

    private Transaction peek() throws Exception {
        transactionActual = delegate.read();
        return transactionActual;
    }

    public CreditCardBillReader(ItemStreamReader<Transaction> delegate) {
        this.delegate = delegate;
    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        delegate.open(executionContext);
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        delegate.update(executionContext);
    }

    @Override
    public void close() throws ItemStreamException {
        delegate.close();
    }

}
