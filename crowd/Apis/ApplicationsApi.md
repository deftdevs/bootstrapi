# ApplicationsApi

All URIs are relative to *https://&lt;CROWD_URL&gt;/rest/bootstrapi/1*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**deleteApplications**](ApplicationsApi.md#deleteApplications) | **DELETE** /applications | Delete all applications |
| [**getApplications**](ApplicationsApi.md#getApplications) | **GET** /applications | Get all applications |
| [**setApplications**](ApplicationsApi.md#setApplications) | **PUT** /applications | Set a list of applications |


<a name="deleteApplications"></a>
# **deleteApplications**
> deleteApplications(force)

Delete all applications

    NOTE: The &#39;force&#39; parameter must be se to &#39;true&#39; in order to execute this request.

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

<a name="getApplications"></a>
# **getApplications**
> List getApplications()

Get all applications

    Upon successful request, returns a &#x60;ApplicationsModel&#x60; object containing all applications

### Parameters
This endpoint does not need any parameter.

### Return type

[**List**](../Models/ApplicationModel.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

<a name="setApplications"></a>
# **setApplications**
> List setApplications(ApplicationModel)

Set a list of applications

    NOTE: All existing applications with the same &#39;name&#39; attribute are updated.

### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **ApplicationModel** | [**List**](../Models/ApplicationModel.md)|  | [optional] |

### Return type

[**List**](../Models/ApplicationModel.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json

