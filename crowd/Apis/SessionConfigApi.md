# SessionConfigApi

All URIs are relative to *https://&lt;CROWD_URL&gt;/rest/bootstrapi/1*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**getSessionConfig**](SessionConfigApi.md#getSessionConfig) | **GET** /session-config | Get the session config |
| [**setSessionConfig**](SessionConfigApi.md#setSessionConfig) | **PUT** /session-config | Set the session config |


<a name="getSessionConfig"></a>
# **getSessionConfig**
> SessionConfigBean getSessionConfig()

Get the session config

### Parameters
This endpoint does not need any parameter.

### Return type

[**SessionConfigBean**](../Models/SessionConfigBean.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

<a name="setSessionConfig"></a>
# **setSessionConfig**
> SessionConfigBean setSessionConfig(SessionConfigBean)

Set the session config

### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **SessionConfigBean** | [**SessionConfigBean**](../Models/SessionConfigBean.md)|  | [optional] |

### Return type

[**SessionConfigBean**](../Models/SessionConfigBean.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json

