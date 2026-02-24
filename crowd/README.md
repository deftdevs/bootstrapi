# Documentation for BootstrAPI for Crowd

<a name="documentation-for-api-endpoints"></a>
## Documentation for API Endpoints

All URIs are relative to *https://CROWD_URL/rest/bootstrapi/1*

| Class | Method | HTTP request | Description |
|------------ | ------------- | ------------- | -------------|
| *ApplicationApi* | [**createApplication**](Apis/ApplicationApi.md#createApplication) | **POST** /application | Create an application |
*ApplicationApi* | [**deleteApplication**](Apis/ApplicationApi.md#deleteApplication) | **DELETE** /application/{id} | Delete an application |
*ApplicationApi* | [**getApplication**](Apis/ApplicationApi.md#getApplication) | **GET** /application/{id} | Get an application |
*ApplicationApi* | [**updateApplication**](Apis/ApplicationApi.md#updateApplication) | **PUT** /application/{id} | Update an application |
| *ApplicationLinkApi* | [**createApplicationLink**](Apis/ApplicationLinkApi.md#createApplicationLink) | **POST** /application-link | Create an application link |
*ApplicationLinkApi* | [**deleteApplicationLink**](Apis/ApplicationLinkApi.md#deleteApplicationLink) | **DELETE** /application-link/{uuid} | Delete an application link |
*ApplicationLinkApi* | [**getApplicationLink**](Apis/ApplicationLinkApi.md#getApplicationLink) | **GET** /application-link/{uuid} | Get an application link |
*ApplicationLinkApi* | [**updateApplicationLink**](Apis/ApplicationLinkApi.md#updateApplicationLink) | **PUT** /application-link/{uuid} | Update an application link |
| *ApplicationLinksApi* | [**deleteApplicationLinks**](Apis/ApplicationLinksApi.md#deleteApplicationLinks) | **DELETE** /application-links | Delete all application links |
*ApplicationLinksApi* | [**getApplicationLinks**](Apis/ApplicationLinksApi.md#getApplicationLinks) | **GET** /application-links | Get all application links |
*ApplicationLinksApi* | [**setApplicationLinks**](Apis/ApplicationLinksApi.md#setApplicationLinks) | **PUT** /application-links | Set a list of application links |
| *ApplicationsApi* | [**deleteApplications**](Apis/ApplicationsApi.md#deleteApplications) | **DELETE** /applications | Delete all applications |
*ApplicationsApi* | [**getApplications**](Apis/ApplicationsApi.md#getApplications) | **GET** /applications | Get all applications |
*ApplicationsApi* | [**setApplications**](Apis/ApplicationsApi.md#setApplications) | **PUT** /applications | Set a list of applications |
| *DirectoriesApi* | [**deleteDirectories**](Apis/DirectoriesApi.md#deleteDirectories) | **DELETE** /directories | Delete all user directories |
*DirectoriesApi* | [**getDirectories**](Apis/DirectoriesApi.md#getDirectories) | **GET** /directories | Get all user directories |
*DirectoriesApi* | [**setDirectories**](Apis/DirectoriesApi.md#setDirectories) | **PUT** /directories | Set a list of user directories |
| *DirectoryApi* | [**createDirectory**](Apis/DirectoryApi.md#createDirectory) | **POST** /directory | Create a user directory |
*DirectoryApi* | [**deleteDirectory**](Apis/DirectoryApi.md#deleteDirectory) | **DELETE** /directory/{id} | Delete a user directory |
*DirectoryApi* | [**getDirectory**](Apis/DirectoryApi.md#getDirectory) | **GET** /directory/{id} | Get a user directory |
*DirectoryApi* | [**updateDirectory**](Apis/DirectoryApi.md#updateDirectory) | **PUT** /directory/{id} | Update a user directory |
| *GroupApi* | [**createGroup**](Apis/GroupApi.md#createGroup) | **POST** /group | Create a group |
*GroupApi* | [**getGroup**](Apis/GroupApi.md#getGroup) | **GET** /group | Get a group |
*GroupApi* | [**updateGroup**](Apis/GroupApi.md#updateGroup) | **PUT** /group | Update a group |
| *GroupsApi* | [**setGroups**](Apis/GroupsApi.md#setGroups) | **PATCH** /groups | Set groups |
| *LicenseApi* | [**addLicense**](Apis/LicenseApi.md#addLicense) | **POST** /license | Add a license |
| *LicensesApi* | [**getLicenses**](Apis/LicensesApi.md#getLicenses) | **GET** /licenses | Get all licenses information |
*LicensesApi* | [**setLicenses**](Apis/LicensesApi.md#setLicenses) | **PUT** /licenses | Set a list of licenses |
| *MailServerApi* | [**getMailServerSmtp**](Apis/MailServerApi.md#getMailServerSmtp) | **GET** /mail-server/smtp | Get the default SMTP mail server |
*MailServerApi* | [**setMailServerSmtp**](Apis/MailServerApi.md#setMailServerSmtp) | **PUT** /mail-server/smtp | Set the default SMTP mail server |
| *MailTemplatesApi* | [**getMailTemplates**](Apis/MailTemplatesApi.md#getMailTemplates) | **GET** /mail-templates | Get the mail templates |
*MailTemplatesApi* | [**setMailTemplates**](Apis/MailTemplatesApi.md#setMailTemplates) | **PUT** /mail-templates | Set the mail templates |
| *PingApi* | [**getPing**](Apis/PingApi.md#getPing) | **GET** /ping | Ping method for probing the REST API. |
| *SessionConfigApi* | [**getSessionConfig**](Apis/SessionConfigApi.md#getSessionConfig) | **GET** /session-config | Get the session config |
*SessionConfigApi* | [**setSessionConfig**](Apis/SessionConfigApi.md#setSessionConfig) | **PUT** /session-config | Set the session config |
| *SettingsApi* | [**getLoginPage**](Apis/SettingsApi.md#getLoginPage) | **GET** /settings/branding/login-page | Get the login-page settings |
*SettingsApi* | [**getSettings**](Apis/SettingsApi.md#getSettings) | **GET** /settings | Get the general settings |
*SettingsApi* | [**setLoginPage**](Apis/SettingsApi.md#setLoginPage) | **PUT** /settings/branding/login-page | Set the login-page settings |
*SettingsApi* | [**setLogo**](Apis/SettingsApi.md#setLogo) | **PUT** /settings/branding/logo | Set the logo |
*SettingsApi* | [**setSettings**](Apis/SettingsApi.md#setSettings) | **PUT** /settings | Set the general settings |
| *TrustedProxiesApi* | [**addTrustedProxy**](Apis/TrustedProxiesApi.md#addTrustedProxy) | **POST** /trusted-proxies | Add a trusted proxy |
*TrustedProxiesApi* | [**getTrustedProxies**](Apis/TrustedProxiesApi.md#getTrustedProxies) | **GET** /trusted-proxies | Get the trusted proxies |
*TrustedProxiesApi* | [**removeTrustedProxy**](Apis/TrustedProxiesApi.md#removeTrustedProxy) | **DELETE** /trusted-proxies | Remove a trusted proxy |
*TrustedProxiesApi* | [**setTrustedProxies**](Apis/TrustedProxiesApi.md#setTrustedProxies) | **PUT** /trusted-proxies | Set the trusted proxies |
| *UserApi* | [**getUser**](Apis/UserApi.md#getUser) | **GET** /user | Get a user |
*UserApi* | [**setUser**](Apis/UserApi.md#setUser) | **PUT** /user | Update an user |
*UserApi* | [**setUserPassword**](Apis/UserApi.md#setUserPassword) | **PUT** /user/password | Update a user password |


<a name="documentation-for-models"></a>
## Documentation for Models

 - [AbstractDirectoryModel](./Models/AbstractDirectoryModel.md)
 - [ApplicationDirectoryMapping](./Models/ApplicationDirectoryMapping.md)
 - [ApplicationLinkModel](./Models/ApplicationLinkModel.md)
 - [ApplicationModel](./Models/ApplicationModel.md)
 - [DirectoryCrowdAdvanced](./Models/DirectoryCrowdAdvanced.md)
 - [DirectoryCrowdModel](./Models/DirectoryCrowdModel.md)
 - [DirectoryCrowdPermissions](./Models/DirectoryCrowdPermissions.md)
 - [DirectoryCrowdServer](./Models/DirectoryCrowdServer.md)
 - [DirectoryCrowdServerProxy](./Models/DirectoryCrowdServerProxy.md)
 - [DirectoryDelegatingConfiguration](./Models/DirectoryDelegatingConfiguration.md)
 - [DirectoryDelegatingConnector](./Models/DirectoryDelegatingConnector.md)
 - [DirectoryDelegatingModel](./Models/DirectoryDelegatingModel.md)
 - [DirectoryGenericModel](./Models/DirectoryGenericModel.md)
 - [DirectoryInternalAdvanced](./Models/DirectoryInternalAdvanced.md)
 - [DirectoryInternalCredentialPolicy](./Models/DirectoryInternalCredentialPolicy.md)
 - [DirectoryInternalModel](./Models/DirectoryInternalModel.md)
 - [DirectoryLdapModel](./Models/DirectoryLdapModel.md)
 - [DirectoryLdapPermissions](./Models/DirectoryLdapPermissions.md)
 - [DirectoryLdapSchema](./Models/DirectoryLdapSchema.md)
 - [DirectoryLdapServer](./Models/DirectoryLdapServer.md)
 - [DirectoryPermissions](./Models/DirectoryPermissions.md)
 - [ErrorCollection](./Models/ErrorCollection.md)
 - [GroupModel](./Models/GroupModel.md)
 - [LicenseModel](./Models/LicenseModel.md)
 - [MailServerSmtpModel](./Models/MailServerSmtpModel.md)
 - [MailTemplatesModel](./Models/MailTemplatesModel.md)
 - [SessionConfigModel](./Models/SessionConfigModel.md)
 - [SettingsBrandingLoginPageModel](./Models/SettingsBrandingLoginPageModel.md)
 - [SettingsModel](./Models/SettingsModel.md)
 - [UserModel](./Models/UserModel.md)


<a name="documentation-for-authorization"></a>
## Documentation for Authorization

<a name="basicAuth"></a>
### basicAuth

- **Type**: HTTP basic authentication

