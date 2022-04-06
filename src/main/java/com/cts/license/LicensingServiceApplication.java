package com.cts.license;

import com.cts.license.event.OrganizationChangeModel;
import com.cts.license.model.License;
import com.cts.license.repository.LicenseRepository;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.adapters.KeycloakConfigResolver;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.context.annotation.Bean;


@Slf4j
@RefreshScope
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableBinding(Sink.class)
public class LicensingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(LicensingServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner init(LicenseRepository repository) {
        return args -> {
            License license = License.builder()
                                     .description("Software product")
                                     .organizationId(1L)
                                     .productName("Ostock")
                                     .licenseType("full")
                                     .build();
            repository.save(license);
            repository.findAll()
                      .forEach(license1 -> log.info("{}", license1));
        };
    }

    @Bean
    public KeycloakConfigResolver keycloakConfigResolver() {
        return new KeycloakSpringBootConfigResolver();
    }

    @StreamListener(Sink.INPUT)
    public void loggerSink(OrganizationChangeModel model) {
        log.info("Received a {} event for Organization id {}", model.getAction(), model.getOrganizationId());
    }
}
