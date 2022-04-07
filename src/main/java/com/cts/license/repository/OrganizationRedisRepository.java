package com.cts.license.repository;

import com.cts.license.vo.Organization;
import org.springframework.data.repository.CrudRepository;

public interface OrganizationRedisRepository extends CrudRepository<Organization, Long> {
}
