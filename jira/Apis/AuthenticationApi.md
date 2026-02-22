# AuthenticationApi

All URIs are relative to *https://&lt;JIRA_URL&gt;/rest/bootstrapi/1*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**getAuthenticationIdps**](AuthenticationApi.md#getAuthenticationIdps) | **GET** /authentication/idps | Get all authentication identity providers |
| [**getAuthenticationSso**](AuthenticationApi.md#getAuthenticationSso) | **GET** /authentication/sso | Get authentication SSO configuration |
| [**setAuthenticationIdps**](AuthenticationApi.md#setAuthenticationIdps) | **PATCH** /authentication/idps | Set all authentication identity providers |
| [**setAuthenticationSso**](AuthenticationApi.md#setAuthenticationSso) | **PATCH** /authentication/sso | Set authentication SSO configuration |


<a name="getAuthenticationIdps"></a>
# **getAuthenticationIdps**
> List getAuthenticationIdps()

Get all authentication identity providers

### Parameters
This endpoint does not need any parameter.

### Return type

[**List**](../Models/AbstractAuthenticationIdpModel.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

<a name="getAuthenticationSso"></a>
# **getAuthenticationSso**
> AuthenticationSsoModel getAuthenticationSso()

Get authentication SSO configuration

### Parameters
This endpoint does not need any parameter.

### Return type

[**AuthenticationSsoModel**](../Models/AuthenticationSsoModel.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

<a name="setAuthenticationIdps"></a>
# **setAuthenticationIdps**
> List setAuthenticationIdps(request\_body)

Set all authentication identity providers

### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **request\_body** | [**Map**](../Models/AbstractAuthenticationIdpModel.md)|  | [optional] |

### Return type

[**List**](../Models/AbstractAuthenticationIdpModel.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json

<a name="setAuthenticationSso"></a>
# **setAuthenticationSso**
> AuthenticationSsoModel setAuthenticationSso(AuthenticationSsoModel)

Set authentication SSO configuration

### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **AuthenticationSsoModel** | [**AuthenticationSsoModel**](../Models/AuthenticationSsoModel.md)|  | [optional] |

### Return type

[**AuthenticationSsoModel**](../Models/AuthenticationSsoModel.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json

