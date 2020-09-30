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

    public static final ApplicationBean EXAMPLE_1;
    public static final ApplicationBean EXAMPLE_2;

    static {
        EXAMPLE_1 = new ApplicationBean();
        EXAMPLE_1.setName("app_name");
        EXAMPLE_1.setDescription("app_description");
        EXAMPLE_1.setActive(true);
        EXAMPLE_1.setPassword("3x4mpl3");
        EXAMPLE_1.setType(ApplicationType.GENERIC);
        EXAMPLE_1.setId(1L);
    }

    static {
        EXAMPLE_2 = new ApplicationBean();
        EXAMPLE_2.setName("app_name2");
        EXAMPLE_2.setDescription("app_description2");
        EXAMPLE_2.setActive(false);
        EXAMPLE_2.setPassword("3x4mpl32");
        EXAMPLE_2.setType(ApplicationType.BAMBOO);
        EXAMPLE_1.setId(2L);
    }
}
