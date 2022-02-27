package com.cts.license.controller;

import com.cts.license.exceptions.NoEntityFoundException;
import com.cts.license.model.License;
import com.cts.license.service.LicenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("v1/organization/license")
public class LicenseController {

    private final LicenseService licenseService;

    @GetMapping("/{licenseId}/{organizationId}")
    public ResponseEntity<License> getLicense(@PathVariable Long licenseId, @PathVariable String organizationId) {
        License license = licenseService.getLicense(licenseId, organizationId);
        if (license == null) {
            throw new NoEntityFoundException("No License found");
        }
        return ResponseEntity.ok(license);
    }

    @PutMapping("/{licenseId}/{organizationId}")
    public ResponseEntity<String> updateLicense(@PathVariable Long licenseId, @PathVariable String organizationId) {
        return ResponseEntity.ok(licenseService.updateLicense(licenseId, organizationId));
    }

    @PostMapping
    public ResponseEntity<String> createLicense(@RequestBody License license) {
        return ResponseEntity.ok(licenseService.createLicense(license));
    }

    @DeleteMapping("/{licenseId}")
    public ResponseEntity<String> deleteLicense(@PathVariable Long licenseId) {
        return ResponseEntity.ok(licenseService.deleteLicense(licenseId));
    }
}
