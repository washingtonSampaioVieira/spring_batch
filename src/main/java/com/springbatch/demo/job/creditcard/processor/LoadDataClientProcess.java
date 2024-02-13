package com.springbatch.demo.job.creditcard.processor;

import com.springbatch.demo.dominio.Client;
import com.springbatch.demo.dominio.CreditCardBill;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class LoadDataClientProcess implements ItemProcessor<CreditCardBill, CreditCardBill> {
    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public CreditCardBill process(CreditCardBill creditCardBill) throws Exception {
        String uri = String.format("http://my-json-server.typicode.com/washingtonsampaiovieira/demo/profile/%d", creditCardBill.getClient().getId());
        ResponseEntity<Client> response = restTemplate.getForEntity(uri, Client.class);

        if (response.getStatusCode() != HttpStatus.OK)
            throw new ValidationException("Client not found!");

        creditCardBill.setClient(response.getBody());
        return creditCardBill;
    }

}
