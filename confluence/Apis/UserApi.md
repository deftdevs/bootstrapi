# UserApi

All URIs are relative to *https://&lt;CONFLUENCE_URL&gt;/rest/bootstrapi/1*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**getUser**](UserApi.md#getUser) | **GET** /user | Get a user |
| [**setUser**](UserApi.md#setUser) | **PUT** /user | Update an user |
| [**setUserPassword**](UserApi.md#setUserPassword) | **PUT** /user/password | Update a user password |


<a name="getUser"></a>
# **getUser**
> UserModel getUser(username)

Get a user

### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **username** | **String**|  | [optional] [default to null] |

### Return type

[**UserModel**](../Models/UserModel.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

<a name="setUser"></a>
# **setUser**
> UserModel setUser(username, UserModel)

Update an user

### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **username** | **String**|  | [optional] [default to null] |
| **UserModel** | [**UserModel**](../Models/UserModel.md)|  | [optional] |

### Return type

[**UserModel**](../Models/UserModel.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json

<a name="setUserPassword"></a>
# **setUserPassword**
> UserModel setUserPassword(username, body)

Update a user password

### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **username** | **String**|  | [optional] [default to null] |
| **body** | **String**|  | [optional] |

### Return type

[**UserModel**](../Models/UserModel.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: text/plain
- **Accept**: application/json

