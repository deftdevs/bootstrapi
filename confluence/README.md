# Documentation for BootstrAPI for Confluence

<a name="documentation-for-api-endpoints"></a>
## Documentation for API Endpoints

All URIs are relative to *https://<CONFLUENCE_URL>/rest/bootstrapi/1*

| Class | Method | HTTP request | Description |
|------------ | ------------- | ------------- | -------------|
| *ApplicationLinksApi* | [**addApplicationLink**](Apis/ApplicationLinksApi.md#addapplicationlink) | **POST** /application-links | Add an application link |
*ApplicationLinksApi* | [**deleteApplicationLink**](Apis/ApplicationLinksApi.md#deleteapplicationlink) | **DELETE** /application-links/{uuid} | Delete an application link |
*ApplicationLinksApi* | [**deleteApplicationLinks**](Apis/ApplicationLinksApi.md#deleteapplicationlinks) | **DELETE** /application-links | Delete all application links |
*ApplicationLinksApi* | [**getApplicationLink**](Apis/ApplicationLinksApi.md#getapplicationlink) | **GET** /application-links/{uuid} | Get an application link |
*ApplicationLinksApi* | [**getApplicationLinks**](Apis/ApplicationLinksApi.md#getapplicationlinks) | **GET** /application-links | Get all application links |
*ApplicationLinksApi* | [**setApplicationLink**](Apis/ApplicationLinksApi.md#setapplicationlink) | **PUT** /application-links/{uuid} | Update an application link |
*ApplicationLinksApi* | [**setApplicationLinks**](Apis/ApplicationLinksApi.md#setapplicationlinks) | **PUT** /application-links | Set or update a list of application links |
| *AuthenticationApi* | [**getAuthenticationIdps**](Apis/AuthenticationApi.md#getauthenticationidps) | **GET** /authentication/idps | Get all authentication identity providers |
*AuthenticationApi* | [**getAuthenticationSso**](Apis/AuthenticationApi.md#getauthenticationsso) | **GET** /authentication/sso | Get authentication SSO configuration |
*AuthenticationApi* | [**setAuthenticationIdps**](Apis/AuthenticationApi.md#setauthenticationidps) | **PATCH** /authentication/idps | Set all authentication identity providers |
*AuthenticationApi* | [**setAuthenticationSso**](Apis/AuthenticationApi.md#setauthenticationsso) | **PATCH** /authentication/sso | Set authentication SSO configuration |
| *CacheApi* | [**flushCache**](Apis/CacheApi.md#flushcache) | **POST** /caches/{name}/flush | Flushes a cache |
*CacheApi* | [**getCache**](Apis/CacheApi.md#getcache) | **GET** /caches/{name} | Read cache information for a specified cache |
*CacheApi* | [**getCaches**](Apis/CacheApi.md#getcaches) | **GET** /caches | Read all cache information |
*CacheApi* | [**updateCache**](Apis/CacheApi.md#updatecache) | **PUT** /caches/{name} | Update an existing cache-size. Only Setting maxObjectCount is supported. |
| *DirectoriesApi* | [**addDirectory**](Apis/DirectoriesApi.md#adddirectory) | **POST** /directories | Add a user directory |
*DirectoriesApi* | [**deleteDirectories**](Apis/DirectoriesApi.md#deletedirectories) | **DELETE** /directories | Delete all user directories |
*DirectoriesApi* | [**deleteDirectory**](Apis/DirectoriesApi.md#deletedirectory) | **DELETE** /directories/{id} | Delete a user directory |
*DirectoriesApi* | [**getDirectories**](Apis/DirectoriesApi.md#getdirectories) | **GET** /directories | Get all user directories |
*DirectoriesApi* | [**getDirectory**](Apis/DirectoriesApi.md#getdirectory) | **GET** /directories/{id} | Get a user directory |
*DirectoriesApi* | [**setDirectories**](Apis/DirectoriesApi.md#setdirectories) | **PUT** /directories | Set or update a list of user directories |
*DirectoriesApi* | [**setDirectory**](Apis/DirectoriesApi.md#setdirectory) | **PUT** /directories/{id} | Update a user directory |
| *GadgetsApi* | [**addGadget**](Apis/GadgetsApi.md#addgadget) | **POST** /gadgets | Add a gadget |
*GadgetsApi* | [**deleteGadget**](Apis/GadgetsApi.md#deletegadget) | **DELETE** /gadgets/{id} | Delete a gadget |
*GadgetsApi* | [**deleteGadgets**](Apis/GadgetsApi.md#deletegadgets) | **DELETE** /gadgets | Delete all gadgets |
*GadgetsApi* | [**getGadget**](Apis/GadgetsApi.md#getgadget) | **GET** /gadgets/{id} | Get a gadget |
*GadgetsApi* | [**getGadgets**](Apis/GadgetsApi.md#getgadgets) | **GET** /gadgets | Get all gadgets |
*GadgetsApi* | [**setGadget**](Apis/GadgetsApi.md#setgadget) | **PUT** /gadgets/{id} | Update a gadget |
*GadgetsApi* | [**setGadgets**](Apis/GadgetsApi.md#setgadgets) | **PUT** /gadgets | Set or update a list of gadgets |
| *LicensesApi* | [**addLicense**](Apis/LicensesApi.md#addlicense) | **POST** /licenses | Add a license |
*LicensesApi* | [**getLicenses**](Apis/LicensesApi.md#getlicenses) | **GET** /licenses | Get all licenses information |
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
*SettingsApi* | [**getSettings**](Apis/SettingsApi.md#getsettings) | **GET** /settings | Get the application settings |
*SettingsApi* | [**setBrandingColorScheme**](Apis/SettingsApi.md#setbrandingcolorscheme) | **PUT** /settings/branding/color-scheme | Set the color scheme |
*SettingsApi* | [**setBrandingFavicon**](Apis/SettingsApi.md#setbrandingfavicon) | **PUT** /settings/branding/favicon | Set the favicon |
*SettingsApi* | [**setBrandingLogo**](Apis/SettingsApi.md#setbrandinglogo) | **PUT** /settings/branding/logo | Set the logo |
*SettingsApi* | [**setSettings**](Apis/SettingsApi.md#setsettings) | **PUT** /settings | Set the application settings |
| *UsersApi* | [**getUser**](Apis/UsersApi.md#getuser) | **GET** /users | Get a user |
*UsersApi* | [**setUser**](Apis/UsersApi.md#setuser) | **PUT** /users | Update an user |
*UsersApi* | [**setUserPassword**](Apis/UsersApi.md#setuserpassword) | **PUT** /users/password | Update a user password |


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
 - [UserBean](./Models/UserBean.md)


<a name="documentation-for-authorization"></a>
## Documentation for Authorization

<a name="basicAuth"></a>
### basicAuth

- **Type**: HTTP basic authentication

