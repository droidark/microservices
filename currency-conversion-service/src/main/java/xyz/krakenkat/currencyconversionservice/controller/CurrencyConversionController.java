package xyz.krakenkat.currencyconversionservice.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import xyz.krakenkat.currencyconversionservice.domain.model.CurrencyConversion;
import xyz.krakenkat.currencyconversionservice.service.CurrencyExchangeServiceProxy;

import java.math.BigDecimal;
import java.util.Map;

@AllArgsConstructor
@RestController
public class CurrencyConversionController {

    private CurrencyExchangeServiceProxy currencyExchangeServiceProxy;

    // USING RestTemplate
    @GetMapping(path = "/currency-converter/rest-template/from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConversion convertCurrencyRestTemplate(
            @PathVariable String from,
            @PathVariable String to,
            @PathVariable BigDecimal quantity) {
        Map<String, String> uriVariables = Map.of("from", from, "to", to);
        // USING RestTemplate
        ResponseEntity<CurrencyConversion> currencyConversionResponseEntity =
                new RestTemplate().getForEntity("http://localhost:8000/currency-exchange/from/{from}/to/{to}",
                        CurrencyConversion.class, uriVariables);
        CurrencyConversion response = currencyConversionResponseEntity.getBody();
        return new CurrencyConversion(response.getId(), from, to, response.getConversionMultiple(), quantity,
                quantity.multiply(response.getConversionMultiple()), response.getPort());
    }

    // USING WebClient
    @GetMapping(path = "/currency-converter/web-client/from/{from}/to/{to}/quantity/{quantity}")
    public Mono<CurrencyConversion> convertCurrencyWebClient(
            @PathVariable String from,
            @PathVariable String to,
            @PathVariable BigDecimal quantity) {
        Map<String, String> uriVariables = Map.of("from", from, "to", to);
        WebClient client = WebClient
                .builder()
                .baseUrl("http://localhost:8000/currency-exchange/from/{from}/to/{to}")
                .defaultUriVariables(uriVariables)
                .build();
        return client.get().retrieve().bodyToMono(CurrencyConversion.class);
    }

    // USING FEING
    @GetMapping(path = "/currency-converter/feing/from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConversion convertCurrencyFeing(
            @PathVariable String to,
            @PathVariable String from,
            @PathVariable BigDecimal quantity) {
        Map<String, String> uriVariables = Map.of("from", from, "to", to);
        CurrencyConversion response = currencyExchangeServiceProxy.retrieveExchangeValue(from, to);
        return new CurrencyConversion(response.getId(), from, to, response.getConversionMultiple(), quantity,
                quantity.multiply(response.getConversionMultiple()), response.getPort());
    }
}
