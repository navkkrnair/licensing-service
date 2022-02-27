package com.cts.license.service.impl;

import com.cts.license.model.License;
import com.cts.license.repository.LicenseRepository;
import com.cts.license.service.LicenseService;
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

    @Override
    public License getLicense(Long licenseId, String organizationId) {
        return repository.findByIdAndOrganizationId(licenseId, organizationId);
    }

    @Override
    public String createLicense(License license, Locale locale) {
        String response = null;
        License savedLicense = repository.save(license);
        response = String.format(messages.getMessage("license.create.message", null,locale), savedLicense.getId());
        return response;
    }

    @Override
    public String updateLicense(Long licenseId, String organizationId) {
        Optional<License> optionalLicense = repository.findById(licenseId);
        return optionalLicense.map(license -> {
                                  license.setOrganizationId(organizationId);
                                  repository.save(license);
                                  return String.format("License with id: %s updated", license.getId());
                              })
                              .orElse(String.format("Failed to update License with id: %s", licenseId));
    }

    @Override
    public String deleteLicense(Long licenseId) {
        repository.deleteById(licenseId);
        String response = String.format("License with id: %s deleted", licenseId);
        return response;
    }
}
