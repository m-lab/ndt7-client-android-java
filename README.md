# ndt7-java

Java implementation of the [NDT7 protocol by M-Lab](https://github.com/m-lab/ndt-server/blob/master/spec/ndt7-protocol.md)


# How it works

Consumers wishing to use the NDT7 speed test should create a subclass of type `NDTTest` and override the functions that it exposes

--------------------------------------------------------------------------------------------------------------------------------------------------------

```
    public void onMeasurementDownloadProgress(Measurement measurement) {
    }
```
Returns Measurement information during the download speed test. You can look at the Measurement data class to understand what this will provide. If you are purely interested in download speed, you don't need to override this



--------------------------------------------------------------------------------------------------------------------------------------------------------

```
    public void onMeasurementUploadProgress(Measurement measurement) {
    }
```
Returns Measurement information during the upload speed test. You can look at the Measurement data class to understand what this will provide. If you are purely interested in upload speed, you don't need to override this

   
   
   --------------------------------------------------------------------------------------------------------------------------------------------------------

```
    public void onDownloadProgress(ClientResponse clientResponse) {
    }
```
This returns the ElapsedTime and NumBytes during the download test. ElapsedTime is in *microseconds*. 
To ease data transformation into the commonly used `mbps`, we expose a static class that assists with the calculations
`DataConverter.convertToMbps(clientResponse) will generate a String that represents the speed in mbps. Note this is mega*BITS* per second.



--------------------------------------------------------------------------------------------------------------------------------------------------------

```
    public void onUploadProgress(ClientResponse clientResponse) {
    }
```
This returns the ElapsedTime and NumBytes during the upload test. ElapsedTime is in *microseconds*. 
To ease data transformation into the commonly used `mbps`, we expose a static class that assists with the calculations
`DataConverter.convertToMbps(clientResponse) will generate a String that represents the speed in mbps. Note this is mega*BITS* per second.

--------------------------------------------------------------------------------------------------------------------------------------------------------



```
    public void onFinished(ClientResponse clientResponse, Throwable error, TestType testType) {
    }
```
This will be called when a speed test is finished. It includes the type of test. If you run both an upload and download test, this will be fired twice. Once with download results and once with the upload results.
You should also check the `error` is null. The reason for this is to allow the consumer to determine if they wish to use the results. The test may run for 9/10 seconds and then error. The consumer may believe that the results are accurate enough to use. Error will be null if the test completed successfully. Otherwise, it is up to the consumer to determine if they wish to run another scan or if they believe the partial results are acceptable
  