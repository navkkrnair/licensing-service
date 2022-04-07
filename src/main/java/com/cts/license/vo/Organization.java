package com.cts.license.vo;

import lombok.Data;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("organization")
@Data
public class Organization {
    private Long id;
    private String name;
    private String contactName;
    private String contactEmail;
    private String contactPhone;
}
