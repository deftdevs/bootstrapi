# Documentation for BootstrAPI for Confluence

<a name="documentation-for-api-endpoints"></a>
## Documentation for API Endpoints

All URIs are relative to *https://CONFLUENCE_URL/rest/bootstrapi/1*

| Class | Method | HTTP request | Description |
|------------ | ------------- | ------------- | -------------|
| *AllApi* | [**setAll**](Apis/AllApi.md#setAll) | **PUT** / | _all |
| *ApplicationLinkApi* | [**createApplicationLink**](Apis/ApplicationLinkApi.md#createApplicationLink) | **POST** /application-link | Create an application link |
*ApplicationLinkApi* | [**deleteApplicationLink**](Apis/ApplicationLinkApi.md#deleteApplicationLink) | **DELETE** /application-link/{uuid} | Delete an application link |
*ApplicationLinkApi* | [**getApplicationLink**](Apis/ApplicationLinkApi.md#getApplicationLink) | **GET** /application-link/{uuid} | Get an application link |
*ApplicationLinkApi* | [**updateApplicationLink**](Apis/ApplicationLinkApi.md#updateApplicationLink) | **PUT** /application-link/{uuid} | Update an application link |
| *ApplicationLinksApi* | [**deleteApplicationLinks**](Apis/ApplicationLinksApi.md#deleteApplicationLinks) | **DELETE** /application-links | Delete all application links |
*ApplicationLinksApi* | [**getApplicationLinks**](Apis/ApplicationLinksApi.md#getApplicationLinks) | **GET** /application-links | Get all application links |
*ApplicationLinksApi* | [**setApplicationLinks**](Apis/ApplicationLinksApi.md#setApplicationLinks) | **PUT** /application-links | Set a list of application links |
| *AuthenticationApi* | [**getAuthenticationIdps**](Apis/AuthenticationApi.md#getAuthenticationIdps) | **GET** /authentication/idps | Get all authentication identity providers |
*AuthenticationApi* | [**getAuthenticationSso**](Apis/AuthenticationApi.md#getAuthenticationSso) | **GET** /authentication/sso | Get authentication SSO configuration |
*AuthenticationApi* | [**setAuthenticationIdps**](Apis/AuthenticationApi.md#setAuthenticationIdps) | **PATCH** /authentication/idps | Set all authentication identity providers |
*AuthenticationApi* | [**setAuthenticationSso**](Apis/AuthenticationApi.md#setAuthenticationSso) | **PATCH** /authentication/sso | Set authentication SSO configuration |
| *CachesApi* | [**flushCache**](Apis/CachesApi.md#flushCache) | **POST** /caches/{name}/flush | Flushes a cache |
*CachesApi* | [**getCache**](Apis/CachesApi.md#getCache) | **GET** /caches/{name} | Read cache information for a specified cache |
*CachesApi* | [**getCaches**](Apis/CachesApi.md#getCaches) | **GET** /caches | Read all cache information |
*CachesApi* | [**updateCache**](Apis/CachesApi.md#updateCache) | **PUT** /caches/{name} | Update an existing cache-size. Only Setting maxObjectCount is supported. |
| *DirectoriesApi* | [**deleteDirectories**](Apis/DirectoriesApi.md#deleteDirectories) | **DELETE** /directories | Delete all user directories |
*DirectoriesApi* | [**getDirectories**](Apis/DirectoriesApi.md#getDirectories) | **GET** /directories | Get all user directories |
*DirectoriesApi* | [**setDirectories**](Apis/DirectoriesApi.md#setDirectories) | **PUT** /directories | Set directories mapped by their name. |
| *DirectoryApi* | [**createDirectory**](Apis/DirectoryApi.md#createDirectory) | **POST** /directory | Create a user directory |
*DirectoryApi* | [**deleteDirectory**](Apis/DirectoryApi.md#deleteDirectory) | **DELETE** /directory/{id} | Delete a user directory |
*DirectoryApi* | [**getDirectory**](Apis/DirectoryApi.md#getDirectory) | **GET** /directory/{id} | Get a user directory |
*DirectoryApi* | [**updateDirectory**](Apis/DirectoryApi.md#updateDirectory) | **PUT** /directory/{id} | Update a user directory |
| *LicenseApi* | [**addLicense**](Apis/LicenseApi.md#addLicense) | **POST** /license | Add a license |
| *LicensesApi* | [**getLicenses**](Apis/LicensesApi.md#getLicenses) | **GET** /licenses | Get all licenses information |
*LicensesApi* | [**setLicenses**](Apis/LicensesApi.md#setLicenses) | **PUT** /licenses | Set a list of licenses |
| *MailServerApi* | [**getMailServerPop**](Apis/MailServerApi.md#getMailServerPop) | **GET** /mail-server/pop | Get the default POP mail server |
*MailServerApi* | [**getMailServerSmtp**](Apis/MailServerApi.md#getMailServerSmtp) | **GET** /mail-server/smtp | Get the default SMTP mail server |
*MailServerApi* | [**setMailServerPop**](Apis/MailServerApi.md#setMailServerPop) | **PUT** /mail-server/pop | Set the default POP mail server |
*MailServerApi* | [**setMailServerSmtp**](Apis/MailServerApi.md#setMailServerSmtp) | **PUT** /mail-server/smtp | Set the default SMTP mail server |
| *PermissionsApi* | [**getPermissionGlobal**](Apis/PermissionsApi.md#getPermissionGlobal) | **GET** /permissions/global | Get global permissions configuration |
*PermissionsApi* | [**setPermissionGlobal**](Apis/PermissionsApi.md#setPermissionGlobal) | **PUT** /permissions/global | Set global permissions configuration |
| *PingApi* | [**getPing**](Apis/PingApi.md#getPing) | **GET** /ping | Ping method for probing the REST API. |
| *SettingsApi* | [**getBrandingColorScheme**](Apis/SettingsApi.md#getBrandingColorScheme) | **GET** /settings/branding/color-scheme | Get the color scheme |
*SettingsApi* | [**getBrandingFavicon**](Apis/SettingsApi.md#getBrandingFavicon) | **GET** /settings/branding/favicon | Get the favicon |
*SettingsApi* | [**getBrandingLogo**](Apis/SettingsApi.md#getBrandingLogo) | **GET** /settings/branding/logo | Get the logo |
*SettingsApi* | [**getCustomHtml**](Apis/SettingsApi.md#getCustomHtml) | **GET** /settings/custom-html | Get the custom HTML |
*SettingsApi* | [**getSettings**](Apis/SettingsApi.md#getSettings) | **GET** /settings | Get the general settings |
*SettingsApi* | [**getSettingsSecurity**](Apis/SettingsApi.md#getSettingsSecurity) | **GET** /settings/security | Get the security settings |
*SettingsApi* | [**setBrandingColorScheme**](Apis/SettingsApi.md#setBrandingColorScheme) | **PUT** /settings/branding/color-scheme | Set the color scheme |
*SettingsApi* | [**setBrandingFavicon**](Apis/SettingsApi.md#setBrandingFavicon) | **PUT** /settings/branding/favicon | Set the favicon |
*SettingsApi* | [**setBrandingLogo**](Apis/SettingsApi.md#setBrandingLogo) | **PUT** /settings/branding/logo | Set the logo |
*SettingsApi* | [**setCustomHtml**](Apis/SettingsApi.md#setCustomHtml) | **PUT** /settings/custom-html | Set the custom HTML |
*SettingsApi* | [**setSettings**](Apis/SettingsApi.md#setSettings) | **PUT** /settings | Set the general settings |
*SettingsApi* | [**setSettingsSecurity**](Apis/SettingsApi.md#setSettingsSecurity) | **PUT** /settings/security | Set the security settings |
| *UserApi* | [**getUser**](Apis/UserApi.md#getUser) | **GET** /user | Get a user |
*UserApi* | [**setUser**](Apis/UserApi.md#setUser) | **PUT** /user | Update an user |
*UserApi* | [**setUserPassword**](Apis/UserApi.md#setUserPassword) | **PUT** /user/password | Update a user password |


<a name="documentation-for-models"></a>
## Documentation for Models

 - [AbstractAuthenticationIdpModel](./Models/AbstractAuthenticationIdpModel.md)
 - [AbstractDirectoryModel](./Models/AbstractDirectoryModel.md)
 - [ApplicationLinkModel](./Models/ApplicationLinkModel.md)
 - [AuthenticationIdpOidcModel](./Models/AuthenticationIdpOidcModel.md)
 - [AuthenticationIdpSamlModel](./Models/AuthenticationIdpSamlModel.md)
 - [AuthenticationSsoModel](./Models/AuthenticationSsoModel.md)
 - [CacheModel](./Models/CacheModel.md)
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
 - [MailServerPopModel](./Models/MailServerPopModel.md)
 - [MailServerSmtpModel](./Models/MailServerSmtpModel.md)
 - [PermissionsGlobalModel](./Models/PermissionsGlobalModel.md)
 - [SettingsBrandingColorSchemeModel](./Models/SettingsBrandingColorSchemeModel.md)
 - [SettingsCustomHtmlModel](./Models/SettingsCustomHtmlModel.md)
 - [SettingsGeneralModel](./Models/SettingsGeneralModel.md)
 - [SettingsModel](./Models/SettingsModel.md)
 - [SettingsSecurityModel](./Models/SettingsSecurityModel.md)
 - [UserModel](./Models/UserModel.md)
 - [_AllModel](./Models/_AllModel.md)
 - [_AllModelStatus](./Models/_AllModelStatus.md)


<a name="documentation-for-authorization"></a>
## Documentation for Authorization

<a name="basicAuth"></a>
### basicAuth

- **Type**: HTTP basic authentication

