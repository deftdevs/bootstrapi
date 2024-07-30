# GroupsApi

All URIs are relative to *https://&lt;CROWD_URL&gt;/rest/bootstrapi/1*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**setGroups**](GroupsApi.md#setGroups) | **PATCH** /groups | Set groups |


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

