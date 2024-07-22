# AuthenticationApi

All URIs are relative to *https://&lt;CONFLUENCE_URL&gt;/rest/bootstrapi/1*

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

[**List**](../Models/AbstractAuthenticationIdpBean.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

<a name="getAuthenticationSso"></a>
# **getAuthenticationSso**
> AuthenticationSsoBean getAuthenticationSso()

Get authentication SSO configuration

### Parameters
This endpoint does not need any parameter.

### Return type

[**AuthenticationSsoBean**](../Models/AuthenticationSsoBean.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

<a name="setAuthenticationIdps"></a>
# **setAuthenticationIdps**
> List setAuthenticationIdps(AbstractAuthenticationIdpBean)

Set all authentication identity providers

### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **AbstractAuthenticationIdpBean** | [**List**](../Models/AbstractAuthenticationIdpBean.md)|  | [optional] |

### Return type

[**List**](../Models/AbstractAuthenticationIdpBean.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json

<a name="setAuthenticationSso"></a>
# **setAuthenticationSso**
> AuthenticationSsoBean setAuthenticationSso(AuthenticationSsoBean)

Set authentication SSO configuration

### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **AuthenticationSsoBean** | [**AuthenticationSsoBean**](../Models/AuthenticationSsoBean.md)|  | [optional] |

### Return type

[**AuthenticationSsoBean**](../Models/AuthenticationSsoBean.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json

