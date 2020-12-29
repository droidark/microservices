package xyz.krakenkat.limitsservice.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.krakenkat.limitsservice.configuration.AppConfiguration;
import xyz.krakenkat.limitsservice.domain.model.Limit;

@RestController
@AllArgsConstructor
public class LimitController {

    private AppConfiguration appConfiguration;

    @GetMapping("/limits")
    public Limit retrieveLimitsFromConfiguration() {
        return new Limit(appConfiguration.getMinimum(), appConfiguration.getMaximum());
    }
}
