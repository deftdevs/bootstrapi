# Documentation for BootstrAPI for Crowd

<a name="documentation-for-api-endpoints"></a>
## Documentation for API Endpoints

All URIs are relative to *https://<CROWD_URL>/rest/bootstrapi/1*

| Class | Method | HTTP request | Description |
|------------ | ------------- | ------------- | -------------|
| *ApplicationApi* | [**createApplication**](Apis/ApplicationApi.md#createapplication) | **POST** /application | Create an application |
*ApplicationApi* | [**deleteApplication**](Apis/ApplicationApi.md#deleteapplication) | **DELETE** /application/{id} | Delete an application |
*ApplicationApi* | [**getApplication**](Apis/ApplicationApi.md#getapplication) | **GET** /application/{id} | Get an application |
*ApplicationApi* | [**updateApplication**](Apis/ApplicationApi.md#updateapplication) | **PUT** /application/{id} | Update an application |
| *ApplicationLinkApi* | [**createApplicationLink**](Apis/ApplicationLinkApi.md#createapplicationlink) | **POST** /application-link | Create an application link |
*ApplicationLinkApi* | [**deleteApplicationLink**](Apis/ApplicationLinkApi.md#deleteapplicationlink) | **DELETE** /application-link/{uuid} | Delete an application link |
*ApplicationLinkApi* | [**getApplicationLink**](Apis/ApplicationLinkApi.md#getapplicationlink) | **GET** /application-link/{uuid} | Get an application link |
*ApplicationLinkApi* | [**updateApplicationLink**](Apis/ApplicationLinkApi.md#updateapplicationlink) | **PUT** /application-link/{uuid} | Update an application link |
| *ApplicationLinksApi* | [**deleteApplicationLinks**](Apis/ApplicationLinksApi.md#deleteapplicationlinks) | **DELETE** /application-links | Delete all application links |
*ApplicationLinksApi* | [**getApplicationLinks**](Apis/ApplicationLinksApi.md#getapplicationlinks) | **GET** /application-links | Get all application links |
*ApplicationLinksApi* | [**setApplicationLinks**](Apis/ApplicationLinksApi.md#setapplicationlinks) | **PUT** /application-links | Set a list of application links |
| *ApplicationsApi* | [**deleteApplications**](Apis/ApplicationsApi.md#deleteapplications) | **DELETE** /applications | Delete all applications |
*ApplicationsApi* | [**getApplications**](Apis/ApplicationsApi.md#getapplications) | **GET** /applications | Get all applications |
*ApplicationsApi* | [**setApplications**](Apis/ApplicationsApi.md#setapplications) | **PUT** /applications | Set a list of applications |
| *DirectoriesApi* | [**deleteDirectories**](Apis/DirectoriesApi.md#deletedirectories) | **DELETE** /directories | Delete all user directories |
*DirectoriesApi* | [**getDirectories**](Apis/DirectoriesApi.md#getdirectories) | **GET** /directories | Get all user directories |
*DirectoriesApi* | [**setDirectories**](Apis/DirectoriesApi.md#setdirectories) | **PUT** /directories | Set a list of user directories |
| *DirectoryApi* | [**createDirectory**](Apis/DirectoryApi.md#createdirectory) | **POST** /directory | Create a user directory |
*DirectoryApi* | [**deleteDirectory**](Apis/DirectoryApi.md#deletedirectory) | **DELETE** /directory/{id} | Delete a user directory |
*DirectoryApi* | [**getDirectory**](Apis/DirectoryApi.md#getdirectory) | **GET** /directory/{id} | Get a user directory |
*DirectoryApi* | [**updateDirectory**](Apis/DirectoryApi.md#updatedirectory) | **PUT** /directory/{id} | Update a user directory |
| *GroupApi* | [**createGroup**](Apis/GroupApi.md#creategroup) | **POST** /group | Create a group |
*GroupApi* | [**getGroup**](Apis/GroupApi.md#getgroup) | **GET** /group | Get a group |
*GroupApi* | [**updateGroup**](Apis/GroupApi.md#updategroup) | **PUT** /group | Update a group |
| *GroupsApi* | [**setGroups**](Apis/GroupsApi.md#setgroups) | **PATCH** /groups | Set groups |
| *LicensesApi* | [**addLicense**](Apis/LicensesApi.md#addlicense) | **POST** /licenses | Add a license |
*LicensesApi* | [**getLicenses**](Apis/LicensesApi.md#getlicenses) | **GET** /licenses | Get all licenses information |
| *MailServerApi* | [**getMailServerSmtp**](Apis/MailServerApi.md#getmailserversmtp) | **GET** /mail-server/smtp | Get the default SMTP mail server |
*MailServerApi* | [**setMailServerSmtp**](Apis/MailServerApi.md#setmailserversmtp) | **PUT** /mail-server/smtp | Set the default SMTP mail server |
| *MailTemplatesApi* | [**getMailTemplates**](Apis/MailTemplatesApi.md#getmailtemplates) | **GET** /mail-templates | Get the mail templates |
*MailTemplatesApi* | [**setMailTemplates**](Apis/MailTemplatesApi.md#setmailtemplates) | **PUT** /mail-templates | Set the mail templates |
| *PingApi* | [**getPing**](Apis/PingApi.md#getping) | **GET** /ping | Ping method for probing the REST API. |
| *SessionConfigApi* | [**getSessionConfig**](Apis/SessionConfigApi.md#getsessionconfig) | **GET** /session-config | Get the session config |
*SessionConfigApi* | [**setSessionConfig**](Apis/SessionConfigApi.md#setsessionconfig) | **PUT** /session-config | Set the session config |
| *SettingsApi* | [**getLoginPage**](Apis/SettingsApi.md#getloginpage) | **GET** /settings/branding/login-page | Get the login-page settings |
*SettingsApi* | [**getSettings**](Apis/SettingsApi.md#getsettings) | **GET** /settings | Get the general settings |
*SettingsApi* | [**setLoginPage**](Apis/SettingsApi.md#setloginpage) | **PUT** /settings/branding/login-page | Set the login-page settings |
*SettingsApi* | [**setLogo**](Apis/SettingsApi.md#setlogo) | **PUT** /settings/branding/logo | Set the logo |
*SettingsApi* | [**setSettings**](Apis/SettingsApi.md#setsettings) | **PUT** /settings | Set the general settings |
| *TrustedProxiesApi* | [**addTrustedProxy**](Apis/TrustedProxiesApi.md#addtrustedproxy) | **POST** /trusted-proxies | Add a trusted proxy |
*TrustedProxiesApi* | [**getTrustedProxies**](Apis/TrustedProxiesApi.md#gettrustedproxies) | **GET** /trusted-proxies | Get the trusted proxies |
*TrustedProxiesApi* | [**removeTrustedProxy**](Apis/TrustedProxiesApi.md#removetrustedproxy) | **DELETE** /trusted-proxies | Remove a trusted proxy |
*TrustedProxiesApi* | [**setTrustedProxies**](Apis/TrustedProxiesApi.md#settrustedproxies) | **PUT** /trusted-proxies | Set the trusted proxies |
| *UserApi* | [**getUser**](Apis/UserApi.md#getuser) | **GET** /user | Get a user |
*UserApi* | [**setUser**](Apis/UserApi.md#setuser) | **PUT** /user | Update an user |
*UserApi* | [**setUserPassword**](Apis/UserApi.md#setuserpassword) | **PUT** /user/password | Update a user password |


<a name="documentation-for-models"></a>
## Documentation for Models

 - [AbstractDirectoryBean](./Models/AbstractDirectoryBean.md)
 - [ApplicationBean](./Models/ApplicationBean.md)
 - [ApplicationDirectoryMapping](./Models/ApplicationDirectoryMapping.md)
 - [ApplicationLinkBean](./Models/ApplicationLinkBean.md)
 - [DirectoryCrowdAdvanced](./Models/DirectoryCrowdAdvanced.md)
 - [DirectoryCrowdBean](./Models/DirectoryCrowdBean.md)
 - [DirectoryCrowdPermissions](./Models/DirectoryCrowdPermissions.md)
 - [DirectoryCrowdServer](./Models/DirectoryCrowdServer.md)
 - [DirectoryCrowdServerProxy](./Models/DirectoryCrowdServerProxy.md)
 - [DirectoryDelegatingBean](./Models/DirectoryDelegatingBean.md)
 - [DirectoryDelegatingConfiguration](./Models/DirectoryDelegatingConfiguration.md)
 - [DirectoryDelegatingConnector](./Models/DirectoryDelegatingConnector.md)
 - [DirectoryGenericBean](./Models/DirectoryGenericBean.md)
 - [DirectoryInternalAdvanced](./Models/DirectoryInternalAdvanced.md)
 - [DirectoryInternalBean](./Models/DirectoryInternalBean.md)
 - [DirectoryInternalCredentialPolicy](./Models/DirectoryInternalCredentialPolicy.md)
 - [DirectoryLdapBean](./Models/DirectoryLdapBean.md)
 - [DirectoryLdapPermissions](./Models/DirectoryLdapPermissions.md)
 - [DirectoryLdapSchema](./Models/DirectoryLdapSchema.md)
 - [DirectoryLdapServer](./Models/DirectoryLdapServer.md)
 - [DirectoryPermissions](./Models/DirectoryPermissions.md)
 - [ErrorCollection](./Models/ErrorCollection.md)
 - [GroupBean](./Models/GroupBean.md)
 - [LicenseBean](./Models/LicenseBean.md)
 - [MailServerSmtpBean](./Models/MailServerSmtpBean.md)
 - [MailTemplatesBean](./Models/MailTemplatesBean.md)
 - [SessionConfigBean](./Models/SessionConfigBean.md)
 - [SettingsBean](./Models/SettingsBean.md)
 - [SettingsBrandingLoginPageBean](./Models/SettingsBrandingLoginPageBean.md)
 - [UserBean](./Models/UserBean.md)


<a name="documentation-for-authorization"></a>
## Documentation for Authorization

<a name="basicAuth"></a>
### basicAuth

- **Type**: HTTP basic authentication

