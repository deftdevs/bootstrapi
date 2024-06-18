package de.aservo.confapi.crowd.service.api;

import de.aservo.confapi.crowd.model.SessionConfigBean;

public interface SessionConfigService {

    SessionConfigBean getSessionConfig();

    SessionConfigBean setSessionConfig(
            SessionConfigBean sessionConfigBean);

}
