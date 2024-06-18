package de.aservo.confapi.crowd.model;

import de.aservo.confapi.commons.model.SettingsBean;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "all")
public class AllBean {

    @XmlElement
    private SettingsBean settings;

    @XmlElement
    private ApplicationsBean applications;

}
