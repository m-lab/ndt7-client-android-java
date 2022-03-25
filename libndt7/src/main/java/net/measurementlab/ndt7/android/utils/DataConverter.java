package net.measurementlab.ndt7.android.utils;

import androidx.annotation.NonNull;
import net.measurementlab.ndt7.android.NdtTest.TestType;
import net.measurementlab.ndt7.android.models.AppInfo;
import net.measurementlab.ndt7.android.models.ClientResponse;

public abstract class DataConverter {

  public static @NonNull ClientResponse generateResponse(
      long startTime,
      double numBytes,
      TestType testType
  ) {
    return new ClientResponse(
        new AppInfo(currentTimeInMicroseconds() - startTime, numBytes),
        null,
        testType.name()
    );
  }

  public static double convertToMbps(@NonNull ClientResponse clientResponse) {
    double time = clientResponse.getAppInfo().getElapsedTime() / 1e6;
    double speed = clientResponse.getAppInfo().getNumBytes() / time;
    speed *= 8;
    speed /= 1e6;
    return speed;
  }

  public static long currentTimeInMicroseconds() {
    return System.nanoTime() / 1000;
  }

}
