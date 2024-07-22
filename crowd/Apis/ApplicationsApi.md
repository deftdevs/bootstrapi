# ApplicationsApi

All URIs are relative to *https://&lt;CROWD_URL&gt;/rest/bootstrapi/1*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**createApplication**](ApplicationsApi.md#createApplication) | **POST** /applications | Add an application |
| [**deleteApplication**](ApplicationsApi.md#deleteApplication) | **DELETE** /applications/{id} | Delete an application |
| [**deleteApplications**](ApplicationsApi.md#deleteApplications) | **DELETE** /applications | Delete all applications |
| [**getApplication**](ApplicationsApi.md#getApplication) | **GET** /applications/{id} | Get an application |
| [**getApplications**](ApplicationsApi.md#getApplications) | **GET** /applications | Get all applications |
| [**setApplications**](ApplicationsApi.md#setApplications) | **PUT** /applications | Set a list of applications |
| [**updateApplication**](ApplicationsApi.md#updateApplication) | **PUT** /applications/{id} | Update an application |


<a name="createApplication"></a>
# **createApplication**
> ApplicationBean createApplication(ApplicationBean)

Add an application

### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **ApplicationBean** | [**ApplicationBean**](../Models/ApplicationBean.md)|  | [optional] |

### Return type

[**ApplicationBean**](../Models/ApplicationBean.md)

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

<a name="getApplication"></a>
# **getApplication**
> List getApplication(id)

Get an application

### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **id** | **Long**|  | [default to null] |

### Return type

[**List**](../Models/ApplicationBean.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

<a name="getApplications"></a>
# **getApplications**
> List getApplications()

Get all applications

    Upon successful request, returns a &#x60;ApplicationsBean&#x60; object containing all applications

### Parameters
This endpoint does not need any parameter.

### Return type

[**List**](../Models/ApplicationBean.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

<a name="setApplications"></a>
# **setApplications**
> List setApplications(ApplicationBean)

Set a list of applications

    NOTE: All existing applications with the same &#39;name&#39; attribute are updated.

### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **ApplicationBean** | [**List**](../Models/ApplicationBean.md)|  | [optional] |

### Return type

[**List**](../Models/ApplicationBean.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json

<a name="updateApplication"></a>
# **updateApplication**
> ApplicationBean updateApplication(id, ApplicationBean)

Update an application

### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **id** | **Long**|  | [default to null] |
| **ApplicationBean** | [**ApplicationBean**](../Models/ApplicationBean.md)|  | [optional] |

### Return type

[**ApplicationBean**](../Models/ApplicationBean.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json

