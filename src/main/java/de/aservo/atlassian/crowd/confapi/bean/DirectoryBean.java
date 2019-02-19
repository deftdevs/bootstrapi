package de.aservo.atlassian.crowd.confapi.bean;

import com.atlassian.crowd.embedded.api.Directory;
import org.apache.commons.lang3.builder.EqualsBuilder;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Bean for directory in REST queries.
 */
@XmlRootElement(name = "directory")
public class DirectoryBean {

    @XmlElement
    private final Long id;

    @XmlElement
    private final String name;

    @XmlElement
    private final DirectoryAttributesBean attributes;

    /**
     * The default constructor is needed for JSON request deserialization.
     */
    public DirectoryBean() {
        this.id = null;
        this.name = null;
        this.attributes = null;
    }

    public DirectoryBean(
            final Directory directory) {

        this.id = directory.getId();
        this.name = directory.getName();
        this.attributes = new DirectoryAttributesBean(directory);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public DirectoryAttributesBean getAttributes() {
        return attributes;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) { return false; }
        if (obj == this) { return true; }
        if (obj.getClass() != getClass()) { return false; }

        final DirectoryBean directoryBean = (DirectoryBean) obj;
        return new EqualsBuilder()
                .append(id, directoryBean.id)
                .append(name, directoryBean.name)
                .isEquals();
    }

}
