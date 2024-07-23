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
> GadgetBean createGadget(GadgetBean)

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

<a name="updateGadget"></a>
# **updateGadget**
> GadgetBean updateGadget(id, GadgetBean)

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

