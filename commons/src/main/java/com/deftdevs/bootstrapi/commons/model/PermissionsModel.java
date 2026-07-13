package com.deftdevs.bootstrapi.commons.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import static com.deftdevs.bootstrapi.commons.constants.BootstrAPI.PERMISSIONS;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = PERMISSIONS)
public class PermissionsModel {

    @XmlElement
    private PermissionsGlobalModel global;

}
