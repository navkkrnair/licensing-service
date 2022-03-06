package com.cts.license;

import com.cts.license.model.License;
import com.cts.license.repository.LicenseRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;


@Slf4j
@RefreshScope
@SpringBootApplication
@EnableDiscoveryClient
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

}
