package com.deftdevs.bootstrapi.jira.service;

import com.deftdevs.bootstrapi.commons.model.AbstractDirectoryModel;
import com.deftdevs.bootstrapi.commons.model.ApplicationLinkModel;
import com.deftdevs.bootstrapi.commons.model.AuthenticationModel;
import com.deftdevs.bootstrapi.commons.model.LicenseModel;
import com.deftdevs.bootstrapi.commons.model.MailServerModel;
import com.deftdevs.bootstrapi.commons.model.PermissionsGlobalModel;
import com.deftdevs.bootstrapi.commons.model.type._AllModelStatus;
import com.deftdevs.bootstrapi.commons.service._AbstractAllServiceImpl;
import com.deftdevs.bootstrapi.commons.service.api.ApplicationLinksService;
import com.deftdevs.bootstrapi.commons.service.api.DirectoriesService;
import com.deftdevs.bootstrapi.commons.service.api.LicensesService;
import com.deftdevs.bootstrapi.commons.service.api.MailServerService;
import com.deftdevs.bootstrapi.commons.service.api.PermissionsService;
import com.deftdevs.bootstrapi.jira.model.SettingsModel;
import com.deftdevs.bootstrapi.jira.model._AllModel;
import com.deftdevs.bootstrapi.jira.service.api.JiraAuthenticationService;
import com.deftdevs.bootstrapi.jira.service.api.JiraSettingsService;

import java.util.LinkedHashMap;
import java.util.Map;


public class _AllServiceImpl extends _AbstractAllServiceImpl<_AllModel> {

    private final JiraSettingsService settingsService;
    private final DirectoriesService directoriesService;
    private final ApplicationLinksService applicationLinksService;
    private final JiraAuthenticationService authenticationService;
    private final LicensesService licensesService;
    private final MailServerService mailServerService;
    private final PermissionsService permissionsService;

    public _AllServiceImpl(
            final JiraSettingsService settingsService,
            final DirectoriesService directoriesService,
            final ApplicationLinksService applicationLinksService,
            final JiraAuthenticationService authenticationService,
            final LicensesService licensesService,
            final MailServerService mailServerService,
            final PermissionsService permissionsService) {

        this.settingsService = settingsService;
        this.directoriesService = directoriesService;
        this.applicationLinksService = applicationLinksService;
        this.authenticationService = authenticationService;
        this.licensesService = licensesService;
        this.mailServerService = mailServerService;
        this.permissionsService = permissionsService;
    }

    @Override
    public _AllModel setAll(
            final _AllModel allModel) {

        final _AllModel result = new _AllModel();
        final Map<String, _AllModelStatus> statusMap = new LinkedHashMap<>();

        setEntityWithStatus(SettingsModel.class, allModel.getSettings(),
                settingsService::setSettings, result::setSettings, statusMap);

        setEntities(AbstractDirectoryModel.class, allModel.getDirectories(),
                directoriesService::setDirectories, result::setDirectories, statusMap);

        setEntities(ApplicationLinkModel.class, allModel.getApplicationLinks(),
                applicationLinksService::setApplicationLinks, result::setApplicationLinks, statusMap);

        setEntityWithStatus(AuthenticationModel.class, allModel.getAuthentication(),
                authenticationService::setAuthentication, result::setAuthentication, statusMap);

        setEntities(LicenseModel.class, allModel.getLicenses(),
                licensesService::setLicenses, result::setLicenses, statusMap);

        setEntityWithStatus(MailServerModel.class, allModel.getMailServer(),
                mailServerService::setMailServer, result::setMailServer, statusMap);

        setEntity(PermissionsGlobalModel.class, allModel.getPermissionsGlobal(),
                permissionsService::setPermissionsGlobal, result::setPermissionsGlobal, statusMap);

        result.setStatus(statusMap);
        return result;
    }
}
