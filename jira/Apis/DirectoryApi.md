# DirectoryApi

All URIs are relative to *https://&lt;JIRA_URL&gt;/rest/bootstrapi/1*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**createDirectory**](DirectoryApi.md#createDirectory) | **POST** /directory | Create a user directory |
| [**deleteDirectory**](DirectoryApi.md#deleteDirectory) | **DELETE** /directory/{id} | Delete a user directory |
| [**getDirectory**](DirectoryApi.md#getDirectory) | **GET** /directory/{id} | Get a user directory |
| [**updateDirectory**](DirectoryApi.md#updateDirectory) | **PUT** /directory/{id} | Update a user directory |


<a name="createDirectory"></a>
# **createDirectory**
> AbstractDirectoryBean createDirectory(AbstractDirectoryBean, test-connection)

Create a user directory

### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **AbstractDirectoryBean** | [**AbstractDirectoryBean**](../Models/AbstractDirectoryBean.md)|  | |
| **test-connection** | **Boolean**|  | [optional] [default to false] |

### Return type

[**AbstractDirectoryBean**](../Models/AbstractDirectoryBean.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json

<a name="deleteDirectory"></a>
# **deleteDirectory**
> deleteDirectory(id)

Delete a user directory

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

<a name="getDirectory"></a>
# **getDirectory**
> AbstractDirectoryBean getDirectory(id)

Get a user directory

### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **id** | **Long**|  | [default to null] |

### Return type

[**AbstractDirectoryBean**](../Models/AbstractDirectoryBean.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

<a name="updateDirectory"></a>
# **updateDirectory**
> AbstractDirectoryBean updateDirectory(id, AbstractDirectoryBean, test-connection)

Update a user directory

### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **id** | **Long**|  | [default to null] |
| **AbstractDirectoryBean** | [**AbstractDirectoryBean**](../Models/AbstractDirectoryBean.md)|  | |
| **test-connection** | **Boolean**|  | [optional] [default to false] |

### Return type

[**AbstractDirectoryBean**](../Models/AbstractDirectoryBean.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json

