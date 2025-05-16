# SessionConfigApi

All URIs are relative to *https://&lt;CROWD_URL&gt;/rest/bootstrapi/1*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**getSessionConfig**](SessionConfigApi.md#getSessionConfig) | **GET** /session-config | Get the session config |
| [**setSessionConfig**](SessionConfigApi.md#setSessionConfig) | **PUT** /session-config | Set the session config |


<a name="getSessionConfig"></a>
# **getSessionConfig**
> SessionConfigModel getSessionConfig()

Get the session config

### Parameters
This endpoint does not need any parameter.

### Return type

[**SessionConfigModel**](../Models/SessionConfigModel.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

<a name="setSessionConfig"></a>
# **setSessionConfig**
> SessionConfigModel setSessionConfig(SessionConfigModel)

Set the session config

### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **SessionConfigModel** | [**SessionConfigModel**](../Models/SessionConfigModel.md)|  | [optional] |

### Return type

[**SessionConfigModel**](../Models/SessionConfigModel.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json

