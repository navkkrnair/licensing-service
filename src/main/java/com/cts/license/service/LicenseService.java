package com.cts.license.service;

import com.cts.license.model.License;

import java.util.Locale;

public interface LicenseService {
    License getLicense(Long licenseId, String organizationId);

    String createLicense(License license, Locale locale);

    String updateLicense(Long licenseId, String organizationId);

    String deleteLicense(Long licenseId);
}
