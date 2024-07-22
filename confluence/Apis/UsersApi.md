# UsersApi

All URIs are relative to *https://&lt;CONFLUENCE_URL&gt;/rest/bootstrapi/1*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**getUser**](UsersApi.md#getUser) | **GET** /users | Get a user |
| [**setUser**](UsersApi.md#setUser) | **PUT** /users | Update an user |
| [**setUserPassword**](UsersApi.md#setUserPassword) | **PUT** /users/password | Update a user password |


<a name="getUser"></a>
# **getUser**
> UserBean getUser(username)

Get a user

### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **username** | **String**|  | [default to null] |

### Return type

[**UserBean**](../Models/UserBean.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

<a name="setUser"></a>
# **setUser**
> UserBean setUser(username, UserBean)

Update an user

### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **username** | **String**|  | [default to null] |
| **UserBean** | [**UserBean**](../Models/UserBean.md)|  | |

### Return type

[**UserBean**](../Models/UserBean.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json

<a name="setUserPassword"></a>
# **setUserPassword**
> UserBean setUserPassword(username, body)

Update a user password

### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **username** | **String**|  | [default to null] |
| **body** | **String**|  | |

### Return type

[**UserBean**](../Models/UserBean.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: text/plain
- **Accept**: application/json

