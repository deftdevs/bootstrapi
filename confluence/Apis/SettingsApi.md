# SettingsApi

All URIs are relative to *https://&lt;CONFLUENCE_URL&gt;/rest/bootstrapi/1*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**getBrandingColorScheme**](SettingsApi.md#getBrandingColorScheme) | **GET** /settings/branding/color-scheme | Get the color scheme |
| [**getBrandingFavicon**](SettingsApi.md#getBrandingFavicon) | **GET** /settings/branding/favicon | Get the favicon |
| [**getBrandingLogo**](SettingsApi.md#getBrandingLogo) | **GET** /settings/branding/logo | Get the logo |
| [**getSettings**](SettingsApi.md#getSettings) | **GET** /settings | Get the application settings |
| [**setBrandingColorScheme**](SettingsApi.md#setBrandingColorScheme) | **PUT** /settings/branding/color-scheme | Set the color scheme |
| [**setBrandingFavicon**](SettingsApi.md#setBrandingFavicon) | **PUT** /settings/branding/favicon | Set the favicon |
| [**setBrandingLogo**](SettingsApi.md#setBrandingLogo) | **PUT** /settings/branding/logo | Set the logo |
| [**setSettings**](SettingsApi.md#setSettings) | **PUT** /settings | Set the application settings |


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

Get the application settings

### Parameters
This endpoint does not need any parameter.

### Return type

[**SettingsBean**](../Models/SettingsBean.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
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

Set the application settings

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

