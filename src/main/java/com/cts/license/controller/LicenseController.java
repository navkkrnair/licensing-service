package com.cts.license.controller;

import com.cts.license.exceptions.NoEntityFoundException;
import com.cts.license.model.License;
import com.cts.license.service.LicenseService;
import com.cts.license.utils.UserContext;
import com.cts.license.utils.UserContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;
import java.util.concurrent.TimeoutException;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("v1/license")
public class LicenseController {

    private final LicenseService licenseService;

    @GetMapping("/{licenseId}/{organizationId}/{clientType}")
    public ResponseEntity<License> getLicenseWithClient(@PathVariable Long licenseId,
                                                        @PathVariable Long organizationId,
                                                        @PathVariable String clientType) {
        License license = licenseService.getLicense(licenseId, organizationId, clientType);
        if (license == null) {
            throw new NoEntityFoundException("No License found");
        }

        return ResponseEntity.ok(license);
    }

    @GetMapping("/{licenseId}/{organizationId}")
    public ResponseEntity<License> getLicense(@PathVariable Long licenseId, @PathVariable Long organizationId) throws TimeoutException {
        String correlationId = UserContextHolder.getContext()
                                                .getCorrelationId();
        log.debug("Value from header for {}: {}", UserContext.CORRELATION_ID, correlationId);
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
    public ResponseEntity<String> updateLicense(@PathVariable Long licenseId, @PathVariable Long organizationId) {
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
