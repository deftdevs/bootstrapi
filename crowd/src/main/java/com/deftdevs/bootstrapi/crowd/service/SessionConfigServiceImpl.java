package de.aservo.confapi.crowd.service;

import com.atlassian.crowd.manager.property.PropertyManager;
import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import de.aservo.confapi.crowd.model.SessionConfigBean;
import de.aservo.confapi.crowd.service.api.SessionConfigService;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
@ExportAsService(SessionConfigService.class)
public class SessionConfigServiceImpl implements SessionConfigService {

    private final PropertyManager propertyManager;

    @Inject
    public SessionConfigServiceImpl(
            final PropertyManager propertyManager) {

        this.propertyManager = propertyManager;
    }

    @Override
    public SessionConfigBean getSessionConfig() {
        final SessionConfigBean sessionConfigBean = new SessionConfigBean();
        sessionConfigBean.setSessionTimeoutInMinutes(propertyManager.getSessionTime());
        sessionConfigBean.setRequireConsistentClientIP(propertyManager.isIncludeIpAddressInValidationFactors());

        return sessionConfigBean;
    }

    @Override
    public SessionConfigBean setSessionConfig(
            final SessionConfigBean sessionConfigBean) {

        if (sessionConfigBean.getSessionTimeoutInMinutes() != null) {
            propertyManager.setSessionTime(sessionConfigBean.getSessionTimeoutInMinutes());
        }

        if (sessionConfigBean.getRequireConsistentClientIP() != null) {
            propertyManager.setIncludeIpAddressInValidationFactors(sessionConfigBean.getRequireConsistentClientIP());
        }

        return getSessionConfig();
    }
}
