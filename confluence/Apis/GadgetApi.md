# GadgetApi

All URIs are relative to *https://&lt;CONFLUENCE_URL&gt;/rest/bootstrapi/1*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**createGadget**](GadgetApi.md#createGadget) | **POST** /gadget | Add a gadget |
| [**deleteGadget**](GadgetApi.md#deleteGadget) | **DELETE** /gadget/{id} | Delete a gadget |
| [**getGadget**](GadgetApi.md#getGadget) | **GET** /gadget/{id} | Get a gadget |
| [**updateGadget**](GadgetApi.md#updateGadget) | **PUT** /gadget/{id} | Update a gadget |


<a name="createGadget"></a>
# **createGadget**
> GadgetModel createGadget(GadgetModel)

Add a gadget

    Upon successful request, returns a &#x60;GadgetModel&#x60; object of the created gadget.

### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **GadgetModel** | [**GadgetModel**](../Models/GadgetModel.md)|  | |

### Return type

[**GadgetModel**](../Models/GadgetModel.md)

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

<a name="getGadget"></a>
# **getGadget**
> GadgetModel getGadget(id)

Get a gadget

### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **id** | **Long**|  | [default to null] |

### Return type

[**GadgetModel**](../Models/GadgetModel.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

<a name="updateGadget"></a>
# **updateGadget**
> GadgetModel updateGadget(id, GadgetModel)

Update a gadget

### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **id** | **Long**|  | [default to null] |
| **GadgetModel** | [**GadgetModel**](../Models/GadgetModel.md)|  | |

### Return type

[**GadgetModel**](../Models/GadgetModel.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json

