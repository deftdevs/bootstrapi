package com.deftdevs.bootstrapi.confluence.service;

import com.deftdevs.bootstrapi.commons.model.AbstractDirectoryModel;
import com.deftdevs.bootstrapi.commons.model.ApplicationLinkModel;
import com.deftdevs.bootstrapi.commons.model.AuthenticationModel;
import com.deftdevs.bootstrapi.commons.model.LicenseModel;
import com.deftdevs.bootstrapi.commons.model.MailServerModel;
import com.deftdevs.bootstrapi.commons.model.PermissionsModel;
import com.deftdevs.bootstrapi.commons.model.UpmModel;
import com.deftdevs.bootstrapi.commons.model.type._AllModelStatus;
import com.deftdevs.bootstrapi.commons.service._AbstractAllServiceImpl;
import com.deftdevs.bootstrapi.commons.service.api.ApplicationLinksService;
import com.deftdevs.bootstrapi.commons.service.api.DirectoriesService;
import com.deftdevs.bootstrapi.commons.service.api.LicensesService;
import com.deftdevs.bootstrapi.commons.service.api.MailServerService;
import com.deftdevs.bootstrapi.commons.service.api.PermissionsService;
import com.deftdevs.bootstrapi.commons.service.api.UpmService;
import com.deftdevs.bootstrapi.confluence.model.SettingsModel;
import com.deftdevs.bootstrapi.confluence.model._AllModel;
import com.deftdevs.bootstrapi.confluence.service.api.ConfluenceAuthenticationService;
import com.deftdevs.bootstrapi.confluence.service.api.ConfluenceSettingsService;

import java.util.LinkedHashMap;
import java.util.Map;


public class _AllServiceImpl extends _AbstractAllServiceImpl<_AllModel> {

    private final ConfluenceSettingsService settingsService;
    private final DirectoriesService directoriesService;
    private final ApplicationLinksService applicationLinksService;
    private final ConfluenceAuthenticationService authenticationService;
    private final LicensesService licensesService;
    private final MailServerService mailServerService;
    private final PermissionsService permissionsService;
    private final UpmService upmService;

    public _AllServiceImpl(
            final ConfluenceSettingsService settingsService,
            final DirectoriesService directoriesService,
            final ApplicationLinksService applicationLinksService,
            final ConfluenceAuthenticationService authenticationService,
            final LicensesService licensesService,
            final MailServerService mailServerService,
            final PermissionsService permissionsService,
            final UpmService upmService) {

        this.settingsService = settingsService;
        this.directoriesService = directoriesService;
        this.applicationLinksService = applicationLinksService;
        this.authenticationService = authenticationService;
        this.licensesService = licensesService;
        this.mailServerService = mailServerService;
        this.permissionsService = permissionsService;
        this.upmService = upmService;
    }

    @Override
    public _AllModel setAll(
            final _AllModel allModel) {

        final _AllModel result = new _AllModel();
        final Map<String, _AllModelStatus> statusMap = new LinkedHashMap<>();

        // plugins are applied first so the sections below can configure them
        setEntityWithStatus(UpmModel.class, allModel.getUpm(),
                upmService::setUpm, result::setUpm, statusMap);

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

        setEntityWithStatus(PermissionsModel.class, allModel.getPermissions(),
                permissionsService::setPermissions, result::setPermissions, statusMap);

        result.setStatus(statusMap);
        return result;
    }
}
