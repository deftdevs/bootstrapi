# SettingsApi

All URIs are relative to *https://&lt;CONFLUENCE_URL&gt;/rest/bootstrapi/1*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**getCustomHtml**](SettingsApi.md#getCustomHtml) | **GET** /settings/custom-html | Get the custom HTML |
| [**getSettings**](SettingsApi.md#getSettings) | **GET** /settings | Get the general settings |
| [**getSettingsSecurity**](SettingsApi.md#getSettingsSecurity) | **GET** /settings/security | Get the security settings |
| [**setCustomHtml**](SettingsApi.md#setCustomHtml) | **PUT** /settings/custom-html | Set the custom HTML |
| [**setSettings**](SettingsApi.md#setSettings) | **PUT** /settings | Set the general settings |
| [**setSettingsSecurity**](SettingsApi.md#setSettingsSecurity) | **PUT** /settings/security | Set the security settings |


<a name="getCustomHtml"></a>
# **getCustomHtml**
> SettingsCustomHtmlBean getCustomHtml()

Get the custom HTML

### Parameters
This endpoint does not need any parameter.

### Return type

[**SettingsCustomHtmlBean**](../Models/SettingsCustomHtmlBean.md)

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

<a name="setCustomHtml"></a>
# **setCustomHtml**
> SettingsCustomHtmlBean setCustomHtml(SettingsCustomHtmlBean)

Set the custom HTML

### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **SettingsCustomHtmlBean** | [**SettingsCustomHtmlBean**](../Models/SettingsCustomHtmlBean.md)|  | |

### Return type

[**SettingsCustomHtmlBean**](../Models/SettingsCustomHtmlBean.md)

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

