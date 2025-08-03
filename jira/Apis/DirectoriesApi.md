# DirectoriesApi

All URIs are relative to *https://&lt;JIRA_URL&gt;/rest/bootstrapi/1*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**deleteDirectories**](DirectoriesApi.md#deleteDirectories) | **DELETE** /directories | Delete all user directories |
| [**getDirectories**](DirectoriesApi.md#getDirectories) | **GET** /directories | Get all user directories |
| [**setDirectories**](DirectoriesApi.md#setDirectories) | **PUT** /directories | Set directories mapped by their name. |


<a name="deleteDirectories"></a>
# **deleteDirectories**
> deleteDirectories(force)

Delete all user directories

    NOTE: The &#39;force&#39; parameter must be set to &#39;true&#39; in order to execute this request.

### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **force** | **Boolean**|  | [optional] [default to null] |

### Return type

null (empty response body)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: */*

<a name="getDirectories"></a>
# **getDirectories**
> Map getDirectories()

Get all user directories

### Parameters
This endpoint does not need any parameter.

### Return type

[**Map**](../Models/AbstractDirectoryModel.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

<a name="setDirectories"></a>
# **setDirectories**
> Map setDirectories(request\_body)

Set directories mapped by their name.

    NOTE: All existing directories with the same &#39;name&#39; attribute are updated.

### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **request\_body** | [**Map**](../Models/AbstractDirectoryModel.md)|  | [optional] |

### Return type

[**Map**](../Models/AbstractDirectoryModel.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json

