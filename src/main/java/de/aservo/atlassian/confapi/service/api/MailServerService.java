package de.aservo.atlassian.confapi.service.api;

import de.aservo.atlassian.confapi.model.MailServerPopBean;
import de.aservo.atlassian.confapi.model.MailServerSmtpBean;

import javax.validation.constraints.NotNull;

public interface MailServerService {

    /**
     * Get the smtp mailserver settings.
     *
     * @return the smtp mailserver settings
     */
    public MailServerSmtpBean getMailServerSmtp();

    /**
     * Sets the smtp mailserver settings.
     *
     * @param smtpBean the smtp mailserver settings to set
     * @return the smtp mailserver settings
     */
    public MailServerSmtpBean setMailServerSmtp(
            @NotNull final MailServerSmtpBean smtpBean);

    /**
     * Get the pop mailserver settings.
     *
     * @return the pop mailserver settings
     */
    public MailServerPopBean getMailServerPop();

    /**
     * Sets the pop mailserver settings.
     *
     * @param popBean the pop mailserver settings to set
     * @return the pop mailserver settings
     */
    public MailServerPopBean setMailServerPop(
            @NotNull final MailServerPopBean popBean);
}
