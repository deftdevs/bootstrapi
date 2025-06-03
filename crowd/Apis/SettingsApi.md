# SettingsApi

All URIs are relative to *https://&lt;CROWD_URL&gt;/rest/bootstrapi/1*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**getLoginPage**](SettingsApi.md#getLoginPage) | **GET** /settings/branding/login-page | Get the login-page settings |
| [**getSettings**](SettingsApi.md#getSettings) | **GET** /settings | Get the general settings |
| [**setLoginPage**](SettingsApi.md#setLoginPage) | **PUT** /settings/branding/login-page | Set the login-page settings |
| [**setLogo**](SettingsApi.md#setLogo) | **PUT** /settings/branding/logo | Set the logo |
| [**setSettings**](SettingsApi.md#setSettings) | **PUT** /settings | Set the general settings |


<a name="getLoginPage"></a>
# **getLoginPage**
> SettingsBrandingLoginPageModel getLoginPage()

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

<a name="setLoginPage"></a>
# **setLoginPage**
> SettingsBrandingLoginPageModel setLoginPage(SettingsBrandingLoginPageModel)

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

<a name="setLogo"></a>
# **setLogo**
> ErrorCollection setLogo(body)

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

