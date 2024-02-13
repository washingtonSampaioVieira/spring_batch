package com.springbatch.demo.dominio;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Data
public class CreditCardBill {
	private Client client;
	private CreditCard creditCard;
	private final List<Transaction> transactions = new ArrayList<>();
	
	public Double getTotal() {
		return transactions
				.stream()
				.mapToDouble(Transaction::getValue)
				.reduce(0.0, Double::sum);
	}
}
