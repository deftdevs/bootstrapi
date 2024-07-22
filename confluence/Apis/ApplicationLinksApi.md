# ApplicationLinksApi

All URIs are relative to *https://&lt;CONFLUENCE_URL&gt;/rest/bootstrapi/1*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**createApplicationLink**](ApplicationLinksApi.md#createApplicationLink) | **POST** /application-links | Create an application link |
| [**deleteApplicationLink**](ApplicationLinksApi.md#deleteApplicationLink) | **DELETE** /application-links/{uuid} | Delete an application link |
| [**deleteApplicationLinks**](ApplicationLinksApi.md#deleteApplicationLinks) | **DELETE** /application-links | Delete all application links |
| [**getApplicationLink**](ApplicationLinksApi.md#getApplicationLink) | **GET** /application-links/{uuid} | Get an application link |
| [**getApplicationLinks**](ApplicationLinksApi.md#getApplicationLinks) | **GET** /application-links | Get all application links |
| [**setApplicationLinks**](ApplicationLinksApi.md#setApplicationLinks) | **PUT** /application-links | Set a list of application links |
| [**updateApplicationLink**](ApplicationLinksApi.md#updateApplicationLink) | **PUT** /application-links/{uuid} | Update an application link |


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

<a name="deleteApplicationLinks"></a>
# **deleteApplicationLinks**
> deleteApplicationLinks(force)

Delete all application links

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

<a name="getApplicationLinks"></a>
# **getApplicationLinks**
> List getApplicationLinks()

Get all application links

### Parameters
This endpoint does not need any parameter.

### Return type

[**List**](../Models/ApplicationLinkBean.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

<a name="setApplicationLinks"></a>
# **setApplicationLinks**
> List setApplicationLinks(ApplicationLinkBean, ignore-setup-errors)

Set a list of application links

    NOTE: All existing application links with the same &#39;rpcUrl&#39; attribute are updated.

### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **ApplicationLinkBean** | [**List**](../Models/ApplicationLinkBean.md)|  | |
| **ignore-setup-errors** | **Boolean**|  | [optional] [default to false] |

### Return type

[**List**](../Models/ApplicationLinkBean.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
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

