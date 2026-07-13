package com.deftdevs.bootstrapi.commons.service.api;

import com.deftdevs.bootstrapi.commons.model.MailServerModel;
import com.deftdevs.bootstrapi.commons.model.MailServerPopModel;
import com.deftdevs.bootstrapi.commons.model.MailServerSmtpModel;
import com.deftdevs.bootstrapi.commons.model.type.ServiceResult;
import com.deftdevs.bootstrapi.commons.model.type._AllModelStatus;
import com.deftdevs.bootstrapi.commons.util.ServiceResultUtil;

import java.util.LinkedHashMap;
import java.util.Map;

public interface MailServerService {

    default MailServerModel getMailServer() {
        return new MailServerModel(getMailServerSmtp(), getMailServerPop());
    }

    default ServiceResult<MailServerModel> setMailServer(final MailServerModel mailServerModel) {
        final MailServerModel result = new MailServerModel();
        final Map<String, _AllModelStatus> status = new LinkedHashMap<>();

        ServiceResultUtil.setSubEntity(status, MailServerSmtpModel.class, mailServerModel.getSmtp(),
                this::setMailServerSmtp, result::setSmtp);
        ServiceResultUtil.setSubEntity(status, MailServerPopModel.class, mailServerModel.getPop(),
                this::setMailServerPop, result::setPop);

        return new ServiceResult<>(result, status);
    }

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
            final MailServerSmtpModel smtpModel);

    /**
     * Removes the default smtp mailserver if one is configured; does nothing otherwise.
     */
    void deleteMailServerSmtp();

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
            final MailServerPopModel popModel);

    /**
     * Removes the default pop mailserver if one is configured; does nothing otherwise.
     */
    void deleteMailServerPop();
}
