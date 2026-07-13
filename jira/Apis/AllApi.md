# AllApi

All URIs are relative to *https://JIRA_URL/rest/bootstrapi/1*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**setAll**](AllApi.md#setAll) | **PUT** / | Apply a complete configuration |


<a name="setAll"></a>
# **setAll**
> _AllModel setAll(\_AllModel)

Apply a complete configuration

    Returns the updated configuration. The per-sub-field outcome is reported in the &#39;status&#39; map, keyed by the request&#39;s field paths (e.g. &#39;settings/general&#39;, &#39;mailServer/smtp&#39;; 2xx for success, 4xx/5xx for failure with a human-readable &#39;message&#39; and optional &#39;details&#39;). License keys in the response are redacted (e.g. &#39;AAAB...wxyz#a1b2&#39;). If any sub-field fails, the highest sub-field status code is returned with the same response body.

### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **\_AllModel** | [**_AllModel**](../Models/_AllModel.md)|  | |

### Return type

[**_AllModel**](../Models/_AllModel.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json

