package com.cts.license.repository;

import com.cts.license.model.License;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LicenseRepository extends JpaRepository<License, Long> {
    License findByIdAndOrganizationId(Long licenseId, String organizationId);
}
