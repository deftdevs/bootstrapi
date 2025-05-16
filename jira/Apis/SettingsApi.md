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
> SettingsBannerModel getBanner()

Get the banner

### Parameters
This endpoint does not need any parameter.

### Return type

[**SettingsBannerModel**](../Models/SettingsBannerModel.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

<a name="getBrandingColorScheme"></a>
# **getBrandingColorScheme**
> SettingsBrandingColorSchemeModel getBrandingColorScheme()

Get the color scheme

### Parameters
This endpoint does not need any parameter.

### Return type

[**SettingsBrandingColorSchemeModel**](../Models/SettingsBrandingColorSchemeModel.md)

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
> SettingsModel getSettings()

Get the general settings

### Parameters
This endpoint does not need any parameter.

### Return type

[**SettingsModel**](../Models/SettingsModel.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

<a name="getSettingsSecurity"></a>
# **getSettingsSecurity**
> SettingsSecurityModel getSettingsSecurity()

Get the security settings

### Parameters
This endpoint does not need any parameter.

### Return type

[**SettingsSecurityModel**](../Models/SettingsSecurityModel.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

<a name="setBanner"></a>
# **setBanner**
> SettingsBannerModel setBanner(SettingsBannerModel)

Set the banner

### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **SettingsBannerModel** | [**SettingsBannerModel**](../Models/SettingsBannerModel.md)|  | [optional] |

### Return type

[**SettingsBannerModel**](../Models/SettingsBannerModel.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json

<a name="setBrandingColorScheme"></a>
# **setBrandingColorScheme**
> SettingsBrandingColorSchemeModel setBrandingColorScheme(SettingsBrandingColorSchemeModel)

Set the color scheme

### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **SettingsBrandingColorSchemeModel** | [**SettingsBrandingColorSchemeModel**](../Models/SettingsBrandingColorSchemeModel.md)|  | [optional] |

### Return type

[**SettingsBrandingColorSchemeModel**](../Models/SettingsBrandingColorSchemeModel.md)

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
| **body** | **Object**|  | [optional] |

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
| **body** | **Object**|  | [optional] |

### Return type

null (empty response body)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/octet-stream
- **Accept**: application/json

<a name="setSettings"></a>
# **setSettings**
> SettingsModel setSettings(SettingsModel)

Set the general settings

### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **SettingsModel** | [**SettingsModel**](../Models/SettingsModel.md)|  | [optional] |

### Return type

[**SettingsModel**](../Models/SettingsModel.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json

<a name="setSettingsSecurity"></a>
# **setSettingsSecurity**
> SettingsSecurityModel setSettingsSecurity(SettingsSecurityModel)

Set the security settings

### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **SettingsSecurityModel** | [**SettingsSecurityModel**](../Models/SettingsSecurityModel.md)|  | [optional] |

### Return type

[**SettingsSecurityModel**](../Models/SettingsSecurityModel.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json

