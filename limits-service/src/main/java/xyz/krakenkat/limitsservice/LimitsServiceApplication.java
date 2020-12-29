package xyz.krakenkat.limitsservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import xyz.krakenkat.limitsservice.configuration.AppConfiguration;

@SpringBootApplication
@EnableConfigurationProperties(AppConfiguration.class)
public class LimitsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(LimitsServiceApplication.class, args);
    }

}
