package com.deftdevs.bootstrapi.crowd.service.api;

import com.deftdevs.bootstrapi.crowd.model.SessionConfigModel;

public interface SessionConfigService {

    SessionConfigModel getSessionConfig();

    SessionConfigModel setSessionConfig(
            SessionConfigModel sessionConfigModel);

}
