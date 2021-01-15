package xyz.krakenkat.currencyexchangeservice.controller;

import lombok.AllArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import xyz.krakenkat.currencyexchangeservice.domain.model.ExchangeValue;
import xyz.krakenkat.currencyexchangeservice.domain.repository.ExchangeValueRepository;

@RestController
@AllArgsConstructor
public class CurrencyExchangeController {

    private ExchangeValueRepository exchangeValueRepository;

    private Environment environment;

    @GetMapping("/currency-exchange/from/{from}/to/{to}")
    public ExchangeValue retrieveExchangeValue(@PathVariable String from, @PathVariable String to) {
        ExchangeValue exchangeValue = exchangeValueRepository.findByFromAndTo(from, to);
        exchangeValue.setPort(Integer.valueOf(environment.getProperty("local.server.port")));
        return exchangeValue;
    }
}
