package com.deftdevs.bootstrapi.commons.service.api;

import com.deftdevs.bootstrapi.commons.model.MailServerPopModel;
import com.deftdevs.bootstrapi.commons.model.MailServerSmtpModel;

import javax.validation.constraints.NotNull;

public interface MailServerService {

    /**
     * Get the smtp mailserver settings.
     *
     * @return the smtp mailserver settings
     */
    MailServerSmtpModel getMailServerSmtp();

    /**
     * Sets the smtp mailserver settings.
     *
     * @param smtpModel the smtp mailserver settings to set
     * @return the smtp mailserver settings
     */
    MailServerSmtpModel setMailServerSmtp(
            @NotNull final MailServerSmtpModel smtpModel);

    /**
     * Get the pop mailserver settings.
     *
     * @return the pop mailserver settings
     */
    MailServerPopModel getMailServerPop();

    /**
     * Sets the pop mailserver settings.
     *
     * @param popModel the pop mailserver settings to set
     * @return the pop mailserver settings
     */
    MailServerPopModel setMailServerPop(
            @NotNull final MailServerPopModel popModel);
}
