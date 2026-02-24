package com.deftdevs.bootstrapi.confluence.service;

import com.deftdevs.bootstrapi.commons.model.AuthenticationModel;
import com.deftdevs.bootstrapi.commons.model.MailServerModel;
import com.deftdevs.bootstrapi.commons.model.type._AllModelStatus;
import com.deftdevs.bootstrapi.commons.service._AbstractAllServiceImpl;
import com.deftdevs.bootstrapi.commons.service.api.ApplicationLinksService;
import com.deftdevs.bootstrapi.commons.service.api.DirectoriesService;
import com.deftdevs.bootstrapi.commons.service.api.LicensesService;
import com.deftdevs.bootstrapi.commons.service.api.MailServerService;
import com.deftdevs.bootstrapi.commons.service.api.PermissionsService;
import com.deftdevs.bootstrapi.commons.service.api.SettingsBrandingService;
import com.deftdevs.bootstrapi.confluence.model.SettingsModel;
import com.deftdevs.bootstrapi.confluence.model._AllModel;
import com.deftdevs.bootstrapi.confluence.service.api.ConfluenceAuthenticationService;
import com.deftdevs.bootstrapi.confluence.service.api.ConfluenceSettingsService;

import java.util.HashMap;
import java.util.Map;

public class _AllServiceImpl extends _AbstractAllServiceImpl<_AllModel> {

    private final ConfluenceSettingsService settingsService;
    private final DirectoriesService directoriesService;
    private final ApplicationLinksService applicationLinksService;
    private final ConfluenceAuthenticationService authenticationService;
    private final LicensesService licensesService;
    private final MailServerService mailServerService;
    private final PermissionsService permissionsService;
    private final SettingsBrandingService settingsBrandingService;

    public _AllServiceImpl(
            final ConfluenceSettingsService settingsService,
            final DirectoriesService directoriesService,
            final ApplicationLinksService applicationLinksService,
            final ConfluenceAuthenticationService authenticationService,
            final LicensesService licensesService,
            final MailServerService mailServerService,
            final PermissionsService permissionsService,
            final SettingsBrandingService settingsBrandingService) {

        this.settingsService = settingsService;
        this.directoriesService = directoriesService;
        this.applicationLinksService = applicationLinksService;
        this.authenticationService = authenticationService;
        this.licensesService = licensesService;
        this.mailServerService = mailServerService;
        this.permissionsService = permissionsService;
        this.settingsBrandingService = settingsBrandingService;
    }

    @Override
    public _AllModel setAll(
            final _AllModel allModel) {

        final _AllModel result = new _AllModel();
        final Map<String, _AllModelStatus> statusMap = new HashMap<>();

        // Settings wrapper
        final SettingsModel settingsInput = allModel.getSettings();
        if (settingsInput != null) {
            final SettingsModel settingsResult = new SettingsModel();
            setEntity("settings/general", settingsInput.getGeneral(),
                    settingsService::setSettingsGeneral, settingsResult::setGeneral, statusMap);
            setEntity("settings/security", settingsInput.getSecurity(),
                    settingsService::setSettingsSecurity, settingsResult::setSecurity, statusMap);
            setEntity("settings/branding", settingsInput.getBranding(),
                    settingsBrandingService::setColourScheme, settingsResult::setBranding, statusMap);
            setEntity("settings/customHtml", settingsInput.getCustomHtml(),
                    settingsService::setCustomHtml, settingsResult::setCustomHtml, statusMap);
            result.setSettings(settingsResult);
        }

        setEntities("directories", allModel.getDirectories(),
                directoriesService::setDirectories, result::setDirectories, statusMap);

        setEntities("applicationLinks", allModel.getApplicationLinks(),
                applicationLinksService::setApplicationLinks, result::setApplicationLinks, statusMap);

        // Authentication wrapper
        final AuthenticationModel authInput = allModel.getAuthentication();
        if (authInput != null) {
            final AuthenticationModel authResult = new AuthenticationModel();
            setEntities("authentication/idps", authInput.getIdps(),
                    authenticationService::setAuthenticationIdps, authResult::setIdps, statusMap);
            setEntity("authentication/sso", authInput.getSso(),
                    authenticationService::setAuthenticationSso, authResult::setSso, statusMap);
            result.setAuthentication(authResult);
        }

        setEntity("licenses", allModel.getLicenses(),
                keys -> { licensesService.setLicenses(keys); return keys; },
                result::setLicenses, statusMap);

        // Mail server wrapper
        final MailServerModel mailServerInput = allModel.getMailServer();
        if (mailServerInput != null) {
            final MailServerModel mailServerResult = new MailServerModel();
            setEntity("mailServer/smtp", mailServerInput.getSmtp(),
                    mailServerService::setMailServerSmtp, mailServerResult::setSmtp, statusMap);
            setEntity("mailServer/pop", mailServerInput.getPop(),
                    mailServerService::setMailServerPop, mailServerResult::setPop, statusMap);
            result.setMailServer(mailServerResult);
        }

        setEntity("permissionsGlobal", allModel.getPermissionsGlobal(),
                permissionsService::setPermissionsGlobal, result::setPermissionsGlobal, statusMap);

        result.setStatus(statusMap);
        return result;
    }
}
