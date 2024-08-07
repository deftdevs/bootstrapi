# TrustedProxiesApi

All URIs are relative to *https://&lt;CROWD_URL&gt;/rest/bootstrapi/1*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**addTrustedProxy**](TrustedProxiesApi.md#addTrustedProxy) | **POST** /trusted-proxies | Add a trusted proxy |
| [**getTrustedProxies**](TrustedProxiesApi.md#getTrustedProxies) | **GET** /trusted-proxies | Get the trusted proxies |
| [**removeTrustedProxy**](TrustedProxiesApi.md#removeTrustedProxy) | **DELETE** /trusted-proxies | Remove a trusted proxy |
| [**setTrustedProxies**](TrustedProxiesApi.md#setTrustedProxies) | **PUT** /trusted-proxies | Set the trusted proxies |


<a name="addTrustedProxy"></a>
# **addTrustedProxy**
> List addTrustedProxy(body)

Add a trusted proxy

### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **body** | **String**|  | [optional] |

### Return type

**List**

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: text/plain
- **Accept**: application/json

<a name="getTrustedProxies"></a>
# **getTrustedProxies**
> List getTrustedProxies()

Get the trusted proxies

### Parameters
This endpoint does not need any parameter.

### Return type

**List**

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

<a name="removeTrustedProxy"></a>
# **removeTrustedProxy**
> List removeTrustedProxy(body)

Remove a trusted proxy

### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **body** | **String**|  | [optional] |

### Return type

**List**

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: text/plain
- **Accept**: application/json

<a name="setTrustedProxies"></a>
# **setTrustedProxies**
> List setTrustedProxies(request\_body)

Set the trusted proxies

### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **request\_body** | [**List**](../Models/string.md)|  | [optional] |

### Return type

**List**

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json

