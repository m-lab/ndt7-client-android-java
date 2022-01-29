package net.measurementlab.ndt7.android;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import net.measurementlab.ndt7.android.models.ClientResponse;
import net.measurementlab.ndt7.android.models.Measurement;

interface DataPublisher {

  void onMeasurementDownloadProgress(@NonNull Measurement measurement);

  void onMeasurementUploadProgress(@NonNull Measurement measurement);

  void onDownloadProgress(@NonNull ClientResponse clientResponse);

  void onUploadProgress(@NonNull ClientResponse clientResponse);

  void onFinished(
      @Nullable ClientResponse clientResponse,
      @Nullable Throwable error,
      @NonNull NdtTest.TestType testType
  );

}