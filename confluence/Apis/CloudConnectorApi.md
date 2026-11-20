# CloudConnectorApi

All URIs are relative to *https://CONFLUENCE_URL/rest/bootstrapi/1*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**deleteCloudConnector**](CloudConnectorApi.md#deleteCloudConnector) | **DELETE** /cloud-connector | Disable the cloud connector |
| [**getCloudConnector**](CloudConnectorApi.md#getCloudConnector) | **GET** /cloud-connector | Get the cloud connector |
| [**setCloudConnector**](CloudConnectorApi.md#setCloudConnector) | **PUT** /cloud-connector | Set the cloud connector |


<a name="deleteCloudConnector"></a>
# **deleteCloudConnector**
> deleteCloudConnector()

Disable the cloud connector

    Disables the cloud connector and revokes its credentials in the cloud. Fails while cloud product connections depend on the connector.

### Parameters
This endpoint does not need any parameter.

### Return type

null (empty response body)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: */*

<a name="getCloudConnector"></a>
# **getCloudConnector**
> CloudConnectorModel getCloudConnector()

Get the cloud connector

    The client credentials are never returned.

### Parameters
This endpoint does not need any parameter.

### Return type

[**CloudConnectorModel**](../Models/CloudConnectorModel.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json, application/yaml, application/x-yaml, text/yaml

<a name="setCloudConnector"></a>
# **setCloudConnector**
> CloudConnectorModel setCloudConnector(CloudConnectorModel)

Set the cloud connector

    Creates or replaces the cloud connector from the client credentials generated in Atlassian Administration. The credentials must have been generated for the base URL of this instance. Applying the credentials of the current connector again is a no-op.

### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **CloudConnectorModel** | [**CloudConnectorModel**](../Models/CloudConnectorModel.md)|  | [optional] |

### Return type

[**CloudConnectorModel**](../Models/CloudConnectorModel.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json, application/yaml, application/x-yaml, text/yaml
- **Accept**: application/json, application/yaml, application/x-yaml, text/yaml

