# GroupsApi

All URIs are relative to *https://CROWD_URL/rest/bootstrapi/1*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**setGroups**](GroupsApi.md#setGroups) | **PATCH** /groups | Set groups |


<a name="setGroups"></a>
# **setGroups**
> GroupModel setGroups(directoryId, request\_body)

Set groups

### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **directoryId** | **Long**|  | [optional] [default to null] |
| **request\_body** | [**Map**](../Models/GroupModel.md)|  | [optional] |

### Return type

[**GroupModel**](../Models/GroupModel.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json

