# PermissionsApi

All URIs are relative to *https://&lt;JIRA_URL&gt;/rest/bootstrapi/1*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**getPermissionGlobal**](PermissionsApi.md#getPermissionGlobal) | **GET** /permissions/global | Get global permissions configuration |
| [**setPermissionGlobal**](PermissionsApi.md#setPermissionGlobal) | **PUT** /permissions/global | Set global permissions configuration |


<a name="getPermissionGlobal"></a>
# **getPermissionGlobal**
> PermissionsGlobalModel getPermissionGlobal()

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
- **Accept**: application/json

<a name="setPermissionGlobal"></a>
# **setPermissionGlobal**
> PermissionsGlobalModel setPermissionGlobal(PermissionsGlobalModel)

Set global permissions configuration

    Set the global permissions for ... TODO

### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **PermissionsGlobalModel** | [**PermissionsGlobalModel**](../Models/PermissionsGlobalModel.md)|  | |

### Return type

[**PermissionsGlobalModel**](../Models/PermissionsGlobalModel.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json

