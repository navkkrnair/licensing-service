package com.cts.license.service.client;

import com.cts.license.vo.Organization;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("organization-service")
public interface OrganizationFeignClient {
    @GetMapping("v1/organization/{organizationId}")
    Organization getOrganization(@PathVariable Long organizationId);
}
