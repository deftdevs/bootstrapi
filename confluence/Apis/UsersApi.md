# UsersApi

All URIs are relative to *https://&lt;CONFLUENCE_URL&gt;/rest/bootstrapi/1*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**getUser**](UsersApi.md#getUser) | **GET** /users | Get a user |
| [**setUser**](UsersApi.md#setUser) | **PUT** /users | Update an user |
| [**setUserPassword**](UsersApi.md#setUserPassword) | **PUT** /users/password | Update a user password |


<a name="getUser"></a>
# **getUser**
> UserModel getUser(username)

Get a user

### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **username** | **String**|  | [default to null] |

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
| **username** | **String**|  | [default to null] |
| **UserModel** | [**UserModel**](../Models/UserModel.md)|  | |

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
| **username** | **String**|  | [default to null] |
| **body** | **String**|  | |

### Return type

[**UserModel**](../Models/UserModel.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: text/plain
- **Accept**: application/json

