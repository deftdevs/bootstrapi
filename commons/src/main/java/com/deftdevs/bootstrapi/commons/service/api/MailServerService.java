package com.deftdevs.bootstrapi.commons.service.api;

import com.deftdevs.bootstrapi.commons.model.MailServerPopBean;
import com.deftdevs.bootstrapi.commons.model.MailServerSmtpBean;

import javax.validation.constraints.NotNull;

public interface MailServerService {

    /**
     * Get the smtp mailserver settings.
     *
     * @return the smtp mailserver settings
     */
    MailServerSmtpBean getMailServerSmtp();

    /**
     * Sets the smtp mailserver settings.
     *
     * @param smtpBean the smtp mailserver settings to set
     * @return the smtp mailserver settings
     */
    MailServerSmtpBean setMailServerSmtp(
            @NotNull final MailServerSmtpBean smtpBean);

    /**
     * Get the pop mailserver settings.
     *
     * @return the pop mailserver settings
     */
    MailServerPopBean getMailServerPop();

    /**
     * Sets the pop mailserver settings.
     *
     * @param popBean the pop mailserver settings to set
     * @return the pop mailserver settings
     */
    MailServerPopBean setMailServerPop(
            @NotNull final MailServerPopBean popBean);
}
