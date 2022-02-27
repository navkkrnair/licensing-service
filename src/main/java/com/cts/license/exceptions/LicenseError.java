package com.cts.license.exceptions;

import lombok.Builder;
import lombok.Getter;

@Builder
public class LicenseError {
    @Getter
    private String message;
}
