package com.springbatch.demo.dominio;

import lombok.Data;

import java.util.Date;

@Data
public class Transaction {
	private int id;
	private CreditCard creditCard;
	private String description;
	private Double value;
	private Date data;
}
