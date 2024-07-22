# DirectoriesApi

All URIs are relative to *https://&lt;CONFLUENCE_URL&gt;/rest/bootstrapi/1*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**createDirectory**](DirectoriesApi.md#createDirectory) | **POST** /directories | Add a user directory |
| [**deleteDirectories**](DirectoriesApi.md#deleteDirectories) | **DELETE** /directories | Delete all user directories |
| [**deleteDirectory**](DirectoriesApi.md#deleteDirectory) | **DELETE** /directories/{id} | Delete a user directory |
| [**getDirectories**](DirectoriesApi.md#getDirectories) | **GET** /directories | Get all user directories |
| [**getDirectory**](DirectoriesApi.md#getDirectory) | **GET** /directories/{id} | Get a user directory |
| [**setDirectories**](DirectoriesApi.md#setDirectories) | **PUT** /directories | Set a list of user directories |
| [**updateDirectory**](DirectoriesApi.md#updateDirectory) | **PUT** /directories/{id} | Update a user directory |


<a name="createDirectory"></a>
# **createDirectory**
> AbstractDirectoryBean createDirectory(AbstractDirectoryBean, test-connection)

Add a user directory

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

<a name="deleteDirectories"></a>
# **deleteDirectories**
> deleteDirectories(force)

Delete all user directories

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

<a name="getDirectories"></a>
# **getDirectories**
> List getDirectories()

Get all user directories

### Parameters
This endpoint does not need any parameter.

### Return type

[**List**](../Models/AbstractDirectoryBean.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

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

<a name="setDirectories"></a>
# **setDirectories**
> List setDirectories(AbstractDirectoryBean, test-connection)

Set a list of user directories

    NOTE: All existing directories with the same &#39;name&#39; attribute are updated.

### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **AbstractDirectoryBean** | [**List**](../Models/AbstractDirectoryBean.md)|  | |
| **test-connection** | **Boolean**|  | [optional] [default to false] |

### Return type

[**List**](../Models/AbstractDirectoryBean.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
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

