package com.cts.license.controller;

import com.cts.license.exceptions.NoEntityFoundException;
import com.cts.license.model.License;
import com.cts.license.service.LicenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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
        license.add(linkTo(methodOn(LicenseController.class).getLicense(licenseId, organizationId)).withSelfRel(),
                linkTo(methodOn(LicenseController.class).createLicense(license, null)).withRel("createLicense"),
                linkTo(methodOn(LicenseController.class).updateLicense(licenseId, organizationId)).withRel(
                        "updateLicense"),
                linkTo(methodOn(LicenseController.class).deleteLicense(licenseId))
                        .withRel("deleteLicense"));
        return ResponseEntity.ok(license);
    }

    @PutMapping("/{licenseId}/{organizationId}")
    public ResponseEntity<String> updateLicense(@PathVariable Long licenseId, @PathVariable String organizationId) {
        return ResponseEntity.ok(licenseService.updateLicense(licenseId, organizationId));
    }

    @PostMapping
    public ResponseEntity<String> createLicense(@RequestBody License license,
                                                @RequestHeader(value = "Accept-Language", required = false) Locale locale) {
        return ResponseEntity.ok(licenseService.createLicense(license, locale));
    }

    @DeleteMapping("/{licenseId}")
    public ResponseEntity<String> deleteLicense(@PathVariable Long licenseId) {
        return ResponseEntity.ok(licenseService.deleteLicense(licenseId));
    }
}
