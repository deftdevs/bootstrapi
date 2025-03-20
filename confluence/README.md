# Documentation for BootstrAPI for Confluence

<a name="documentation-for-api-endpoints"></a>
## Documentation for API Endpoints

All URIs are relative to *https://<CONFLUENCE_URL>/rest/bootstrapi/1*

| Class | Method | HTTP request | Description |
|------------ | ------------- | ------------- | -------------|
| *ApplicationLinkApi* | [**createApplicationLink**](Apis/ApplicationLinkApi.md#createapplicationlink) | **POST** /application-link | Create an application link |
*ApplicationLinkApi* | [**deleteApplicationLink**](Apis/ApplicationLinkApi.md#deleteapplicationlink) | **DELETE** /application-link/{uuid} | Delete an application link |
*ApplicationLinkApi* | [**getApplicationLink**](Apis/ApplicationLinkApi.md#getapplicationlink) | **GET** /application-link/{uuid} | Get an application link |
*ApplicationLinkApi* | [**updateApplicationLink**](Apis/ApplicationLinkApi.md#updateapplicationlink) | **PUT** /application-link/{uuid} | Update an application link |
| *ApplicationLinksApi* | [**deleteApplicationLinks**](Apis/ApplicationLinksApi.md#deleteapplicationlinks) | **DELETE** /application-links | Delete all application links |
*ApplicationLinksApi* | [**getApplicationLinks**](Apis/ApplicationLinksApi.md#getapplicationlinks) | **GET** /application-links | Get all application links |
*ApplicationLinksApi* | [**setApplicationLinks**](Apis/ApplicationLinksApi.md#setapplicationlinks) | **PUT** /application-links | Set a list of application links |
| *AuthenticationApi* | [**getAuthenticationIdps**](Apis/AuthenticationApi.md#getauthenticationidps) | **GET** /authentication/idps | Get all authentication identity providers |
*AuthenticationApi* | [**getAuthenticationSso**](Apis/AuthenticationApi.md#getauthenticationsso) | **GET** /authentication/sso | Get authentication SSO configuration |
*AuthenticationApi* | [**setAuthenticationIdps**](Apis/AuthenticationApi.md#setauthenticationidps) | **PATCH** /authentication/idps | Set all authentication identity providers |
*AuthenticationApi* | [**setAuthenticationSso**](Apis/AuthenticationApi.md#setauthenticationsso) | **PATCH** /authentication/sso | Set authentication SSO configuration |
| *CachesApi* | [**flushCache**](Apis/CachesApi.md#flushcache) | **POST** /caches/{name}/flush | Flushes a cache |
*CachesApi* | [**getCache**](Apis/CachesApi.md#getcache) | **GET** /caches/{name} | Read cache information for a specified cache |
*CachesApi* | [**getCaches**](Apis/CachesApi.md#getcaches) | **GET** /caches | Read all cache information |
*CachesApi* | [**updateCache**](Apis/CachesApi.md#updatecache) | **PUT** /caches/{name} | Update an existing cache-size. Only Setting maxObjectCount is supported. |
| *DirectoriesApi* | [**deleteDirectories**](Apis/DirectoriesApi.md#deletedirectories) | **DELETE** /directories | Delete all user directories |
*DirectoriesApi* | [**getDirectories**](Apis/DirectoriesApi.md#getdirectories) | **GET** /directories | Get all user directories |
*DirectoriesApi* | [**setDirectories**](Apis/DirectoriesApi.md#setdirectories) | **PUT** /directories | Set a list of user directories |
| *DirectoryApi* | [**createDirectory**](Apis/DirectoryApi.md#createdirectory) | **POST** /directory | Create a user directory |
*DirectoryApi* | [**deleteDirectory**](Apis/DirectoryApi.md#deletedirectory) | **DELETE** /directory/{id} | Delete a user directory |
*DirectoryApi* | [**getDirectory**](Apis/DirectoryApi.md#getdirectory) | **GET** /directory/{id} | Get a user directory |
*DirectoryApi* | [**updateDirectory**](Apis/DirectoryApi.md#updatedirectory) | **PUT** /directory/{id} | Update a user directory |
| *GadgetApi* | [**createGadget**](Apis/GadgetApi.md#creategadget) | **POST** /gadget | Add a gadget |
*GadgetApi* | [**deleteGadget**](Apis/GadgetApi.md#deletegadget) | **DELETE** /gadget/{id} | Delete a gadget |
*GadgetApi* | [**getGadget**](Apis/GadgetApi.md#getgadget) | **GET** /gadget/{id} | Get a gadget |
*GadgetApi* | [**updateGadget**](Apis/GadgetApi.md#updategadget) | **PUT** /gadget/{id} | Update a gadget |
| *GadgetsApi* | [**deleteGadgets**](Apis/GadgetsApi.md#deletegadgets) | **DELETE** /gadgets | Delete all gadgets |
*GadgetsApi* | [**getGadgets**](Apis/GadgetsApi.md#getgadgets) | **GET** /gadgets | Get all gadgets |
*GadgetsApi* | [**setGadgets**](Apis/GadgetsApi.md#setgadgets) | **PUT** /gadgets | Set a list of gadgets |
| *LicenseApi* | [**addLicense**](Apis/LicenseApi.md#addlicense) | **POST** /license | Add a license |
| *LicensesApi* | [**getLicenses**](Apis/LicensesApi.md#getlicenses) | **GET** /licenses | Get all licenses information |
*LicensesApi* | [**setLicenses**](Apis/LicensesApi.md#setlicenses) | **PUT** /licenses | Set a list of licenses |
| *MailServerApi* | [**getMailServerPop**](Apis/MailServerApi.md#getmailserverpop) | **GET** /mail-server/pop | Get the default POP mail server |
*MailServerApi* | [**getMailServerSmtp**](Apis/MailServerApi.md#getmailserversmtp) | **GET** /mail-server/smtp | Get the default SMTP mail server |
*MailServerApi* | [**setMailServerPop**](Apis/MailServerApi.md#setmailserverpop) | **PUT** /mail-server/pop | Set the default POP mail server |
*MailServerApi* | [**setMailServerSmtp**](Apis/MailServerApi.md#setmailserversmtp) | **PUT** /mail-server/smtp | Set the default SMTP mail server |
| *PermissionsApi* | [**getPermissionGlobal**](Apis/PermissionsApi.md#getpermissionglobal) | **GET** /permissions/global | Get global permissions configuration |
*PermissionsApi* | [**setPermissionGlobal**](Apis/PermissionsApi.md#setpermissionglobal) | **PUT** /permissions/global | Set global permissions configuration |
| *PingApi* | [**getPing**](Apis/PingApi.md#getping) | **GET** /ping | Ping method for probing the REST API. |
| *SettingsApi* | [**getBrandingColorScheme**](Apis/SettingsApi.md#getbrandingcolorscheme) | **GET** /settings/branding/color-scheme | Get the color scheme |
*SettingsApi* | [**getBrandingFavicon**](Apis/SettingsApi.md#getbrandingfavicon) | **GET** /settings/branding/favicon | Get the favicon |
*SettingsApi* | [**getBrandingLogo**](Apis/SettingsApi.md#getbrandinglogo) | **GET** /settings/branding/logo | Get the logo |
*SettingsApi* | [**getCustomHtml**](Apis/SettingsApi.md#getcustomhtml) | **GET** /settings/custom-html | Get the custom HTML |
*SettingsApi* | [**getSettings**](Apis/SettingsApi.md#getsettings) | **GET** /settings | Get the general settings |
*SettingsApi* | [**getSettingsSecurity**](Apis/SettingsApi.md#getsettingssecurity) | **GET** /settings/security | Get the security settings |
*SettingsApi* | [**setBrandingColorScheme**](Apis/SettingsApi.md#setbrandingcolorscheme) | **PUT** /settings/branding/color-scheme | Set the color scheme |
*SettingsApi* | [**setBrandingFavicon**](Apis/SettingsApi.md#setbrandingfavicon) | **PUT** /settings/branding/favicon | Set the favicon |
*SettingsApi* | [**setBrandingLogo**](Apis/SettingsApi.md#setbrandinglogo) | **PUT** /settings/branding/logo | Set the logo |
*SettingsApi* | [**setCustomHtml**](Apis/SettingsApi.md#setcustomhtml) | **PUT** /settings/custom-html | Set the custom HTML |
*SettingsApi* | [**setSettings**](Apis/SettingsApi.md#setsettings) | **PUT** /settings | Set the general settings |
*SettingsApi* | [**setSettingsSecurity**](Apis/SettingsApi.md#setsettingssecurity) | **PUT** /settings/security | Set the security settings |
| *UserApi* | [**getUser**](Apis/UserApi.md#getuser) | **GET** /user | Get a user |
*UserApi* | [**setUser**](Apis/UserApi.md#setuser) | **PUT** /user | Update an user |
*UserApi* | [**setUserPassword**](Apis/UserApi.md#setuserpassword) | **PUT** /user/password | Update a user password |


<a name="documentation-for-models"></a>
## Documentation for Models

 - [AbstractAuthenticationIdpBean](./Models/AbstractAuthenticationIdpBean.md)
 - [AbstractDirectoryBean](./Models/AbstractDirectoryBean.md)
 - [ApplicationLinkBean](./Models/ApplicationLinkBean.md)
 - [AuthenticationIdpOidcBean](./Models/AuthenticationIdpOidcBean.md)
 - [AuthenticationIdpSamlBean](./Models/AuthenticationIdpSamlBean.md)
 - [AuthenticationSsoBean](./Models/AuthenticationSsoBean.md)
 - [CacheBean](./Models/CacheBean.md)
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
 - [GadgetBean](./Models/GadgetBean.md)
 - [GroupBean](./Models/GroupBean.md)
 - [LicenseBean](./Models/LicenseBean.md)
 - [MailServerPopBean](./Models/MailServerPopBean.md)
 - [MailServerSmtpBean](./Models/MailServerSmtpBean.md)
 - [PermissionsGlobalBean](./Models/PermissionsGlobalBean.md)
 - [SettingsBean](./Models/SettingsBean.md)
 - [SettingsBrandingColorSchemeBean](./Models/SettingsBrandingColorSchemeBean.md)
 - [SettingsCustomHtmlBean](./Models/SettingsCustomHtmlBean.md)
 - [SettingsSecurityBean](./Models/SettingsSecurityBean.md)
 - [UserBean](./Models/UserBean.md)


<a name="documentation-for-authorization"></a>
## Documentation for Authorization

<a name="basicAuth"></a>
### basicAuth

- **Type**: HTTP basic authentication

