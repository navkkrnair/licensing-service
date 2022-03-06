package com.cts.license.service.client;

import com.cts.license.vo.Organization;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Component
public class OrganizationRestTemplateClient {
    private final RestTemplate restTemplate;

    public Organization getOrganization(Long organizationId) {
        ResponseEntity<Organization> response = restTemplate.exchange("http://organization-service/v1/organization" +
                        "/{organizationId}",
                HttpMethod.GET, null, Organization.class, organizationId);
        return response.getBody();
    }
}
