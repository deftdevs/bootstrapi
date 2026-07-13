package com.deftdevs.bootstrapi.confluence.service;

import com.deftdevs.bootstrapi.commons.model.AbstractDirectoryModel;
import com.deftdevs.bootstrapi.commons.model.ApplicationLinkModel;
import com.deftdevs.bootstrapi.commons.model.AuthenticationModel;
import com.deftdevs.bootstrapi.commons.model.LicenseModel;
import com.deftdevs.bootstrapi.commons.model.MailServerModel;
import com.deftdevs.bootstrapi.commons.model.MailServerSmtpModel;
import com.deftdevs.bootstrapi.commons.model.PermissionsGlobalModel;
import com.deftdevs.bootstrapi.commons.model.PermissionsModel;
import com.deftdevs.bootstrapi.commons.model.AuthenticationSsoModel;
import com.deftdevs.bootstrapi.commons.model.SettingsGeneralModel;
import com.deftdevs.bootstrapi.commons.model.type.ServiceResult;
import com.deftdevs.bootstrapi.commons.util.FieldNames;
import com.deftdevs.bootstrapi.commons.util.LicenseKeyRedactor;
import com.deftdevs.bootstrapi.commons.model.type._AllModelStatus;
import com.deftdevs.bootstrapi.commons.service.api.ApplicationLinksService;
import com.deftdevs.bootstrapi.commons.service.api.DirectoriesService;
import com.deftdevs.bootstrapi.commons.service.api.LicensesService;
import com.deftdevs.bootstrapi.commons.service.api.MailServerService;
import com.deftdevs.bootstrapi.commons.service.api.PermissionsService;
import com.deftdevs.bootstrapi.confluence.model.SettingsModel;
import com.deftdevs.bootstrapi.confluence.model._AllModel;
import com.deftdevs.bootstrapi.confluence.service.api.ConfluenceAuthenticationService;
import com.deftdevs.bootstrapi.confluence.service.api.ConfluenceSettingsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.Collections;
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
    private ConfluenceSettingsService settingsService;

    @Mock
    private DirectoriesService directoriesService;

    @Mock
    private ApplicationLinksService applicationLinksService;

    @Mock
    private ConfluenceAuthenticationService authenticationService;

    @Mock
    private LicensesService licensesService;

    @Mock
    private MailServerService mailServerService;

    @Mock
    private PermissionsService permissionsService;

    private _AllServiceImpl allService;

    @BeforeEach
    void setup() {
        allService = new _AllServiceImpl(
                settingsService,
                directoriesService,
                applicationLinksService,
                authenticationService,
                licensesService,
                mailServerService,
                permissionsService);
    }

    @Test
    void testSetAllEmptyModelYieldsEmptyStatus() {
        final _AllModel result = allService.setAll(new _AllModel());

        assertTrue(result.getStatus().isEmpty());
        verifyNoInteractions(settingsService, directoriesService, applicationLinksService,
                authenticationService, licensesService, mailServerService, permissionsService);
    }

    @Test
    void testSetAllAppliesAllFields() {
        final SettingsModel settings = new SettingsModel();
        settings.setGeneral(SettingsGeneralModel.EXAMPLE_1);
        final Map<String, AbstractDirectoryModel> directories =
                Collections.singletonMap("directory", mock(AbstractDirectoryModel.class));
        final Map<String, ApplicationLinkModel> applicationLinks =
                Collections.singletonMap("link", ApplicationLinkModel.EXAMPLE_1);
        final AuthenticationModel authentication = new AuthenticationModel();
        final Map<String, LicenseModel> licenses =
                Collections.singletonMap("licenseKey", LicenseModel.EXAMPLE_1);
        final Map<String, LicenseModel> redactedLicenses =
                Collections.singletonMap(LicenseKeyRedactor.redact("licenseKey"), LicenseModel.EXAMPLE_1);
        final MailServerModel mailServer = new MailServerModel(MailServerSmtpModel.EXAMPLE_1, null);
        final PermissionsModel permissions = new PermissionsModel();

        doReturn(new ServiceResult<>(settings,
                Collections.singletonMap(FieldNames.of(SettingsModel.class, SettingsGeneralModel.class), _AllModelStatus.success())))
                .when(settingsService).setSettings(settings);
        doReturn(directories).when(directoriesService).setDirectories(directories);
        doReturn(applicationLinks).when(applicationLinksService).setApplicationLinks(applicationLinks);
        doReturn(new ServiceResult<>(authentication,
                Collections.singletonMap(FieldNames.of(AuthenticationModel.class, AuthenticationSsoModel.class), _AllModelStatus.success())))
                .when(authenticationService).setAuthentication(authentication);
        doReturn(redactedLicenses).when(licensesService).setLicenses(licenses);
        doReturn(new ServiceResult<>(mailServer,
                Collections.singletonMap(FieldNames.of(MailServerModel.class, MailServerSmtpModel.class), _AllModelStatus.success())))
                .when(mailServerService).setMailServer(mailServer);
        doReturn(new ServiceResult<>(permissions,
                Collections.singletonMap(FieldNames.of(PermissionsModel.class, PermissionsGlobalModel.class), _AllModelStatus.success())))
                .when(permissionsService).setPermissions(permissions);

        final _AllModel allModel = new _AllModel();
        allModel.setSettings(settings);
        allModel.setDirectories(directories);
        allModel.setApplicationLinks(applicationLinks);
        allModel.setAuthentication(authentication);
        allModel.setLicenses(licenses);
        allModel.setMailServer(mailServer);
        allModel.setPermissions(permissions);

        final _AllModel result = allService.setAll(allModel);

        assertEquals(settings, result.getSettings());
        assertEquals(directories, result.getDirectories());
        assertEquals(applicationLinks, result.getApplicationLinks());
        assertEquals(authentication, result.getAuthentication());
        assertEquals(redactedLicenses, result.getLicenses());
        assertEquals(mailServer, result.getMailServer());
        assertEquals(permissions, result.getPermissions());

        final Map<String, _AllModelStatus> status = result.getStatus();
        assertEquals(7, status.size());
        assertEquals(200, status.get(FieldNames.pathOf(_AllModel.class, SettingsGeneralModel.class)).getStatus());
        assertEquals(200, status.get(FieldNames.of(_AllModel.class, AbstractDirectoryModel.class)).getStatus());
        assertEquals(200, status.get(FieldNames.of(_AllModel.class, ApplicationLinkModel.class)).getStatus());
        assertEquals(200, status.get(FieldNames.pathOf(_AllModel.class, AuthenticationSsoModel.class)).getStatus());
        assertEquals(200, status.get(FieldNames.of(_AllModel.class, LicenseModel.class)).getStatus());
        assertEquals(200, status.get(FieldNames.pathOf(_AllModel.class, MailServerSmtpModel.class)).getStatus());
        assertEquals(200, status.get(FieldNames.pathOf(_AllModel.class, PermissionsGlobalModel.class)).getStatus());
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
