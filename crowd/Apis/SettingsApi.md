# SettingsApi

All URIs are relative to *https://CROWD_URL/rest/bootstrapi/1*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**getSettings**](SettingsApi.md#getSettings) | **GET** /settings | Get all settings |
| [**getSettingsBrandingLoginPage**](SettingsApi.md#getSettingsBrandingLoginPage) | **GET** /settings/branding/login-page | Get the login-page settings |
| [**getSettingsGeneral**](SettingsApi.md#getSettingsGeneral) | **GET** /settings/general | Get the general settings |
| [**setSettings**](SettingsApi.md#setSettings) | **PUT** /settings | Apply a settings configuration |
| [**setSettingsBrandingLoginPage**](SettingsApi.md#setSettingsBrandingLoginPage) | **PUT** /settings/branding/login-page | Set the login-page settings |
| [**setSettingsBrandingLogo**](SettingsApi.md#setSettingsBrandingLogo) | **PUT** /settings/branding/logo | Set the logo |
| [**setSettingsGeneral**](SettingsApi.md#setSettingsGeneral) | **PUT** /settings/general | Set the general settings |


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
- **Accept**: application/json

<a name="getSettingsBrandingLoginPage"></a>
# **getSettingsBrandingLoginPage**
> SettingsBrandingLoginPageModel getSettingsBrandingLoginPage()

Get the login-page settings

### Parameters
This endpoint does not need any parameter.

### Return type

[**SettingsBrandingLoginPageModel**](../Models/SettingsBrandingLoginPageModel.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

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
- **Accept**: application/json

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

- **Content-Type**: application/json
- **Accept**: application/json

<a name="setSettingsBrandingLoginPage"></a>
# **setSettingsBrandingLoginPage**
> SettingsBrandingLoginPageModel setSettingsBrandingLoginPage(SettingsBrandingLoginPageModel)

Set the login-page settings

### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **SettingsBrandingLoginPageModel** | [**SettingsBrandingLoginPageModel**](../Models/SettingsBrandingLoginPageModel.md)|  | [optional] |

### Return type

[**SettingsBrandingLoginPageModel**](../Models/SettingsBrandingLoginPageModel.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json

<a name="setSettingsBrandingLogo"></a>
# **setSettingsBrandingLogo**
> ErrorCollection setSettingsBrandingLogo(body)

Set the logo

### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **body** | **Object**|  | [optional] |

### Return type

[**ErrorCollection**](../Models/ErrorCollection.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: image/*
- **Accept**: application/json

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

- **Content-Type**: application/json
- **Accept**: application/json

