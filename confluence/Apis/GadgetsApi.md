# GadgetsApi

All URIs are relative to *https://&lt;CONFLUENCE_URL&gt;/rest/bootstrapi/1*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**addGadget**](GadgetsApi.md#addGadget) | **POST** /gadgets | Add a gadget |
| [**deleteGadget**](GadgetsApi.md#deleteGadget) | **DELETE** /gadgets/{id} | Delete a gadget |
| [**deleteGadgets**](GadgetsApi.md#deleteGadgets) | **DELETE** /gadgets | Delete all gadgets |
| [**getGadget**](GadgetsApi.md#getGadget) | **GET** /gadgets/{id} | Get a gadget |
| [**getGadgets**](GadgetsApi.md#getGadgets) | **GET** /gadgets | Get all gadgets |
| [**setGadget**](GadgetsApi.md#setGadget) | **PUT** /gadgets/{id} | Update a gadget |
| [**setGadgets**](GadgetsApi.md#setGadgets) | **PUT** /gadgets | Set or update a list of gadgets |


<a name="addGadget"></a>
# **addGadget**
> GadgetBean addGadget(GadgetBean)

Add a gadget

    Upon successful request, returns a &#x60;GadgetBean&#x60; object of the created gadget.

### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **GadgetBean** | [**GadgetBean**](../Models/GadgetBean.md)|  | |

### Return type

[**GadgetBean**](../Models/GadgetBean.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json

<a name="deleteGadget"></a>
# **deleteGadget**
> deleteGadget(id)

Delete a gadget

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

<a name="getGadget"></a>
# **getGadget**
> GadgetBean getGadget(id)

Get a gadget

### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **id** | **Long**|  | [default to null] |

### Return type

[**GadgetBean**](../Models/GadgetBean.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

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

<a name="setGadget"></a>
# **setGadget**
> GadgetBean setGadget(id, GadgetBean)

Update a gadget

### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **id** | **Long**|  | [default to null] |
| **GadgetBean** | [**GadgetBean**](../Models/GadgetBean.md)|  | |

### Return type

[**GadgetBean**](../Models/GadgetBean.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json

<a name="setGadgets"></a>
# **setGadgets**
> List setGadgets(GadgetBean)

Set or update a list of gadgets

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

