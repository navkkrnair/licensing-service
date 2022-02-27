package com.cts.license;

import com.cts.license.model.License;
import com.cts.license.repository.LicenseRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class LicensingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(LicensingServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner init(LicenseRepository repository) {
        return args -> {
            License license = License.builder()
                                     .description("Software product")
                                     .organizationId("cts")
                                     .productName("Ostock")
                                     .licenseType("full")
                                     .build();
            repository.save(license);
            repository.findAll()
                      .forEach(license1 -> System.out.println(license1));
        };
    }

}
