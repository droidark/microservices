package xyz.krakenkat.limitsservice.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;


// @Configuration MUST BE ADDED IF YOU'RE NOT USING
// @EnableConfigurationProperties IN MAIN CLASS
@ConfigurationProperties("limits-service")
@Getter
@Setter
public class AppConfiguration {
    private int minimum;
    private int maximum;
}
