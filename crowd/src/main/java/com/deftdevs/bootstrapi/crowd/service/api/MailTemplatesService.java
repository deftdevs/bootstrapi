package de.aservo.confapi.crowd.service.api;

import de.aservo.confapi.crowd.model.MailTemplatesBean;

public interface MailTemplatesService {

    MailTemplatesBean getMailTemplates();

    MailTemplatesBean setMailTemplates(
            MailTemplatesBean mailTemplatesBean);

}
