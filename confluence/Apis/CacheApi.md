# CacheApi

All URIs are relative to *https://&lt;CONFLUENCE_URL&gt;/rest/bootstrapi/1*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**flushCache**](CacheApi.md#flushCache) | **POST** /caches/{name}/flush | Flushes a cache |
| [**getCache**](CacheApi.md#getCache) | **GET** /caches/{name} | Read cache information for a specified cache |
| [**getCaches**](CacheApi.md#getCaches) | **GET** /caches | Read all cache information |
| [**updateCache**](CacheApi.md#updateCache) | **PUT** /caches/{name} | Update an existing cache-size. Only Setting maxObjectCount is supported. |


<a name="flushCache"></a>
# **flushCache**
> CacheModel flushCache(name)

Flushes a cache

    Empties the specified cache

### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **name** | **String**|  | [default to null] |

### Return type

[**CacheModel**](../Models/CacheModel.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

<a name="getCache"></a>
# **getCache**
> CacheModel getCache(name)

Read cache information for a specified cache

### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **name** | **String**|  | [default to null] |

### Return type

[**CacheModel**](../Models/CacheModel.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

<a name="getCaches"></a>
# **getCaches**
> List getCaches()

Read all cache information

### Parameters
This endpoint does not need any parameter.

### Return type

[**List**](../Models/CacheModel.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

<a name="updateCache"></a>
# **updateCache**
> CacheModel updateCache(name, CacheModel)

Update an existing cache-size. Only Setting maxObjectCount is supported.

### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **name** | **String**|  | [default to null] |
| **CacheModel** | [**CacheModel**](../Models/CacheModel.md)|  | [optional] |

### Return type

[**CacheModel**](../Models/CacheModel.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json

