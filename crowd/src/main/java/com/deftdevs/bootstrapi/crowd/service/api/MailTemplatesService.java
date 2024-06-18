package com.deftdevs.bootstrapi.crowd.service.api;

import com.deftdevs.bootstrapi.crowd.model.MailTemplatesBean;

public interface MailTemplatesService {

    MailTemplatesBean getMailTemplates();

    MailTemplatesBean setMailTemplates(
            MailTemplatesBean mailTemplatesBean);

}
