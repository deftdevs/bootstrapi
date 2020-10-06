package de.aservo.confapi.commons.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsExclude;
import org.apache.commons.lang3.builder.HashCodeExclude;

import javax.xml.bind.annotation.XmlElement;

@Data
@NoArgsConstructor
public abstract class AbstractMailServerProtocolBean {

    @XmlElement
    private String name;

    @XmlElement
    private String description;

    @XmlElement
    private String host;

    @XmlElement
    private Integer port;

    @XmlElement
    private String protocol;

    @XmlElement
    private Long timeout;

    @XmlElement
    private String username;

    @XmlElement
    @EqualsExclude
    @HashCodeExclude
    private String password;

    /**
     * Make sure not to set empty string as description.
     *
     * @param description the description
     */
    public void setDescription(
            final String description) {

        if (StringUtils.isNotBlank(description)) {
            this.description = description;
        }
    }

    /**
     * make sure port can be set from a String value.
     *
     * @param port the port
     */
    public void setPort(
            final String port) {

        if (StringUtils.isNotBlank(port)) {
            this.port = Integer.parseInt(port);
        }
    }

    /**
     * Make sure always to set protocol in lowercase format.
     *
     * @param protocol the protocol
     */
     public void setProtocol(
            final String protocol) {

        if (StringUtils.isNotBlank(protocol)) {
            this.protocol = protocol.toLowerCase();
        }
    }

    /**
     * Return set timeout or default timeout of 10000L if null.
     *
     * @return timeout
     */
    public Long getTimeout() {
        if (timeout == null) {
            return 10000L;
        }

        return timeout;
    }
}
