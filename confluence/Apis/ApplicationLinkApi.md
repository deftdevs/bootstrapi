# ApplicationLinkApi

All URIs are relative to *https://&lt;CONFLUENCE_URL&gt;/rest/bootstrapi/1*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**createApplicationLink**](ApplicationLinkApi.md#createApplicationLink) | **POST** /application-link | Create an application link |
| [**deleteApplicationLink**](ApplicationLinkApi.md#deleteApplicationLink) | **DELETE** /application-link/{uuid} | Delete an application link |
| [**getApplicationLink**](ApplicationLinkApi.md#getApplicationLink) | **GET** /application-link/{uuid} | Get an application link |
| [**updateApplicationLink**](ApplicationLinkApi.md#updateApplicationLink) | **PUT** /application-link/{uuid} | Update an application link |


<a name="createApplicationLink"></a>
# **createApplicationLink**
> ApplicationLinkBean createApplicationLink(ApplicationLinkBean, ignore-setup-errors)

Create an application link

### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **ApplicationLinkBean** | [**ApplicationLinkBean**](../Models/ApplicationLinkBean.md)|  | |
| **ignore-setup-errors** | **Boolean**|  | [optional] [default to false] |

### Return type

[**ApplicationLinkBean**](../Models/ApplicationLinkBean.md)

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
> ApplicationLinkBean getApplicationLink(uuid)

Get an application link

    Upon successful request, 

### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **uuid** | **UUID**|  | [default to null] |

### Return type

[**ApplicationLinkBean**](../Models/ApplicationLinkBean.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

<a name="updateApplicationLink"></a>
# **updateApplicationLink**
> ApplicationLinkBean updateApplicationLink(uuid, ApplicationLinkBean, ignore-setup-errors)

Update an application link

### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **uuid** | **UUID**|  | [default to null] |
| **ApplicationLinkBean** | [**ApplicationLinkBean**](../Models/ApplicationLinkBean.md)|  | |
| **ignore-setup-errors** | **Boolean**|  | [optional] [default to false] |

### Return type

[**ApplicationLinkBean**](../Models/ApplicationLinkBean.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json

