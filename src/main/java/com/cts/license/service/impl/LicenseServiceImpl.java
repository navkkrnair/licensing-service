package com.cts.license.service.impl;

import com.cts.license.config.LicensingServiceProperties;
import com.cts.license.model.License;
import com.cts.license.repository.LicenseRepository;
import com.cts.license.service.LicenseService;
import com.cts.license.service.client.OrganizationDiscoveryClient;
import com.cts.license.vo.Organization;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class LicenseServiceImpl implements LicenseService {

    private final LicenseRepository repository;
    private final MessageSource messages;
    private final LicensingServiceProperties properties;
    private final OrganizationDiscoveryClient organizationDiscoveryClient;


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
        Organization organization = null;
        switch (clientType) {
            case "discovery":
                System.out.println("I am using the discovery client");
                organization = organizationDiscoveryClient.getOrganization(organizationId);
                break;
        }
        return organization;
    }

    @Override
    public License getLicense(Long licenseId, Long organizationId) {
        License license = repository.findByIdAndOrganizationId(licenseId, organizationId);
        license.setComment(properties.getProperty());
        return license;
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
