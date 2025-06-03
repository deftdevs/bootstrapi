# ApplicationLinksApi

All URIs are relative to *https://&lt;JIRA_URL&gt;/rest/bootstrapi/1*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**deleteApplicationLinks**](ApplicationLinksApi.md#deleteApplicationLinks) | **DELETE** /application-links | Delete all application links |
| [**getApplicationLinks**](ApplicationLinksApi.md#getApplicationLinks) | **GET** /application-links | Get all application links |
| [**setApplicationLinks**](ApplicationLinksApi.md#setApplicationLinks) | **PUT** /application-links | Set a list of application links |


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

<a name="getApplicationLinks"></a>
# **getApplicationLinks**
> List getApplicationLinks()

Get all application links

### Parameters
This endpoint does not need any parameter.

### Return type

[**List**](../Models/ApplicationLinkModel.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

<a name="setApplicationLinks"></a>
# **setApplicationLinks**
> List setApplicationLinks(ignore-setup-errors, ApplicationLinkModel)

Set a list of application links

    NOTE: All existing application links with the same &#39;rpcUrl&#39; attribute are updated.

### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **ignore-setup-errors** | **Boolean**|  | [optional] [default to false] |
| **ApplicationLinkModel** | [**List**](../Models/ApplicationLinkModel.md)|  | [optional] |

### Return type

[**List**](../Models/ApplicationLinkModel.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json

