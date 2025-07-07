# SettingsApi

All URIs are relative to *https://&lt;JIRA_URL&gt;/rest/bootstrapi/1*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**getBanner**](SettingsApi.md#getBanner) | **GET** /settings/banner | Get the banner |
| [**getSettings**](SettingsApi.md#getSettings) | **GET** /settings | Get the general settings |
| [**getSettingsSecurity**](SettingsApi.md#getSettingsSecurity) | **GET** /settings/security | Get the security settings |
| [**setBanner**](SettingsApi.md#setBanner) | **PUT** /settings/banner | Set the banner |
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

