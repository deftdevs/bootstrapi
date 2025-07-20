package com.deftdevs.bootstrapi.confluence.model;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Model for anonymous access infos in REST requests.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = BootstrAPI.PERMISSION_ANONYMOUS_ACCESS)
public class PermissionAnonymousAccessModel {

    @XmlElement
    private Boolean allowForPages;

    @XmlElement
    private Boolean allowForUserProfiles;

    public static final PermissionAnonymousAccessModel EXAMPLE_1 = PermissionAnonymousAccessModel.builder()
        .allowForPages(true)
        .allowForUserProfiles(true)
        .build();

}
