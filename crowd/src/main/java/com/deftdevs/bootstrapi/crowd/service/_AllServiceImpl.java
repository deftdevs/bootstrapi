package com.deftdevs.bootstrapi.crowd.service;

import com.deftdevs.bootstrapi.commons.model.AbstractDirectoryModel;
import com.deftdevs.bootstrapi.commons.model.ApplicationLinkModel;
import com.deftdevs.bootstrapi.commons.model.LicenseModel;
import com.deftdevs.bootstrapi.commons.model.MailServerModel;
import com.deftdevs.bootstrapi.commons.model.type._AllModelStatus;
import com.deftdevs.bootstrapi.commons.service._AbstractAllServiceImpl;
import com.deftdevs.bootstrapi.commons.service.api.ApplicationLinksService;
import com.deftdevs.bootstrapi.commons.service.api.DirectoriesService;
import com.deftdevs.bootstrapi.commons.service.api.LicensesService;
import com.deftdevs.bootstrapi.commons.service.api.MailServerService;
import com.deftdevs.bootstrapi.crowd.model.ApplicationModel;
import com.deftdevs.bootstrapi.crowd.model.MailTemplatesModel;
import com.deftdevs.bootstrapi.crowd.model.SessionConfigModel;
import com.deftdevs.bootstrapi.crowd.model.SettingsModel;
import com.deftdevs.bootstrapi.crowd.model._AllModel;
import com.deftdevs.bootstrapi.crowd.service.api.ApplicationsService;
import com.deftdevs.bootstrapi.crowd.service.api.CrowdSettingsService;
import com.deftdevs.bootstrapi.crowd.service.api.MailTemplatesService;
import com.deftdevs.bootstrapi.crowd.service.api.SessionConfigService;
import com.deftdevs.bootstrapi.crowd.service.api.TrustedProxiesService;

import java.util.LinkedHashMap;
import java.util.Map;


public class _AllServiceImpl extends _AbstractAllServiceImpl<_AllModel> {

    private final CrowdSettingsService settingsService;
    private final DirectoriesService directoriesService;
    private final ApplicationsService applicationsService;
    private final ApplicationLinksService applicationLinksService;
    private final LicensesService licensesService;
    private final MailServerService mailServerService;
    private final MailTemplatesService mailTemplatesService;
    private final SessionConfigService sessionConfigService;
    private final TrustedProxiesService trustedProxiesService;

    public _AllServiceImpl(
            final CrowdSettingsService settingsService,
            final DirectoriesService directoriesService,
            final ApplicationsService applicationsService,
            final ApplicationLinksService applicationLinksService,
            final LicensesService licensesService,
            final MailServerService mailServerService,
            final MailTemplatesService mailTemplatesService,
            final SessionConfigService sessionConfigService,
            final TrustedProxiesService trustedProxiesService) {

        this.settingsService = settingsService;
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
        final Map<String, _AllModelStatus> statusMap = new LinkedHashMap<>();

        setEntityWithStatus(SettingsModel.class, allModel.getSettings(),
                settingsService::setSettings, result::setSettings, statusMap);

        setEntities(AbstractDirectoryModel.class, allModel.getDirectories(),
                directoriesService::setDirectories, result::setDirectories, statusMap);

        setEntities(ApplicationModel.class, allModel.getApplications(),
                applicationsService::setApplications, result::setApplications, statusMap);

        setEntities(ApplicationLinkModel.class, allModel.getApplicationLinks(),
                applicationLinksService::setApplicationLinks, result::setApplicationLinks, statusMap);

        setEntities(LicenseModel.class, allModel.getLicenses(),
                licensesService::setLicenses, result::setLicenses, statusMap);

        setEntityWithStatus(MailServerModel.class, allModel.getMailServer(),
                mailServerService::setMailServer, result::setMailServer, statusMap);

        setEntity(MailTemplatesModel.class, allModel.getMailTemplates(),
                mailTemplatesService::setMailTemplates, result::setMailTemplates, statusMap);

        setEntity(SessionConfigModel.class, allModel.getSessionConfig(),
                sessionConfigService::setSessionConfig, result::setSessionConfig, statusMap);

        setEntity(_AllModel::getTrustedProxies, allModel,
                trustedProxiesService::setTrustedProxies, result::setTrustedProxies, statusMap);

        result.setStatus(statusMap);
        return result;
    }
}
