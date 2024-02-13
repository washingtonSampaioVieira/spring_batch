package com.springbatch.demo.job.creditcard.reader;

import com.springbatch.demo.dominio.Client;
import com.springbatch.demo.dominio.CreditCard;
import com.springbatch.demo.dominio.Transaction;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

@Configuration
public class ReadTransactionsReaderConfig {
    @Bean
    public JdbcCursorItemReader<Transaction> lerTransacoesReader(
            @Qualifier("appDataSource") DataSource dataSource) {
        return new JdbcCursorItemReaderBuilder<Transaction>()
                .name("lerTransacoesReader")
                .dataSource(dataSource)
                .sql("select * from transacao join cartao_credito using (numero_cartao_credito) order by numero_cartao_credito")
                .rowMapper(rowMapperTransaction())
                .build();
    }

    private RowMapper<Transaction> rowMapperTransaction() {
        return new RowMapper<Transaction>() {

            @Override
            public Transaction mapRow(ResultSet rs, int rowNum) throws SQLException {
                CreditCard cartCredit = new CreditCard();
                cartCredit.setNumberCreditCard(rs.getInt("numero_cartao_credito"));
                Client client = new Client();
                client.setId(rs.getInt("cliente"));
                cartCredit.setClient(client);

                Transaction transaction = new Transaction();
                transaction.setId(rs.getInt("id"));
                transaction.setCreditCard(cartCredit);
                transaction.setData(rs.getDate("data"));
                transaction.setValue(rs.getDouble("valor"));
                transaction.setDescription(rs.getString("descricao"));

                return transaction;
            }

        };
    }
}
