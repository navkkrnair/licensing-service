package com.cts.license.service;

import com.cts.license.model.License;

public interface LicenseService {
    License getLicense(Long id, String organizationId);

    String createLicense(License license);

    String updateLicense(Long id, String organizationId);

    String deleteLicense(Long id);
}
