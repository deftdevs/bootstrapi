# GroupsApi

All URIs are relative to *https://&lt;CROWD_URL&gt;/rest/bootstrapi/1*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**createGroup**](GroupsApi.md#createGroup) | **POST** /groups | Create a group |
| [**getGroup**](GroupsApi.md#getGroup) | **GET** /groups | Get a group |
| [**setGroups**](GroupsApi.md#setGroups) | **PATCH** /groups | Set groups |
| [**updateGroup**](GroupsApi.md#updateGroup) | **PUT** /groups | Update a group |


<a name="createGroup"></a>
# **createGroup**
> GroupBean createGroup(directoryId, GroupBean)

Create a group

### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **directoryId** | **Long**|  | [default to null] |
| **GroupBean** | [**GroupBean**](../Models/GroupBean.md)|  | |

### Return type

[**GroupBean**](../Models/GroupBean.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json

<a name="getGroup"></a>
# **getGroup**
> GroupBean getGroup(directoryId, name)

Get a group

### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **directoryId** | **Long**|  | [default to null] |
| **name** | **String**|  | [default to null] |

### Return type

[**GroupBean**](../Models/GroupBean.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

<a name="setGroups"></a>
# **setGroups**
> GroupBean setGroups(directoryId, GroupBean)

Set groups

### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **directoryId** | **Long**|  | [default to null] |
| **GroupBean** | [**List**](../Models/GroupBean.md)|  | |

### Return type

[**GroupBean**](../Models/GroupBean.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json

<a name="updateGroup"></a>
# **updateGroup**
> GroupBean updateGroup(directoryId, name, GroupBean)

Update a group

### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **directoryId** | **Long**|  | [default to null] |
| **name** | **String**|  | [default to null] |
| **GroupBean** | [**GroupBean**](../Models/GroupBean.md)|  | |

### Return type

[**GroupBean**](../Models/GroupBean.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json

