package com.deftdevs.bootstrapi.crowd.service.api;

import com.deftdevs.bootstrapi.crowd.model.SessionConfigBean;

public interface SessionConfigService {

    SessionConfigBean getSessionConfig();

    SessionConfigBean setSessionConfig(
            SessionConfigBean sessionConfigBean);

}
