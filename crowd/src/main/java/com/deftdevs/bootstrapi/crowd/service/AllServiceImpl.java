package com.deftdevs.bootstrapi.crowd.service;

import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import com.deftdevs.bootstrapi.commons.service.api.SettingsService;
import com.deftdevs.bootstrapi.crowd.model.AllBean;
import com.deftdevs.bootstrapi.crowd.service.api.AllService;
import com.deftdevs.bootstrapi.crowd.service.api.ApplicationsService;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
@ExportAsService(AllService.class)
public class AllServiceImpl implements AllService {

    private final ApplicationsService applicationsService;
    private final SettingsService settingsService;

    @Inject
    public AllServiceImpl(
            final ApplicationsService applicationsService,
            final SettingsService settingsService) {

        this.applicationsService = applicationsService;
        this.settingsService = settingsService;
    }

    @Override
    public void setAll(AllBean allBean) {
        settingsService.setSettings(allBean.getSettings());
        applicationsService.setApplications(allBean.getApplications());
    }

}
