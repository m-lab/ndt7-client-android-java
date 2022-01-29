package net.measurementlab.ndt7.android.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import net.measurementlab.ndt7.android.NdtTest.TestType;

public class CallbackRegistry {

  public interface ITestSpeedTestProgress {

    void onSpeedTestProgress(@NonNull ClientResponse clientResponse);

  }

  public interface ITestMeasurementProgress {

    void onMeasurementProgress(@NonNull Measurement measurement);

  }

  public interface ITestFinishedObserver {

    void onTestFinished(
        @Nullable ClientResponse clientResponse,
        @Nullable Throwable error,
        @NonNull TestType testType
    );

  }

  private final ITestSpeedTestProgress testSpeedTestProgress;
  private final ITestMeasurementProgress testMeasurementProgress;
  private final ITestFinishedObserver testFinishedObserver;

  public CallbackRegistry(
      ITestSpeedTestProgress testSpeedTestProgress,
      ITestMeasurementProgress testMeasurementProgress,
      ITestFinishedObserver testFinishedObserver) {
    this.testSpeedTestProgress = testSpeedTestProgress;
    this.testMeasurementProgress = testMeasurementProgress;
    this.testFinishedObserver = testFinishedObserver;
  }

  public void onSpeedTestProgress(@NonNull ClientResponse generateResponse) {
    testSpeedTestProgress.onSpeedTestProgress(generateResponse);
  }

  public void onMeasurementProgress(@NonNull Measurement measurement) {
    testMeasurementProgress.onMeasurementProgress(measurement);
  }

  public void onFinished(
      @Nullable ClientResponse clientResponse,
      @Nullable Throwable error,
      @NonNull TestType testType) {
    testFinishedObserver.onTestFinished(clientResponse, error, testType);
  }

}
