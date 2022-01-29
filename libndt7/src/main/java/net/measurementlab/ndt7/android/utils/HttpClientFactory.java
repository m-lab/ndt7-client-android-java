package net.measurementlab.ndt7.android.utils;

import static java.util.concurrent.TimeUnit.SECONDS;

import androidx.annotation.NonNull;
import okhttp3.OkHttpClient;

public abstract class HttpClientFactory {

  public static @NonNull OkHttpClient createHttpClient() {
    return createHttpClient(10L, 10L, 10L);
  }

  public static @NonNull OkHttpClient createHttpClient(
      long connectTimeout,
      long readTimeout,
      long writeTimeout
  ) {
    return new OkHttpClient.Builder()
        .connectTimeout(connectTimeout, SECONDS)
        .readTimeout(readTimeout, SECONDS)
        .writeTimeout(writeTimeout, SECONDS)
        .build();
  }

}
