# MailTemplatesApi

All URIs are relative to *https://&lt;CROWD_URL&gt;/rest/bootstrapi/1*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**getMailTemplates**](MailTemplatesApi.md#getMailTemplates) | **GET** /mail-templates | Get the mail templates |
| [**setMailTemplates**](MailTemplatesApi.md#setMailTemplates) | **PUT** /mail-templates | Set the mail templates |


<a name="getMailTemplates"></a>
# **getMailTemplates**
> MailTemplatesModel getMailTemplates()

Get the mail templates

### Parameters
This endpoint does not need any parameter.

### Return type

[**MailTemplatesModel**](../Models/MailTemplatesModel.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

<a name="setMailTemplates"></a>
# **setMailTemplates**
> MailTemplatesModel setMailTemplates(MailTemplatesModel)

Set the mail templates

### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **MailTemplatesModel** | [**MailTemplatesModel**](../Models/MailTemplatesModel.md)|  | [optional] |

### Return type

[**MailTemplatesModel**](../Models/MailTemplatesModel.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json

