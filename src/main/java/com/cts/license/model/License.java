package com.cts.license.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class License extends RepresentationModel<License> {
    @Id

    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String description;
    private Long organizationId;
    private String productName;
    private String licenseType;
    private String comment;

    @Transient
    private String organizationName;
    @Transient
    private String contactName;
    @Transient
    private String contactPhone;
    @Transient
    private String contactEmail;

}
