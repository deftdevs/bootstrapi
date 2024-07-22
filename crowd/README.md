# Documentation for BootstrAPI for Crowd

<a name="documentation-for-api-endpoints"></a>
## Documentation for API Endpoints

All URIs are relative to *https://<CROWD_URL>/rest/bootstrapi/1*

| Class | Method | HTTP request | Description |
|------------ | ------------- | ------------- | -------------|
| *AllApi* | [**setAll**](Apis/AllApi.md#setall) | **PUT** / | Set the whole configuration |
| *ApplicationLinksApi* | [**addApplicationLink**](Apis/ApplicationLinksApi.md#addapplicationlink) | **POST** /application-links | Add an application link |
*ApplicationLinksApi* | [**deleteApplicationLink**](Apis/ApplicationLinksApi.md#deleteapplicationlink) | **DELETE** /application-links/{uuid} | Delete an application link |
*ApplicationLinksApi* | [**deleteApplicationLinks**](Apis/ApplicationLinksApi.md#deleteapplicationlinks) | **DELETE** /application-links | Delete all application links |
*ApplicationLinksApi* | [**getApplicationLink**](Apis/ApplicationLinksApi.md#getapplicationlink) | **GET** /application-links/{uuid} | Get an application link |
*ApplicationLinksApi* | [**getApplicationLinks**](Apis/ApplicationLinksApi.md#getapplicationlinks) | **GET** /application-links | Get all application links |
*ApplicationLinksApi* | [**setApplicationLink**](Apis/ApplicationLinksApi.md#setapplicationlink) | **PUT** /application-links/{uuid} | Update an application link |
*ApplicationLinksApi* | [**setApplicationLinks**](Apis/ApplicationLinksApi.md#setapplicationlinks) | **PUT** /application-links | Set or update a list of application links |
| *ApplicationsApi* | [**addApplication**](Apis/ApplicationsApi.md#addapplication) | **POST** /applications | Add an application |
*ApplicationsApi* | [**deleteApplication**](Apis/ApplicationsApi.md#deleteapplication) | **DELETE** /applications/{id} | Delete an application |
*ApplicationsApi* | [**deleteApplications**](Apis/ApplicationsApi.md#deleteapplications) | **DELETE** /applications | Delete all applications |
*ApplicationsApi* | [**getApplication**](Apis/ApplicationsApi.md#getapplication) | **GET** /applications/{id} | Get an application |
*ApplicationsApi* | [**getApplications**](Apis/ApplicationsApi.md#getapplications) | **GET** /applications | Get all applications |
*ApplicationsApi* | [**setApplication**](Apis/ApplicationsApi.md#setapplication) | **PUT** /applications/{id} | Update an application |
*ApplicationsApi* | [**setApplications**](Apis/ApplicationsApi.md#setapplications) | **PUT** /applications | Set or update a list of applications |
| *DirectoriesApi* | [**addDirectory**](Apis/DirectoriesApi.md#adddirectory) | **POST** /directories | Add a user directory |
*DirectoriesApi* | [**deleteDirectories**](Apis/DirectoriesApi.md#deletedirectories) | **DELETE** /directories | Delete all user directories |
*DirectoriesApi* | [**deleteDirectory**](Apis/DirectoriesApi.md#deletedirectory) | **DELETE** /directories/{id} | Delete a user directory |
*DirectoriesApi* | [**getDirectories**](Apis/DirectoriesApi.md#getdirectories) | **GET** /directories | Get all user directories |
*DirectoriesApi* | [**getDirectory**](Apis/DirectoriesApi.md#getdirectory) | **GET** /directories/{id} | Get a user directory |
*DirectoriesApi* | [**setDirectories**](Apis/DirectoriesApi.md#setdirectories) | **PUT** /directories | Set or update a list of user directories |
*DirectoriesApi* | [**setDirectory**](Apis/DirectoriesApi.md#setdirectory) | **PUT** /directories/{id} | Update a user directory |
| *GroupsApi* | [**createGroup**](Apis/GroupsApi.md#creategroup) | **POST** /groups | Create a group |
*GroupsApi* | [**getGroup**](Apis/GroupsApi.md#getgroup) | **GET** /groups | Get a group |
*GroupsApi* | [**setGroups**](Apis/GroupsApi.md#setgroups) | **PATCH** /groups | Set groups |
*GroupsApi* | [**updateGroup**](Apis/GroupsApi.md#updategroup) | **PUT** /groups | Update a group |
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
*SettingsApi* | [**getSettings**](Apis/SettingsApi.md#getsettings) | **GET** /settings | Get the application settings |
*SettingsApi* | [**setLoginPage**](Apis/SettingsApi.md#setloginpage) | **PUT** /settings/branding/login-page | Set the login-page settings |
*SettingsApi* | [**setLogo**](Apis/SettingsApi.md#setlogo) | **PUT** /settings/branding/logo | Set the logo |
*SettingsApi* | [**setSettings**](Apis/SettingsApi.md#setsettings) | **PUT** /settings | Set the application settings |
| *TrustedProxiesApi* | [**addTrustedProxy**](Apis/TrustedProxiesApi.md#addtrustedproxy) | **POST** /trusted-proxies | Add a trusted proxy |
*TrustedProxiesApi* | [**getTrustedProxies**](Apis/TrustedProxiesApi.md#gettrustedproxies) | **GET** /trusted-proxies | Get the trusted proxies |
*TrustedProxiesApi* | [**removeTrustedProxy**](Apis/TrustedProxiesApi.md#removetrustedproxy) | **DELETE** /trusted-proxies | Remove a trusted proxy |
*TrustedProxiesApi* | [**setTrustedProxies**](Apis/TrustedProxiesApi.md#settrustedproxies) | **PUT** /trusted-proxies | Set the trusted proxies |
| *UsersApi* | [**getUser**](Apis/UsersApi.md#getuser) | **GET** /users | Get a user |
*UsersApi* | [**setUser**](Apis/UsersApi.md#setuser) | **PUT** /users | Update an user |
*UsersApi* | [**setUserPassword**](Apis/UsersApi.md#setuserpassword) | **PUT** /users/password | Update a user password |


<a name="documentation-for-models"></a>
## Documentation for Models

 - [AbstractDirectoryBean](./Models/AbstractDirectoryBean.md)
 - [AllBean](./Models/AllBean.md)
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
 - [TrustedProxiesBean](./Models/TrustedProxiesBean.md)
 - [UserBean](./Models/UserBean.md)


<a name="documentation-for-authorization"></a>
## Documentation for Authorization

<a name="basicAuth"></a>
### basicAuth

- **Type**: HTTP basic authentication

