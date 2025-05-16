# ApplicationLinkApi

All URIs are relative to *https://&lt;CROWD_URL&gt;/rest/bootstrapi/1*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**createApplicationLink**](ApplicationLinkApi.md#createApplicationLink) | **POST** /application-link | Create an application link |
| [**deleteApplicationLink**](ApplicationLinkApi.md#deleteApplicationLink) | **DELETE** /application-link/{uuid} | Delete an application link |
| [**getApplicationLink**](ApplicationLinkApi.md#getApplicationLink) | **GET** /application-link/{uuid} | Get an application link |
| [**updateApplicationLink**](ApplicationLinkApi.md#updateApplicationLink) | **PUT** /application-link/{uuid} | Update an application link |


<a name="createApplicationLink"></a>
# **createApplicationLink**
> ApplicationLinkModel createApplicationLink(ApplicationLinkModel, ignore-setup-errors)

Create an application link

### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **ApplicationLinkModel** | [**ApplicationLinkModel**](../Models/ApplicationLinkModel.md)|  | |
| **ignore-setup-errors** | **Boolean**|  | [optional] [default to false] |

### Return type

[**ApplicationLinkModel**](../Models/ApplicationLinkModel.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json

<a name="deleteApplicationLink"></a>
# **deleteApplicationLink**
> deleteApplicationLink(uuid)

Delete an application link

### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **uuid** | **UUID**|  | [default to null] |

### Return type

null (empty response body)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: */*

<a name="getApplicationLink"></a>
# **getApplicationLink**
> ApplicationLinkModel getApplicationLink(uuid)

Get an application link

    Upon successful request, 

### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **uuid** | **UUID**|  | [default to null] |

### Return type

[**ApplicationLinkModel**](../Models/ApplicationLinkModel.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

<a name="updateApplicationLink"></a>
# **updateApplicationLink**
> ApplicationLinkModel updateApplicationLink(uuid, ApplicationLinkModel, ignore-setup-errors)

Update an application link

### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **uuid** | **UUID**|  | [default to null] |
| **ApplicationLinkModel** | [**ApplicationLinkModel**](../Models/ApplicationLinkModel.md)|  | |
| **ignore-setup-errors** | **Boolean**|  | [optional] [default to false] |

### Return type

[**ApplicationLinkModel**](../Models/ApplicationLinkModel.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json

