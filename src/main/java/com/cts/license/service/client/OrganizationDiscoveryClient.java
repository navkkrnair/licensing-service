package com.cts.license.service.client;

import com.cts.license.vo.Organization;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RequiredArgsConstructor
@Component
public class OrganizationDiscoveryClient {
    private final DiscoveryClient discoveryClient;

    public Organization getOrganization(Long organizationId){
        RestTemplate restTemplate = new RestTemplate();
        List<ServiceInstance> instances = discoveryClient.getInstances("organization-service");
        String serviceUri = String.format("%s/v1/organization/%s", instances.get(0)
                                                                        .getUri()
                                                                        .toString(), organizationId);
        ResponseEntity<Organization> response = restTemplate.exchange(serviceUri, HttpMethod.GET, null, Organization.class, organizationId);
        return response.getBody();
    }
}
