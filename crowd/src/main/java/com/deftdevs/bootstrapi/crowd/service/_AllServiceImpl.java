package com.deftdevs.bootstrapi.crowd.service;

import com.deftdevs.bootstrapi.commons.model.MailServerModel;
import com.deftdevs.bootstrapi.commons.model.type._AllModelStatus;
import com.deftdevs.bootstrapi.commons.service._AbstractAllServiceImpl;
import com.deftdevs.bootstrapi.commons.service.api.ApplicationLinksService;
import com.deftdevs.bootstrapi.commons.service.api.DirectoriesService;
import com.deftdevs.bootstrapi.commons.service.api.LicensesService;
import com.deftdevs.bootstrapi.commons.service.api.MailServerService;
import com.deftdevs.bootstrapi.crowd.model.SettingsModel;
import com.deftdevs.bootstrapi.crowd.model._AllModel;
import com.deftdevs.bootstrapi.crowd.service.api.ApplicationsService;
import com.deftdevs.bootstrapi.crowd.service.api.CrowdSettingsBrandingService;
import com.deftdevs.bootstrapi.crowd.service.api.CrowdSettingsGeneralService;
import com.deftdevs.bootstrapi.crowd.service.api.MailTemplatesService;
import com.deftdevs.bootstrapi.crowd.service.api.SessionConfigService;
import com.deftdevs.bootstrapi.crowd.service.api.TrustedProxiesService;

import java.util.HashMap;
import java.util.Map;

public class _AllServiceImpl extends _AbstractAllServiceImpl<_AllModel> {

    private final CrowdSettingsGeneralService settingsService;
    private final CrowdSettingsBrandingService brandingService;
    private final DirectoriesService directoriesService;
    private final ApplicationsService applicationsService;
    private final ApplicationLinksService applicationLinksService;
    private final LicensesService licensesService;
    private final MailServerService mailServerService;
    private final MailTemplatesService mailTemplatesService;
    private final SessionConfigService sessionConfigService;
    private final TrustedProxiesService trustedProxiesService;

    public _AllServiceImpl(
            final CrowdSettingsGeneralService settingsService,
            final CrowdSettingsBrandingService brandingService,
            final DirectoriesService directoriesService,
            final ApplicationsService applicationsService,
            final ApplicationLinksService applicationLinksService,
            final LicensesService licensesService,
            final MailServerService mailServerService,
            final MailTemplatesService mailTemplatesService,
            final SessionConfigService sessionConfigService,
            final TrustedProxiesService trustedProxiesService) {

        this.settingsService = settingsService;
        this.brandingService = brandingService;
        this.directoriesService = directoriesService;
        this.applicationsService = applicationsService;
        this.applicationLinksService = applicationLinksService;
        this.licensesService = licensesService;
        this.mailServerService = mailServerService;
        this.mailTemplatesService = mailTemplatesService;
        this.sessionConfigService = sessionConfigService;
        this.trustedProxiesService = trustedProxiesService;
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
            setEntity("settings/branding", settingsInput.getBranding(),
                    brandingService::setLoginPage, settingsResult::setBranding, statusMap);
            result.setSettings(settingsResult);
        }

        setEntities("directories", allModel.getDirectories(),
                directoriesService::setDirectories, result::setDirectories, statusMap);

        setEntities("applications", allModel.getApplications(),
                applicationsService::setApplications, result::setApplications, statusMap);

        setEntities("applicationLinks", allModel.getApplicationLinks(),
                applicationLinksService::setApplicationLinks, result::setApplicationLinks, statusMap);

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

        setEntity("mailTemplates", allModel.getMailTemplates(),
                mailTemplatesService::setMailTemplates, result::setMailTemplates, statusMap);

        setEntity("sessionConfig", allModel.getSessionConfig(),
                sessionConfigService::setSessionConfig, result::setSessionConfig, statusMap);

        setEntity("trustedProxies", allModel.getTrustedProxies(),
                trustedProxiesService::setTrustedProxies, result::setTrustedProxies, statusMap);

        result.setStatus(statusMap);
        return result;
    }
}
