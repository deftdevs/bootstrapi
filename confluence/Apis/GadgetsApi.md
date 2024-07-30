# GadgetsApi

All URIs are relative to *https://&lt;CONFLUENCE_URL&gt;/rest/bootstrapi/1*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**deleteGadgets**](GadgetsApi.md#deleteGadgets) | **DELETE** /gadgets | Delete all gadgets |
| [**getGadgets**](GadgetsApi.md#getGadgets) | **GET** /gadgets | Get all gadgets |
| [**setGadgets**](GadgetsApi.md#setGadgets) | **PUT** /gadgets | Set a list of gadgets |


<a name="deleteGadgets"></a>
# **deleteGadgets**
> deleteGadgets(force)

Delete all gadgets

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

<a name="getGadgets"></a>
# **getGadgets**
> List getGadgets()

Get all gadgets

### Parameters
This endpoint does not need any parameter.

### Return type

[**List**](../Models/GadgetBean.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

<a name="setGadgets"></a>
# **setGadgets**
> List setGadgets(GadgetBean)

Set a list of gadgets

    NOTE: This will only create gadgets that does not exist yet as there is no real &#39;update&#39;.

### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **GadgetBean** | [**List**](../Models/GadgetBean.md)|  | |

### Return type

[**List**](../Models/GadgetBean.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json

