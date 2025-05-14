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

