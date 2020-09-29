package de.aservo.confapi.crowd.model;

import de.aservo.confapi.commons.constants.ConfAPI;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@NoArgsConstructor
@XmlRootElement(name = ConfAPI.APPLICATION)
public class ApplicationBean {

    public enum ApplicationType {
        GENERIC,
        PLUGIN,
        CROWD,
        JIRA,
        CONFLUENCE,
        BITBUCKET,
        FISHEYE,
        CRUCIBLE,
        BAMBOO,
        ;
    }

    @XmlElement
    private Long id;

    @XmlElement
    private String name;

    @XmlElement
    private String description;

    @XmlElement
    private Boolean active;

    @XmlElement
    private ApplicationType type;

    @XmlElement
    private String password;

}
