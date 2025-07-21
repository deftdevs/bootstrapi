package com.deftdevs.bootstrapi.commons.model;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = BootstrAPI.PERMISSIONS + "-" + BootstrAPI.PERMISSIONS_GLOBAL)
public class PermissionsGlobalModel {

    @XmlElement
    private Map<String, ? extends Collection<String>> groupPermissions;

    @XmlElement
    private List<String> anonymousPermissions;

}
