package com.deftdevs.bootstrapi.crowd.model;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.model._AbstractAllModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.Map;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@XmlRootElement(name = BootstrAPI._ALL)
public class _AllModel extends _AbstractAllModel<SettingsModel> {

    @XmlElement
    private Map<String, ApplicationModel> applications;

    @XmlElement
    private MailTemplatesModel mailTemplates;

    @XmlElement
    private SessionConfigModel sessionConfig;

    @XmlElement
    private List<String> trustedProxies;

}
