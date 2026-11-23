# CloudApi

All URIs are relative to *https://JIRA_URL/rest/bootstrapi/1*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**deleteCloudTunnel**](CloudApi.md#deleteCloudTunnel) | **DELETE** /cloud/tunnel | Delete the cloud application tunnel |
| [**getCloudTunnel**](CloudApi.md#getCloudTunnel) | **GET** /cloud/tunnel | Get the cloud application tunnel |
| [**setCloudTunnel**](CloudApi.md#setCloudTunnel) | **PUT** /cloud/tunnel | Set the cloud application tunnel |


<a name="deleteCloudTunnel"></a>
# **deleteCloudTunnel**
> deleteCloudTunnel()

Delete the cloud application tunnel

    Deletes the cloud application tunnel in this instance only, without contacting the cloud. Use this for an instance copied from another one, e.g. a staging instance restored from production data, which would otherwise share the tunnel with the original instance. Fails while the tunnel is managed by a cloud connection.

### Parameters
This endpoint does not need any parameter.

### Return type

null (empty response body)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: */*

<a name="getCloudTunnel"></a>
# **getCloudTunnel**
> CloudTunnelModel getCloudTunnel()

Get the cloud application tunnel

    The key is never returned.

### Parameters
This endpoint does not need any parameter.

### Return type

[**CloudTunnelModel**](../Models/CloudTunnelModel.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json, application/yaml, application/x-yaml, text/yaml

<a name="setCloudTunnel"></a>
# **setCloudTunnel**
> CloudTunnelModel setCloudTunnel(CloudTunnelModel)

Set the cloud application tunnel

    Creates or replaces the cloud application tunnel from the key generated when creating the tunnel in the cloud, optionally with an upstream URL. Applying the current key again is a no-op. Fails while the tunnel is managed by a cloud connection.

### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **CloudTunnelModel** | [**CloudTunnelModel**](../Models/CloudTunnelModel.md)|  | [optional] |

### Return type

[**CloudTunnelModel**](../Models/CloudTunnelModel.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json, application/yaml, application/x-yaml, text/yaml
- **Accept**: application/json, application/yaml, application/x-yaml, text/yaml

