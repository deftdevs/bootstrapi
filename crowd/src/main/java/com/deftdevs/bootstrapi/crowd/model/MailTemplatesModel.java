package com.deftdevs.bootstrapi.crowd.model;

import com.deftdevs.bootstrapi.crowd.rest.api.MailTemplateResource;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@NoArgsConstructor
@XmlRootElement(name = MailTemplateResource.MAIL_TEMPLATES)
public class MailTemplatesModel {

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

    public static final MailTemplatesModel EXAMPLE_1;
    public static final MailTemplatesModel EXAMPLE_2;

    static {
        EXAMPLE_1 = new MailTemplatesModel();
        EXAMPLE_1.setForgottenPassword("Example1ForgottenPassword");
        EXAMPLE_1.setForgottenUsername("Example1ForgottenUsername");
        EXAMPLE_1.setPasswordExpirationReminder("Example1PasswordExpirationReminder");
        EXAMPLE_1.setEmailChangeValidation("Example1ValidationMessage");
        EXAMPLE_1.setEmailChangeInfo("Example1EmailChangeMessage");
    }

    static {
        EXAMPLE_2 = new MailTemplatesModel();
        EXAMPLE_2.setForgottenPassword("Example2ForgottenPassword");
        EXAMPLE_2.setForgottenUsername("Example2ForgottenUsername");
        EXAMPLE_2.setPasswordExpirationReminder("Example2PasswordExpirationReminder");
        EXAMPLE_2.setEmailChangeValidation("Example2ValidationMessage");
        EXAMPLE_2.setEmailChangeInfo("Example2EmailChangeMessage");
    }
}
