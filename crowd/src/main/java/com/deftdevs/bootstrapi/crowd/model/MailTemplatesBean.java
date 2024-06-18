package de.aservo.confapi.crowd.model;

import de.aservo.confapi.crowd.rest.api.MailTemplateResource;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@NoArgsConstructor
@XmlRootElement(name = MailTemplateResource.MAIL_TEMPLATES)
public class MailTemplatesBean {

    @XmlElement
    private String forgottenPassword;

    @XmlElement
    private String forgottenUsername;

    @XmlElement
    private String passwordExpirationReminder;

    @XmlElement
    private String emailChangeValidation;

    @XmlElement
    private String emailChangeInfo;

    public static final MailTemplatesBean EXAMPLE_1;
    public static final MailTemplatesBean EXAMPLE_2;

    static {
        EXAMPLE_1 = new MailTemplatesBean();
        EXAMPLE_1.setForgottenPassword("Example1ForgottenPassword");
        EXAMPLE_1.setForgottenUsername("Example1ForgottenUsername");
        EXAMPLE_1.setPasswordExpirationReminder("Example1PasswordExpirationReminder");
        EXAMPLE_1.setEmailChangeValidation("Example1ValidationMessage");
        EXAMPLE_1.setEmailChangeInfo("Example1EmailChangeMessage");
    }

    static {
        EXAMPLE_2 = new MailTemplatesBean();
        EXAMPLE_2.setForgottenPassword("Example2ForgottenPassword");
        EXAMPLE_2.setForgottenUsername("Example2ForgottenUsername");
        EXAMPLE_2.setPasswordExpirationReminder("Example2PasswordExpirationReminder");
        EXAMPLE_2.setEmailChangeValidation("Example2ValidationMessage");
        EXAMPLE_2.setEmailChangeInfo("Example2EmailChangeMessage");
    }
}
