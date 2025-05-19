# LicensesApi

All URIs are relative to *https://&lt;JIRA_URL&gt;/rest/bootstrapi/1*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**getLicenses**](LicensesApi.md#getLicenses) | **GET** /licenses | Get all licenses information |
| [**setLicenses**](LicensesApi.md#setLicenses) | **PUT** /licenses | Set a list of licenses |


<a name="getLicenses"></a>
# **getLicenses**
> List getLicenses()

Get all licenses information

    Upon successful request, returns a &#x60;LicensesModel&#x60; object containing license details. Be aware that &#x60;products&#x60; collection of the &#x60;LicenseModel&#x60; contains the product display names, not the product key names

### Parameters
This endpoint does not need any parameter.

### Return type

[**List**](../Models/LicenseModel.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

<a name="setLicenses"></a>
# **setLicenses**
> List setLicenses(request\_body)

Set a list of licenses

### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **request\_body** | [**List**](../Models/string.md)|  | [optional] |

### Return type

[**List**](../Models/LicenseModel.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json

