# UpmApi

All URIs are relative to *https://CONFLUENCE_URL/rest/bootstrapi/1*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**getPlugins**](UpmApi.md#getPlugins) | **GET** /upm | Get all installed plugins |
| [**setUpm**](UpmApi.md#setUpm) | **PUT** /upm | Apply a UPM configuration |


<a name="getPlugins"></a>
# **getPlugins**
> PluginModel getPlugins()

Get all installed plugins

    Returns every installed plugin (bundled and user-installed) with its version and enabled state, keyed by plugin key

### Parameters
This endpoint does not need any parameter.

### Return type

[**PluginModel**](../Models/PluginModel.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json, application/yaml, application/x-yaml, text/yaml

<a name="setUpm"></a>
# **setUpm**
> UpmModel setUpm(UpmModel)

Apply a UPM configuration

    Resolves, installs and enables (or disables) the given plugins. Every plugin references one of the named resolvers by key: &#39;marketplace&#39; type resolvers look the artifact up through the Marketplace REST API from the plugin key and version, &#39;maven&#39; type resolvers derive it from the plugin&#39;s Maven coordinates and the standard repository layout. A resolver&#39;s base URL may point to a proxying repository (e.g. an Artifactory generic remote), and each resolver supports basic-auth credentials and an optional web proxy. Plugins already installed in the requested version are skipped, so re-applying the same configuration is safe.

### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **UpmModel** | [**UpmModel**](../Models/UpmModel.md)|  | [optional] |

### Return type

[**UpmModel**](../Models/UpmModel.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json, application/yaml, application/x-yaml, text/yaml
- **Accept**: application/json, application/yaml, application/x-yaml, text/yaml

