# SettingsApi

All URIs are relative to *https://&lt;JIRA_URL&gt;/rest/bootstrapi/1*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**getBanner**](SettingsApi.md#getBanner) | **GET** /settings/banner | Get the banner |
| [**getBrandingColorScheme**](SettingsApi.md#getBrandingColorScheme) | **GET** /settings/branding/color-scheme | Get the color scheme |
| [**getBrandingFavicon**](SettingsApi.md#getBrandingFavicon) | **GET** /settings/branding/favicon | Get the favicon |
| [**getBrandingLogo**](SettingsApi.md#getBrandingLogo) | **GET** /settings/branding/logo | Get the logo |
| [**getSettings**](SettingsApi.md#getSettings) | **GET** /settings | Get the general settings |
| [**getSettingsSecurity**](SettingsApi.md#getSettingsSecurity) | **GET** /settings/security | Get the security settings |
| [**setBanner**](SettingsApi.md#setBanner) | **PUT** /settings/banner | Set the banner |
| [**setBrandingColorScheme**](SettingsApi.md#setBrandingColorScheme) | **PUT** /settings/branding/color-scheme | Set the color scheme |
| [**setBrandingFavicon**](SettingsApi.md#setBrandingFavicon) | **PUT** /settings/branding/favicon | Set the favicon |
| [**setBrandingLogo**](SettingsApi.md#setBrandingLogo) | **PUT** /settings/branding/logo | Set the logo |
| [**setSettings**](SettingsApi.md#setSettings) | **PUT** /settings | Set the general settings |
| [**setSettingsSecurity**](SettingsApi.md#setSettingsSecurity) | **PUT** /settings/security | Set the security settings |


<a name="getBanner"></a>
# **getBanner**
> SettingsBannerBean getBanner()

Get the banner

### Parameters
This endpoint does not need any parameter.

### Return type

[**SettingsBannerBean**](../Models/SettingsBannerBean.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

<a name="getBrandingColorScheme"></a>
# **getBrandingColorScheme**
> SettingsBrandingColorSchemeBean getBrandingColorScheme()

Get the color scheme

### Parameters
This endpoint does not need any parameter.

### Return type

[**SettingsBrandingColorSchemeBean**](../Models/SettingsBrandingColorSchemeBean.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

<a name="getBrandingFavicon"></a>
# **getBrandingFavicon**
> Object getBrandingFavicon()

Get the favicon

### Parameters
This endpoint does not need any parameter.

### Return type

**Object**

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/octet-stream

<a name="getBrandingLogo"></a>
# **getBrandingLogo**
> Object getBrandingLogo()

Get the logo

### Parameters
This endpoint does not need any parameter.

### Return type

**Object**

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/octet-stream

<a name="getSettings"></a>
# **getSettings**
> SettingsBean getSettings()

Get the general settings

### Parameters
This endpoint does not need any parameter.

### Return type

[**SettingsBean**](../Models/SettingsBean.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

<a name="getSettingsSecurity"></a>
# **getSettingsSecurity**
> SettingsSecurityBean getSettingsSecurity()

Get the security settings

### Parameters
This endpoint does not need any parameter.

### Return type

[**SettingsSecurityBean**](../Models/SettingsSecurityBean.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

<a name="setBanner"></a>
# **setBanner**
> SettingsBannerBean setBanner(SettingsBannerBean)

Set the banner

### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **SettingsBannerBean** | [**SettingsBannerBean**](../Models/SettingsBannerBean.md)|  | |

### Return type

[**SettingsBannerBean**](../Models/SettingsBannerBean.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json

<a name="setBrandingColorScheme"></a>
# **setBrandingColorScheme**
> SettingsBrandingColorSchemeBean setBrandingColorScheme(SettingsBrandingColorSchemeBean)

Set the color scheme

### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **SettingsBrandingColorSchemeBean** | [**SettingsBrandingColorSchemeBean**](../Models/SettingsBrandingColorSchemeBean.md)|  | |

### Return type

[**SettingsBrandingColorSchemeBean**](../Models/SettingsBrandingColorSchemeBean.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json

<a name="setBrandingFavicon"></a>
# **setBrandingFavicon**
> setBrandingFavicon(body)

Set the favicon

### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **body** | **Object**|  | |

### Return type

null (empty response body)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/octet-stream
- **Accept**: application/json

<a name="setBrandingLogo"></a>
# **setBrandingLogo**
> setBrandingLogo(body)

Set the logo

### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **body** | **Object**|  | |

### Return type

null (empty response body)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/octet-stream
- **Accept**: application/json

<a name="setSettings"></a>
# **setSettings**
> SettingsBean setSettings(SettingsBean)

Set the general settings

### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **SettingsBean** | [**SettingsBean**](../Models/SettingsBean.md)|  | |

### Return type

[**SettingsBean**](../Models/SettingsBean.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json

<a name="setSettingsSecurity"></a>
# **setSettingsSecurity**
> SettingsSecurityBean setSettingsSecurity(SettingsSecurityBean)

Set the security settings

### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **SettingsSecurityBean** | [**SettingsSecurityBean**](../Models/SettingsSecurityBean.md)|  | |

### Return type

[**SettingsSecurityBean**](../Models/SettingsSecurityBean.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json

