package de.aservo.confapi.crowd.model;

import de.aservo.confapi.commons.constants.ConfAPI;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@NoArgsConstructor
@XmlRootElement(name = ConfAPI.DIRECTORY)
public class DirectoryBean {

    @XmlElement
    private Long id;

    @XmlElement
    private String name;

    @XmlElement
    private String description;

    @XmlElement
    private Boolean active;

    @XmlElement
    private DirectoryConfigurationBean configuration;

    // examples

    public static final DirectoryBean EXAMPLE_1;

    static {
        EXAMPLE_1 = new DirectoryBean();
        EXAMPLE_1.setId(1L);
        EXAMPLE_1.setName("Example");
        EXAMPLE_1.setActive(true);
        EXAMPLE_1.setDescription("Example Directory");
        EXAMPLE_1.setConfiguration(DirectoryConfigurationBean.EXAMPLE_1);
    }

}
