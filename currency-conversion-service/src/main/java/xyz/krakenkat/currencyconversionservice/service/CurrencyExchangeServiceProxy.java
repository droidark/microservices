package xyz.krakenkat.currencyconversionservice.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import xyz.krakenkat.currencyconversionservice.domain.model.CurrencyConversion;

// NAME: THE SERVICE NAME TO CONSUME [DEFINED IN APPLICATION.YML FILE]
// URL: SERVICE URL TO CONSUME
// @FeignClient(name = "currency-exchange-service", url = "localhost:8000") // SPRING CLOUD LOADBALANCER
@FeignClient(name = "currency-exchange-service")
public interface CurrencyExchangeServiceProxy {
    @GetMapping("/currency-exchange/from/{from}/to/{to}") // GET REQUEST DIRECTLY
    CurrencyConversion retrieveExchangeValue(@PathVariable("from") String from, @PathVariable("to") String to);
}
