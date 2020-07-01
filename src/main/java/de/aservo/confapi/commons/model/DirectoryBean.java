package de.aservo.confapi.commons.model;

import de.aservo.confapi.commons.constants.ConfAPI;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Bean for user directory settings in REST requests.
 */
@Data
@NoArgsConstructor
@XmlRootElement(name = ConfAPI.DIRECTORY)
public class DirectoryBean {

    @XmlElement
    private boolean active;

    @XmlElement
    @NotNull
    @Size(min = 1)
    private String name;

    @XmlElement
    @NotNull
    @Size(min = 1)
    private String clientName;

    @XmlElement
    @NotNull
    private String type;

    @XmlElement
    private String description;

    @XmlElement
    @NotNull
    @Size(min = 1)
    private String crowdUrl;

    @XmlElement
    @NotNull
    @Size(min = 1)
    private String appPassword;

    @XmlElement
    @NotNull
    @Size(min = 1)
    private String implClass;

    @XmlElement
    private String proxyHost;

    @XmlElement
    private String proxyPort;

    @XmlElement
    private String proxyUsername;

    @XmlElement
    private String proxyPassword;

    // Example instances for documentation and tests

    public static final DirectoryBean EXAMPLE_1;

    static {
        EXAMPLE_1 = new DirectoryBean();
        EXAMPLE_1.setName("example");
        EXAMPLE_1.setCrowdUrl("https://crowd.example.com");
        EXAMPLE_1.setAppPassword("p455w0rd");
        EXAMPLE_1.setType("crowd");
        EXAMPLE_1.setClientName("confluence-client");
        EXAMPLE_1.setImplClass("test.class");
    }

    public static final DirectoryBean EXAMPLE_2;

    static {
        EXAMPLE_2 = new DirectoryBean();
        EXAMPLE_2.setName("example");
        EXAMPLE_2.setCrowdUrl("https://localhost/crowd");
        EXAMPLE_2.setAppPassword("0th3r");
        EXAMPLE_2.setType("crowd");
        EXAMPLE_2.setClientName("confluence-client2");
        EXAMPLE_2.setImplClass("test2.class");
    }

    public static final DirectoryBean EXAMPLE_3;

    static {
        EXAMPLE_3 = new DirectoryBean();
        EXAMPLE_3.setName("example other");
        EXAMPLE_3.setCrowdUrl("https://crowd.other.example.com");
        EXAMPLE_3.setAppPassword("p455w0rd");
        EXAMPLE_3.setType("other");
        EXAMPLE_3.setClientName("confluence-client3");
        EXAMPLE_3.setImplClass("test3.class");
    }

}
