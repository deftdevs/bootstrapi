# ApplicationApi

All URIs are relative to *https://&lt;CROWD_URL&gt;/rest/bootstrapi/1*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**createApplication**](ApplicationApi.md#createApplication) | **POST** /application | Create an application |
| [**deleteApplication**](ApplicationApi.md#deleteApplication) | **DELETE** /application/{id} | Delete an application |
| [**getApplication**](ApplicationApi.md#getApplication) | **GET** /application/{id} | Get an application |
| [**updateApplication**](ApplicationApi.md#updateApplication) | **PUT** /application/{id} | Update an application |


<a name="createApplication"></a>
# **createApplication**
> ApplicationModel createApplication(ApplicationModel)

Create an application

### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **ApplicationModel** | [**ApplicationModel**](../Models/ApplicationModel.md)|  | [optional] |

### Return type

[**ApplicationModel**](../Models/ApplicationModel.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json

<a name="deleteApplication"></a>
# **deleteApplication**
> deleteApplication(id)

Delete an application

### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **id** | **Long**|  | [default to null] |

### Return type

null (empty response body)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: */*

<a name="getApplication"></a>
# **getApplication**
> List getApplication(id)

Get an application

### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **id** | **Long**|  | [default to null] |

### Return type

[**List**](../Models/ApplicationModel.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

<a name="updateApplication"></a>
# **updateApplication**
> ApplicationModel updateApplication(id, ApplicationModel)

Update an application

### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **id** | **Long**|  | [default to null] |
| **ApplicationModel** | [**ApplicationModel**](../Models/ApplicationModel.md)|  | [optional] |

### Return type

[**ApplicationModel**](../Models/ApplicationModel.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json

