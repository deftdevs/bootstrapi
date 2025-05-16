package com.deftdevs.bootstrapi.crowd.service.api;

import com.deftdevs.bootstrapi.crowd.model.MailTemplatesModel;

public interface MailTemplatesService {

    MailTemplatesModel getMailTemplates();

    MailTemplatesModel setMailTemplates(
            MailTemplatesModel mailTemplatesModel);

}
