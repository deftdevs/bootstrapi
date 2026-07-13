# SettingsApi

All URIs are relative to *https://JIRA_URL/rest/bootstrapi/1*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**getSettings**](SettingsApi.md#getSettings) | **GET** /settings | Get all settings |
| [**getSettingsBrandingBanner**](SettingsApi.md#getSettingsBrandingBanner) | **GET** /settings/branding/banner | Get the banner |
| [**getSettingsGeneral**](SettingsApi.md#getSettingsGeneral) | **GET** /settings/general | Get the general settings |
| [**getSettingsSecurity**](SettingsApi.md#getSettingsSecurity) | **GET** /settings/security | Get the security settings |
| [**setSettings**](SettingsApi.md#setSettings) | **PUT** /settings | Apply a settings configuration |
| [**setSettingsBrandingBanner**](SettingsApi.md#setSettingsBrandingBanner) | **PUT** /settings/branding/banner | Set the banner |
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

<a name="getSettingsBrandingBanner"></a>
# **getSettingsBrandingBanner**
> SettingsBrandingBannerModel getSettingsBrandingBanner()

Get the banner

### Parameters
This endpoint does not need any parameter.

### Return type

[**SettingsBrandingBannerModel**](../Models/SettingsBrandingBannerModel.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json, application/yaml, application/x-yaml, text/yaml

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

<a name="setSettingsBrandingBanner"></a>
# **setSettingsBrandingBanner**
> SettingsBrandingBannerModel setSettingsBrandingBanner(SettingsBrandingBannerModel)

Set the banner

### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **SettingsBrandingBannerModel** | [**SettingsBrandingBannerModel**](../Models/SettingsBrandingBannerModel.md)|  | [optional] |

### Return type

[**SettingsBrandingBannerModel**](../Models/SettingsBrandingBannerModel.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json, application/yaml, application/x-yaml, text/yaml
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

