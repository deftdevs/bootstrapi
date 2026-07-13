# MailServerApi

All URIs are relative to *https://CROWD_URL/rest/bootstrapi/1*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**deleteMailServerSmtp**](MailServerApi.md#deleteMailServerSmtp) | **DELETE** /mail-server/smtp | Remove the default SMTP mail server |
| [**getMailServerSmtp**](MailServerApi.md#getMailServerSmtp) | **GET** /mail-server/smtp | Get the default SMTP mail server |
| [**setMailServerSmtp**](MailServerApi.md#setMailServerSmtp) | **PUT** /mail-server/smtp | Set the default SMTP mail server |


<a name="deleteMailServerSmtp"></a>
# **deleteMailServerSmtp**
> deleteMailServerSmtp()

Remove the default SMTP mail server

    Removes the default SMTP mail server if one is configured; does nothing otherwise.

### Parameters
This endpoint does not need any parameter.

### Return type

null (empty response body)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: */*

<a name="getMailServerSmtp"></a>
# **getMailServerSmtp**
> MailServerSmtpModel getMailServerSmtp()

Get the default SMTP mail server

### Parameters
This endpoint does not need any parameter.

### Return type

[**MailServerSmtpModel**](../Models/MailServerSmtpModel.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json, application/yaml, application/x-yaml, text/yaml

<a name="setMailServerSmtp"></a>
# **setMailServerSmtp**
> MailServerSmtpModel setMailServerSmtp(MailServerSmtpModel)

Set the default SMTP mail server

### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **MailServerSmtpModel** | [**MailServerSmtpModel**](../Models/MailServerSmtpModel.md)|  | [optional] |

### Return type

[**MailServerSmtpModel**](../Models/MailServerSmtpModel.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json, application/yaml, application/x-yaml, text/yaml
- **Accept**: application/json, application/yaml, application/x-yaml, text/yaml

