package com.deftdevs.bootstrapi.jira.model;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.model._AbstractAllModel;
import com.deftdevs.bootstrapi.commons.model.AuthenticationModel;
import com.deftdevs.bootstrapi.commons.model.PermissionsGlobalModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@XmlRootElement(name = BootstrAPI._ALL)
public class _AllModel extends _AbstractAllModel<SettingsModel> {

    @XmlElement
    private AuthenticationModel authentication;

    @XmlElement
    private PermissionsGlobalModel permissionsGlobal;

}
