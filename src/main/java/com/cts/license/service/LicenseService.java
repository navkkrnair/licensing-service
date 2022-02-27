package com.cts.license.service;

import com.cts.license.model.License;

public interface LicenseService {
    License getLicense(Long licenseId, String organizationId);

    String createLicense(License license);

    String updateLicense(Long licenseId, String organizationId);

    String deleteLicense(Long licenseId);
}
