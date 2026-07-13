# PermissionsApi

All URIs are relative to *https://JIRA_URL/rest/bootstrapi/1*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**getPermissionsGlobal**](PermissionsApi.md#getPermissionsGlobal) | **GET** /permissions/global | Get global permissions configuration |
| [**setPermissionsGlobal**](PermissionsApi.md#setPermissionsGlobal) | **PUT** /permissions/global | Set global permissions configuration |


<a name="getPermissionsGlobal"></a>
# **getPermissionsGlobal**
> PermissionsGlobalModel getPermissionsGlobal()

Get global permissions configuration

    Get the global permissions for ... TODO

### Parameters
This endpoint does not need any parameter.

### Return type

[**PermissionsGlobalModel**](../Models/PermissionsGlobalModel.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json, application/yaml, application/x-yaml, text/yaml

<a name="setPermissionsGlobal"></a>
# **setPermissionsGlobal**
> PermissionsGlobalModel setPermissionsGlobal(PermissionsGlobalModel)

Set global permissions configuration

    Set the global permissions for ... TODO

### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **PermissionsGlobalModel** | [**PermissionsGlobalModel**](../Models/PermissionsGlobalModel.md)|  | [optional] |

### Return type

[**PermissionsGlobalModel**](../Models/PermissionsGlobalModel.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json, application/yaml, application/x-yaml, text/yaml
- **Accept**: application/json, application/yaml, application/x-yaml, text/yaml

