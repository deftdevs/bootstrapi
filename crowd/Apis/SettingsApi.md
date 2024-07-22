# SettingsApi

All URIs are relative to *https://&lt;CROWD_URL&gt;/rest/bootstrapi/1*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**getLoginPage**](SettingsApi.md#getLoginPage) | **GET** /settings/branding/login-page | Get the login-page settings |
| [**getSettings**](SettingsApi.md#getSettings) | **GET** /settings | Get the application settings |
| [**setLoginPage**](SettingsApi.md#setLoginPage) | **PUT** /settings/branding/login-page | Set the login-page settings |
| [**setLogo**](SettingsApi.md#setLogo) | **PUT** /settings/branding/logo | Set the logo |
| [**setSettings**](SettingsApi.md#setSettings) | **PUT** /settings | Set the application settings |


<a name="getLoginPage"></a>
# **getLoginPage**
> SettingsBrandingLoginPageBean getLoginPage()

Get the login-page settings

### Parameters
This endpoint does not need any parameter.

### Return type

[**SettingsBrandingLoginPageBean**](../Models/SettingsBrandingLoginPageBean.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

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

<a name="setLoginPage"></a>
# **setLoginPage**
> SettingsBrandingLoginPageBean setLoginPage(SettingsBrandingLoginPageBean)

Set the login-page settings

### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **SettingsBrandingLoginPageBean** | [**SettingsBrandingLoginPageBean**](../Models/SettingsBrandingLoginPageBean.md)|  | [optional] |

### Return type

[**SettingsBrandingLoginPageBean**](../Models/SettingsBrandingLoginPageBean.md)

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

