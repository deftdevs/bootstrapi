# MailServerApi

All URIs are relative to *https://&lt;CONFLUENCE_URL&gt;/rest/bootstrapi/1*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**getMailServerPop**](MailServerApi.md#getMailServerPop) | **GET** /mail-server/pop | Get the default POP mail server |
| [**getMailServerSmtp**](MailServerApi.md#getMailServerSmtp) | **GET** /mail-server/smtp | Get the default SMTP mail server |
| [**setMailServerPop**](MailServerApi.md#setMailServerPop) | **PUT** /mail-server/pop | Set the default POP mail server |
| [**setMailServerSmtp**](MailServerApi.md#setMailServerSmtp) | **PUT** /mail-server/smtp | Set the default SMTP mail server |


<a name="getMailServerPop"></a>
# **getMailServerPop**
> MailServerPopBean getMailServerPop()

Get the default POP mail server

### Parameters
This endpoint does not need any parameter.

### Return type

[**MailServerPopBean**](../Models/MailServerPopBean.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

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

<a name="setMailServerPop"></a>
# **setMailServerPop**
> MailServerPopBean setMailServerPop(MailServerPopBean)

Set the default POP mail server

### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **MailServerPopBean** | [**MailServerPopBean**](../Models/MailServerPopBean.md)|  | |

### Return type

[**MailServerPopBean**](../Models/MailServerPopBean.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
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

