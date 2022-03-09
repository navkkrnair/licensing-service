package com.cts.license.service;

import com.cts.license.model.License;

import java.util.Locale;
import java.util.concurrent.TimeoutException;

public interface LicenseService {
    License getLicense(Long licenseId, Long organizationId) throws TimeoutException;

    String createLicense(License license, Locale locale);

    String updateLicense(Long licenseId, Long organizationId);

    String deleteLicense(Long licenseId);

    License getLicense(Long licenseId, Long organizationId, String clientType);
}
