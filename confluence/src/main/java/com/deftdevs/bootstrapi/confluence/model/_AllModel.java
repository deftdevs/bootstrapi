package com.deftdevs.bootstrapi.confluence.model;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.model.AbstractDirectoryModel;
import com.deftdevs.bootstrapi.commons.model.ApplicationLinkModel;
import com.deftdevs.bootstrapi.commons.model.AuthenticationModel;
import com.deftdevs.bootstrapi.commons.model.MailServerModel;
import com.deftdevs.bootstrapi.commons.model.PermissionsGlobalModel;
import com.deftdevs.bootstrapi.commons.model.type._AllModelAccessor;
import com.deftdevs.bootstrapi.commons.model.type._AllModelStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = BootstrAPI._ALL)
public class _AllModel implements _AllModelAccessor {

    @XmlElement
    private SettingsModel settings;

    @XmlElement
    private Map<String, AbstractDirectoryModel> directories;

    @XmlElement
    private Map<String, ApplicationLinkModel> applicationLinks;

    @XmlElement
    private AuthenticationModel authentication;

    @XmlElement
    private List<String> licenses;

    @XmlElement
    private MailServerModel mailServer;

    @XmlElement
    private PermissionsGlobalModel permissionsGlobal;

    @XmlElement
    private Map<String, _AllModelStatus> status;

}
