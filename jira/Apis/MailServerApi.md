# MailServerApi

All URIs are relative to *https://&lt;JIRA_URL&gt;/rest/bootstrapi/1*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**getMailServerPop**](MailServerApi.md#getMailServerPop) | **GET** /mail-server/pop | Get the default POP mail server |
| [**getMailServerSmtp**](MailServerApi.md#getMailServerSmtp) | **GET** /mail-server/smtp | Get the default SMTP mail server |
| [**setMailServerPop**](MailServerApi.md#setMailServerPop) | **PUT** /mail-server/pop | Set the default POP mail server |
| [**setMailServerSmtp**](MailServerApi.md#setMailServerSmtp) | **PUT** /mail-server/smtp | Set the default SMTP mail server |


<a name="getMailServerPop"></a>
# **getMailServerPop**
> MailServerPopModel getMailServerPop()

Get the default POP mail server

### Parameters
This endpoint does not need any parameter.

### Return type

[**MailServerPopModel**](../Models/MailServerPopModel.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

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
- **Accept**: application/json

<a name="setMailServerPop"></a>
# **setMailServerPop**
> MailServerPopModel setMailServerPop(MailServerPopModel)

Set the default POP mail server

### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **MailServerPopModel** | [**MailServerPopModel**](../Models/MailServerPopModel.md)|  | [optional] |

### Return type

[**MailServerPopModel**](../Models/MailServerPopModel.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json

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

- **Content-Type**: application/json
- **Accept**: application/json

