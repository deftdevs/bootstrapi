# GroupApi

All URIs are relative to *https://&lt;CROWD_URL&gt;/rest/bootstrapi/1*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**createGroup**](GroupApi.md#createGroup) | **POST** /group | Create a group |
| [**getGroup**](GroupApi.md#getGroup) | **GET** /group | Get a group |
| [**updateGroup**](GroupApi.md#updateGroup) | **PUT** /group | Update a group |


<a name="createGroup"></a>
# **createGroup**
> GroupModel createGroup(directoryId, GroupModel)

Create a group

### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **directoryId** | **Long**|  | [default to null] |
| **GroupModel** | [**GroupModel**](../Models/GroupModel.md)|  | |

### Return type

[**GroupModel**](../Models/GroupModel.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json

<a name="getGroup"></a>
# **getGroup**
> GroupModel getGroup(directoryId, name)

Get a group

### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **directoryId** | **Long**|  | [default to null] |
| **name** | **String**|  | [default to null] |

### Return type

[**GroupModel**](../Models/GroupModel.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

<a name="updateGroup"></a>
# **updateGroup**
> GroupModel updateGroup(directoryId, name, GroupModel)

Update a group

### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **directoryId** | **Long**|  | [default to null] |
| **name** | **String**|  | [default to null] |
| **GroupModel** | [**GroupModel**](../Models/GroupModel.md)|  | |

### Return type

[**GroupModel**](../Models/GroupModel.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json

