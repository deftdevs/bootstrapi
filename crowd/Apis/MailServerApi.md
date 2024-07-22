# MailServerApi

All URIs are relative to *https://&lt;CROWD_URL&gt;/rest/bootstrapi/1*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**getMailServerSmtp**](MailServerApi.md#getMailServerSmtp) | **GET** /mail-server/smtp | Get the default SMTP mail server |
| [**setMailServerSmtp**](MailServerApi.md#setMailServerSmtp) | **PUT** /mail-server/smtp | Set the default SMTP mail server |


<a name="getMailServerSmtp"></a>
# **getMailServerSmtp**
> MailServerSmtpBean getMailServerSmtp()

Get the default SMTP mail server

### Parameters
This endpoint does not need any parameter.

### Return type

[**MailServerSmtpBean**](../Models/MailServerSmtpBean.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

<a name="setMailServerSmtp"></a>
# **setMailServerSmtp**
> MailServerSmtpBean setMailServerSmtp(MailServerSmtpBean)

Set the default SMTP mail server

### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **MailServerSmtpBean** | [**MailServerSmtpBean**](../Models/MailServerSmtpBean.md)|  | |

### Return type

[**MailServerSmtpBean**](../Models/MailServerSmtpBean.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json

