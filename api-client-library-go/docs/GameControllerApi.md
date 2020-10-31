# \GameControllerApi

All URIs are relative to *https://localhost:8080*

Method | HTTP request | Description
------------- | ------------- | -------------
[**CreateGameUsingPOST**](GameControllerApi.md#CreateGameUsingPOST) | **Post** /challenge/deviget/minesweeper/v1/game | createGame
[**GetGameUsingGET**](GameControllerApi.md#GetGameUsingGET) | **Get** /challenge/deviget/minesweeper/v1/game/{userName} | getGame
[**GetTimeTrackerUsingGET**](GameControllerApi.md#GetTimeTrackerUsingGET) | **Get** /challenge/deviget/minesweeper/v1/game/{userName}/timetracker | getTimeTracker
[**SetMarkUsingPUT**](GameControllerApi.md#SetMarkUsingPUT) | **Put** /challenge/deviget/minesweeper/v1/game/{userName}/mark/{markType} | setMark
[**StepOnUsingPUT**](GameControllerApi.md#StepOnUsingPUT) | **Put** /challenge/deviget/minesweeper/v1/game/{userName}/step | stepOn


# **CreateGameUsingPOST**
> ResponseEntity CreateGameUsingPOST(ctx, request)
createGame

### Required Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **ctx** | **context.Context** | context for authentication, logging, cancellation, deadlines, tracing, etc.
  **request** | [**GridRequest**](GridRequest.md)| request | 

### Return type

[**ResponseEntity**](ResponseEntity.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **GetGameUsingGET**
> ResponseEntity GetGameUsingGET(ctx, userName)
getGame

### Required Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **ctx** | **context.Context** | context for authentication, logging, cancellation, deadlines, tracing, etc.
  **userName** | **string**| userName | 

### Return type

[**ResponseEntity**](ResponseEntity.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **GetTimeTrackerUsingGET**
> ResponseEntity GetTimeTrackerUsingGET(ctx, userName)
getTimeTracker

### Required Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **ctx** | **context.Context** | context for authentication, logging, cancellation, deadlines, tracing, etc.
  **userName** | **string**| userName | 

### Return type

[**ResponseEntity**](ResponseEntity.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **SetMarkUsingPUT**
> ResponseEntity SetMarkUsingPUT(ctx, markType, request, userName)
setMark

### Required Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **ctx** | **context.Context** | context for authentication, logging, cancellation, deadlines, tracing, etc.
  **markType** | **string**| markType | 
  **request** | [**MarkRequest**](MarkRequest.md)| request | 
  **userName** | **string**| userName | 

### Return type

[**ResponseEntity**](ResponseEntity.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **StepOnUsingPUT**
> ResponseEntity StepOnUsingPUT(ctx, request, userName)
stepOn

### Required Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **ctx** | **context.Context** | context for authentication, logging, cancellation, deadlines, tracing, etc.
  **request** | [**StepRequest**](StepRequest.md)| request | 
  **userName** | **string**| userName | 

### Return type

[**ResponseEntity**](ResponseEntity.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

