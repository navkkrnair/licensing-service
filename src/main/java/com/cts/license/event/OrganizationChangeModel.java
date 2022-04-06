package com.cts.license.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationChangeModel {

    private String type;
    private String action;
    private Long organizationId;
    private String correlationId;
}
