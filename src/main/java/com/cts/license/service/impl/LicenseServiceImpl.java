package com.cts.license.service.impl;

import com.cts.license.config.LicensingServiceProperties;
import com.cts.license.model.License;
import com.cts.license.repository.LicenseRepository;
import com.cts.license.repository.OrganizationRedisRepository;
import com.cts.license.service.LicenseService;
import com.cts.license.service.client.OrganizationDiscoveryClient;
import com.cts.license.service.client.OrganizationFeignClient;
import com.cts.license.service.client.OrganizationRestTemplateClient;
import com.cts.license.vo.Organization;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeoutException;

@Slf4j
@RequiredArgsConstructor
@Service
public class LicenseServiceImpl implements LicenseService {

    private final LicenseRepository repository;
    private final MessageSource messages;
    private final LicensingServiceProperties properties;
    private final OrganizationDiscoveryClient organizationDiscoveryClient;
    private final OrganizationRestTemplateClient organizationRestTemplateClient;
    private final OrganizationFeignClient organizationFeignClient;
    private final OrganizationRedisRepository organizationRedisRepository;


    @Override
    public License getLicense(Long licenseId, Long organizationId, String clientType) {
        License license = repository.findByIdAndOrganizationId(licenseId, organizationId);
        if (license == null) {
            return null;
        }
        Organization organization = retrieveOrganizationInfo(organizationId, clientType);
        if (null != organization) {
            license.setOrganizationName(organization.getName());
            license.setContactName(organization.getContactName());
            license.setContactEmail(organization.getContactEmail());
            license.setContactPhone(organization.getContactPhone());
        }
        return license;
    }

    private Organization retrieveOrganizationInfo(Long organizationId, String clientType) {
        log.info("Checking Cache for Organization with id {}", organizationId);
        Organization organization = checkRedisCache(organizationId);
        if (organization == null) {
            log.info("Redis Cache doesn't have Organization, Calling organization-service");
            switch (clientType) {
                case "discovery":
                    log.info("Retrieving Organization using the discovery client");
                    organization = organizationDiscoveryClient.getOrganization(organizationId);
                    break;
                case "rest":
                    log.info("Retrieving Organization using the rest client");
                    organization = organizationRestTemplateClient.getOrganization(organizationId);
                    break;
                case "feign":
                    log.info("Retrieving Organization using feign client");
                    organization = organizationFeignClient.getOrganization(organizationId);
                    break;
            }
            log.info("Saving Organization with id {} to cache", organization.getId());
            cacheOrganization(organization);
        }

        return organization;
    }

    private void cacheOrganization(Organization organization) {
        organizationRedisRepository.save(organization);
    }

    private Organization checkRedisCache(Long organizationId) {
        return organizationRedisRepository.findById(organizationId)
                                          .orElse(null);
    }

    @CircuitBreaker(name = "licenseService", fallbackMethod = "fallBackLicense")
    @Retry(name = "retryLicenseService", fallbackMethod = "fallBackLicense")
    @Bulkhead(name = "bulkheadLicenseService", fallbackMethod = "fallBackLicense")
    @Override
    public License getLicense(Long licenseId, Long organizationId) throws TimeoutException {
        longRunningOperation();
        License license = repository.findByIdAndOrganizationId(licenseId, organizationId);
        license.setComment(properties.getProperty());
        return license;
    }

    private License fallBackLicense(Long licenseId, Long organizationId, Throwable throwable) {
        License license = License.builder()
                                 .id(licenseId)
                                 .description("fallback license")
                                 .organizationId(organizationId)
                                 .organizationName("na")
                                 .licenseType("fake")
                                 .comment("No info available")
                                 .build();
        return license;
    }

    private void longRunningOperation() throws TimeoutException {
        Random random = new Random();
        int randomNum = random.nextInt((3 - 1) + 1) + 1;
        if (randomNum == 3) {
            sleep();
        }
    }

    private void sleep() throws TimeoutException {
        try {
            Thread.sleep(1000);
            throw new TimeoutException();
        } catch (InterruptedException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public String createLicense(License license, Locale locale) {
        String response = null;
        License savedLicense = repository.save(license);
        response = String.format(messages.getMessage("license.create.message", null, locale), savedLicense.getId());
        return response;
    }

    @Override
    public String updateLicense(Long licenseId, Long organizationId) {
        Optional<License> optionalLicense = repository.findById(licenseId);
        return optionalLicense.map(license -> {
                                  license.setOrganizationId(organizationId);
                                  repository.save(license);
                                  return String.format(messages.getMessage("license.update.message", null, null),
                                          license.getId());
                              })
                              .orElse(String.format(messages.getMessage("license.update.failed.message", null, null),
                                      licenseId));
    }

    @Override
    public String deleteLicense(Long licenseId) {
        repository.deleteById(licenseId);
        String response = String.format("License with id: %s deleted", licenseId);
        return response;
    }

}
