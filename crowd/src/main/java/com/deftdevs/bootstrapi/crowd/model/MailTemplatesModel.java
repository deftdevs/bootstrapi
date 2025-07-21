package com.deftdevs.bootstrapi.crowd.model;

import com.deftdevs.bootstrapi.crowd.rest.api.MailTemplateResource;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

    public static final MailTemplatesModel EXAMPLE_1 = MailTemplatesModel.builder()
        .forgottenPassword("Example1ForgottenPassword")
        .forgottenUsername("Example1ForgottenUsername")
        .passwordExpirationReminder("Example1PasswordExpirationReminder")
        .emailChangeValidation("Example1ValidationMessage")
        .emailChangeInfo("Example1EmailChangeMessage")
        .build();

    public static final MailTemplatesModel EXAMPLE_2 = MailTemplatesModel.builder()
        .forgottenPassword("Example2ForgottenPassword")
        .forgottenUsername("Example2ForgottenUsername")
        .passwordExpirationReminder("Example2PasswordExpirationReminder")
        .emailChangeValidation("Example2ValidationMessage")
        .emailChangeInfo("Example2EmailChangeMessage")
        .build();

}
