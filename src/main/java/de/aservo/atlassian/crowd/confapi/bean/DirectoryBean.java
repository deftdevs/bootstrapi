package de.aservo.atlassian.crowd.confapi.bean;

import com.atlassian.crowd.embedded.api.Directory;
import de.aservo.confapi.commons.constants.ConfAPI;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

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
    private DirectoryAttributesBean attributes;

    public static DirectoryBean from(
            final Directory directory) {

        final DirectoryBean directoryBean = new DirectoryBean();
        directoryBean.setId(directory.getId());
        directoryBean.setName(directory.getName());
        directoryBean.setAttributes(DirectoryAttributesBean.from(directory));
        return directoryBean;
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

}
