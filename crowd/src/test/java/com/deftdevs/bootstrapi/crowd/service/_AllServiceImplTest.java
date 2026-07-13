package com.deftdevs.bootstrapi.crowd.service;

import com.deftdevs.bootstrapi.commons.model.AbstractDirectoryModel;
import com.deftdevs.bootstrapi.commons.model.ApplicationLinkModel;
import com.deftdevs.bootstrapi.commons.model.LicenseModel;
import com.deftdevs.bootstrapi.commons.model.MailServerModel;
import com.deftdevs.bootstrapi.commons.model.MailServerSmtpModel;
import com.deftdevs.bootstrapi.commons.model.SettingsGeneralModel;
import com.deftdevs.bootstrapi.commons.model.type.ServiceResult;
import com.deftdevs.bootstrapi.commons.util.FieldNames;
import com.deftdevs.bootstrapi.commons.util.LicenseKeyRedactor;
import com.deftdevs.bootstrapi.commons.model.type._AllModelStatus;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class _AllServiceImplTest {

    @Mock
    private CrowdSettingsService settingsService;

    @Mock
    private DirectoriesService directoriesService;

    @Mock
    private ApplicationsService applicationsService;

    @Mock
    private ApplicationLinksService applicationLinksService;

    @Mock
    private LicensesService licensesService;

    @Mock
    private MailServerService mailServerService;

    @Mock
    private MailTemplatesService mailTemplatesService;

    @Mock
    private SessionConfigService sessionConfigService;

    @Mock
    private TrustedProxiesService trustedProxiesService;

    private _AllServiceImpl allService;

    @BeforeEach
    void setup() {
        allService = new _AllServiceImpl(
                settingsService,
                directoriesService,
                applicationsService,
                applicationLinksService,
                licensesService,
                mailServerService,
                mailTemplatesService,
                sessionConfigService,
                trustedProxiesService);
    }

    @Test
    void testSetAllEmptyModelYieldsEmptyStatus() {
        final _AllModel result = allService.setAll(new _AllModel());

        assertTrue(result.getStatus().isEmpty());
        verifyNoInteractions(settingsService, directoriesService, applicationsService,
                applicationLinksService, licensesService, mailServerService,
                mailTemplatesService, sessionConfigService, trustedProxiesService);
    }

    @Test
    void testSetAllAppliesAllFields() {
        final SettingsModel settings = new SettingsModel();
        settings.setGeneral(SettingsGeneralModel.EXAMPLE_1);
        final Map<String, AbstractDirectoryModel> directories =
                Collections.singletonMap("directory", mock(AbstractDirectoryModel.class));
        final Map<String, ApplicationModel> applications =
                Collections.singletonMap("application", new ApplicationModel());
        final Map<String, ApplicationLinkModel> applicationLinks =
                Collections.singletonMap("link", ApplicationLinkModel.EXAMPLE_1);
        final Map<String, LicenseModel> licenses =
                Collections.singletonMap("licenseKey", LicenseModel.EXAMPLE_1);
        final Map<String, LicenseModel> redactedLicenses =
                Collections.singletonMap(LicenseKeyRedactor.redact("licenseKey"), LicenseModel.EXAMPLE_1);
        final MailServerModel mailServer = new MailServerModel(MailServerSmtpModel.EXAMPLE_1, null);
        final MailTemplatesModel mailTemplates = MailTemplatesModel.EXAMPLE_1;
        final SessionConfigModel sessionConfig = new SessionConfigModel();
        final List<String> trustedProxies = Collections.singletonList("192.168.0.1");

        doReturn(new ServiceResult<>(settings,
                Collections.singletonMap(FieldNames.of(SettingsModel.class, SettingsGeneralModel.class), _AllModelStatus.success())))
                .when(settingsService).setSettings(settings);
        doReturn(directories).when(directoriesService).setDirectories(directories);
        doReturn(applications).when(applicationsService).setApplications(applications);
        doReturn(applicationLinks).when(applicationLinksService).setApplicationLinks(applicationLinks);
        doReturn(redactedLicenses).when(licensesService).setLicenses(licenses);
        doReturn(new ServiceResult<>(mailServer,
                Collections.singletonMap(FieldNames.of(MailServerModel.class, MailServerSmtpModel.class), _AllModelStatus.success())))
                .when(mailServerService).setMailServer(mailServer);
        doReturn(mailTemplates).when(mailTemplatesService).setMailTemplates(mailTemplates);
        doReturn(sessionConfig).when(sessionConfigService).setSessionConfig(sessionConfig);
        doReturn(trustedProxies).when(trustedProxiesService).setTrustedProxies(trustedProxies);

        final _AllModel allModel = new _AllModel();
        allModel.setSettings(settings);
        allModel.setDirectories(directories);
        allModel.setApplications(applications);
        allModel.setApplicationLinks(applicationLinks);
        allModel.setLicenses(licenses);
        allModel.setMailServer(mailServer);
        allModel.setMailTemplates(mailTemplates);
        allModel.setSessionConfig(sessionConfig);
        allModel.setTrustedProxies(trustedProxies);

        final _AllModel result = allService.setAll(allModel);

        assertEquals(settings, result.getSettings());
        assertEquals(directories, result.getDirectories());
        assertEquals(applications, result.getApplications());
        assertEquals(applicationLinks, result.getApplicationLinks());
        assertEquals(redactedLicenses, result.getLicenses());
        assertEquals(mailServer, result.getMailServer());
        assertEquals(mailTemplates, result.getMailTemplates());
        assertEquals(sessionConfig, result.getSessionConfig());
        assertEquals(trustedProxies, result.getTrustedProxies());

        final Map<String, _AllModelStatus> status = result.getStatus();
        assertEquals(9, status.size());
        assertEquals(200, status.get(FieldNames.pathOf(_AllModel.class, SettingsGeneralModel.class)).getStatus());
        assertEquals(200, status.get(FieldNames.of(_AllModel.class, AbstractDirectoryModel.class)).getStatus());
        assertEquals(200, status.get(FieldNames.of(_AllModel.class, ApplicationModel.class)).getStatus());
        assertEquals(200, status.get(FieldNames.of(_AllModel.class, ApplicationLinkModel.class)).getStatus());
        assertEquals(200, status.get(FieldNames.of(_AllModel.class, LicenseModel.class)).getStatus());
        assertEquals(200, status.get(FieldNames.pathOf(_AllModel.class, MailServerSmtpModel.class)).getStatus());
        assertEquals(200, status.get(FieldNames.of(_AllModel.class, MailTemplatesModel.class)).getStatus());
        assertEquals(200, status.get(FieldNames.of(_AllModel.class, SessionConfigModel.class)).getStatus());
        assertEquals(200, status.get(FieldNames.of(_AllModel.class, String.class)).getStatus());
    }

    @Test
    void testSetAllRecordsFailureAndContinuesWithOtherFields() {
        final SettingsModel settings = new SettingsModel();
        settings.setGeneral(SettingsGeneralModel.EXAMPLE_1);
        final Map<String, LicenseModel> licenses =
                Collections.singletonMap("licenseKey", LicenseModel.EXAMPLE_1);

        doReturn(new ServiceResult<>(settings,
                Collections.singletonMap(FieldNames.of(SettingsModel.class, SettingsGeneralModel.class), _AllModelStatus.success())))
                .when(settingsService).setSettings(settings);
        doThrow(new WebApplicationException(Response.Status.CONFLICT))
                .when(licensesService).setLicenses(licenses);

        final _AllModel allModel = new _AllModel();
        allModel.setSettings(settings);
        allModel.setLicenses(licenses);

        final _AllModel result = allService.setAll(allModel);

        assertEquals(settings, result.getSettings());
        assertNull(result.getLicenses());
        assertEquals(200, result.getStatus().get(FieldNames.pathOf(_AllModel.class, SettingsGeneralModel.class)).getStatus());
        assertEquals(409, result.getStatus().get(FieldNames.of(_AllModel.class, LicenseModel.class)).getStatus());
    }
}
