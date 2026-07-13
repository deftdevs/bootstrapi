# SettingsApi

All URIs are relative to *https://CONFLUENCE_URL/rest/bootstrapi/1*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**getSettings**](SettingsApi.md#getSettings) | **GET** /settings | Get all settings |
| [**getSettingsBrandingColorScheme**](SettingsApi.md#getSettingsBrandingColorScheme) | **GET** /settings/branding/color-scheme | Get the color scheme |
| [**getSettingsBrandingCustomHtml**](SettingsApi.md#getSettingsBrandingCustomHtml) | **GET** /settings/branding/custom-html | Get the custom HTML |
| [**getSettingsBrandingFavicon**](SettingsApi.md#getSettingsBrandingFavicon) | **GET** /settings/branding/favicon | Get the favicon |
| [**getSettingsBrandingLogo**](SettingsApi.md#getSettingsBrandingLogo) | **GET** /settings/branding/logo | Get the logo |
| [**getSettingsGeneral**](SettingsApi.md#getSettingsGeneral) | **GET** /settings/general | Get the general settings |
| [**getSettingsSecurity**](SettingsApi.md#getSettingsSecurity) | **GET** /settings/security | Get the security settings |
| [**setSettings**](SettingsApi.md#setSettings) | **PUT** /settings | Apply a settings configuration |
| [**setSettingsBrandingColorScheme**](SettingsApi.md#setSettingsBrandingColorScheme) | **PUT** /settings/branding/color-scheme | Set the color scheme |
| [**setSettingsBrandingCustomHtml**](SettingsApi.md#setSettingsBrandingCustomHtml) | **PUT** /settings/branding/custom-html | Set the custom HTML |
| [**setSettingsBrandingFavicon**](SettingsApi.md#setSettingsBrandingFavicon) | **PUT** /settings/branding/favicon | Set the favicon |
| [**setSettingsBrandingLogo**](SettingsApi.md#setSettingsBrandingLogo) | **PUT** /settings/branding/logo | Set the logo |
| [**setSettingsGeneral**](SettingsApi.md#setSettingsGeneral) | **PUT** /settings/general | Set the general settings |
| [**setSettingsSecurity**](SettingsApi.md#setSettingsSecurity) | **PUT** /settings/security | Set the security settings |


<a name="getSettings"></a>
# **getSettings**
> SettingsModel getSettings()

Get all settings

### Parameters
This endpoint does not need any parameter.

### Return type

[**SettingsModel**](../Models/SettingsModel.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json, application/yaml, application/x-yaml, text/yaml

<a name="getSettingsBrandingColorScheme"></a>
# **getSettingsBrandingColorScheme**
> SettingsBrandingColorSchemeModel getSettingsBrandingColorScheme()

Get the color scheme

### Parameters
This endpoint does not need any parameter.

### Return type

[**SettingsBrandingColorSchemeModel**](../Models/SettingsBrandingColorSchemeModel.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json, application/yaml, application/x-yaml, text/yaml

<a name="getSettingsBrandingCustomHtml"></a>
# **getSettingsBrandingCustomHtml**
> SettingsBrandingCustomHtmlModel getSettingsBrandingCustomHtml()

Get the custom HTML

### Parameters
This endpoint does not need any parameter.

### Return type

[**SettingsBrandingCustomHtmlModel**](../Models/SettingsBrandingCustomHtmlModel.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json, application/yaml, application/x-yaml, text/yaml

<a name="getSettingsBrandingFavicon"></a>
# **getSettingsBrandingFavicon**
> Object getSettingsBrandingFavicon()

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

<a name="getSettingsBrandingLogo"></a>
# **getSettingsBrandingLogo**
> Object getSettingsBrandingLogo()

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

<a name="getSettingsGeneral"></a>
# **getSettingsGeneral**
> SettingsGeneralModel getSettingsGeneral()

Get the general settings

### Parameters
This endpoint does not need any parameter.

### Return type

[**SettingsGeneralModel**](../Models/SettingsGeneralModel.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json, application/yaml, application/x-yaml, text/yaml

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
- **Accept**: application/json, application/yaml, application/x-yaml, text/yaml

<a name="setSettings"></a>
# **setSettings**
> SettingsModel setSettings(SettingsModel)

Apply a settings configuration

    Returns the updated settings. The per-sub-field outcome is reported in the &#39;status&#39; map, keyed by the request&#39;s field paths (e.g. &#39;general&#39;, &#39;branding/colorScheme&#39;). If any sub-field fails, the highest sub-field status code is returned with the same response body.

### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **SettingsModel** | [**SettingsModel**](../Models/SettingsModel.md)|  | |

### Return type

[**SettingsModel**](../Models/SettingsModel.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json, application/yaml, application/x-yaml, text/yaml
- **Accept**: application/json, application/yaml, application/x-yaml, text/yaml

<a name="setSettingsBrandingColorScheme"></a>
# **setSettingsBrandingColorScheme**
> SettingsBrandingColorSchemeModel setSettingsBrandingColorScheme(SettingsBrandingColorSchemeModel)

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

- **Content-Type**: application/json, application/yaml, application/x-yaml, text/yaml
- **Accept**: application/json, application/yaml, application/x-yaml, text/yaml

<a name="setSettingsBrandingCustomHtml"></a>
# **setSettingsBrandingCustomHtml**
> SettingsBrandingCustomHtmlModel setSettingsBrandingCustomHtml(SettingsBrandingCustomHtmlModel)

Set the custom HTML

### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **SettingsBrandingCustomHtmlModel** | [**SettingsBrandingCustomHtmlModel**](../Models/SettingsBrandingCustomHtmlModel.md)|  | [optional] |

### Return type

[**SettingsBrandingCustomHtmlModel**](../Models/SettingsBrandingCustomHtmlModel.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json, application/yaml, application/x-yaml, text/yaml
- **Accept**: application/json, application/yaml, application/x-yaml, text/yaml

<a name="setSettingsBrandingFavicon"></a>
# **setSettingsBrandingFavicon**
> setSettingsBrandingFavicon(body)

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
- **Accept**: application/json, application/yaml, application/x-yaml, text/yaml

<a name="setSettingsBrandingLogo"></a>
# **setSettingsBrandingLogo**
> setSettingsBrandingLogo(body)

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
- **Accept**: application/json, application/yaml, application/x-yaml, text/yaml

<a name="setSettingsGeneral"></a>
# **setSettingsGeneral**
> SettingsGeneralModel setSettingsGeneral(SettingsGeneralModel)

Set the general settings

### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **SettingsGeneralModel** | [**SettingsGeneralModel**](../Models/SettingsGeneralModel.md)|  | [optional] |

### Return type

[**SettingsGeneralModel**](../Models/SettingsGeneralModel.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json, application/yaml, application/x-yaml, text/yaml
- **Accept**: application/json, application/yaml, application/x-yaml, text/yaml

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

- **Content-Type**: application/json, application/yaml, application/x-yaml, text/yaml
- **Accept**: application/json, application/yaml, application/x-yaml, text/yaml

