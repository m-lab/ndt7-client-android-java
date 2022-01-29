package net.measurementlab.ndt7.android.utils;

import static net.measurementlab.ndt7.android.utils.Ndt7Constants.MAX_MESSAGE_SIZE;

import androidx.annotation.NonNull;
import okio.ByteString;

public abstract class PayloadTransformer {

  /**
   * This is gonna let higher speed clients saturate their pipes better. It will gradually increase
   * the size of data if the web socket queue isn't filling up.
   */
  public static @NonNull ByteString performDynamicTuning(
      @NonNull ByteString data,
      long queueSize,
      double bytesEnqueued
  ) {
    double totalBytesTransmitted = bytesEnqueued - queueSize;

    if (data.size() * 2 < MAX_MESSAGE_SIZE && data.size() < totalBytesTransmitted / 16) {
      return ByteString.of(new byte[data.size() * 2]); // double the size of data
    } else {
      return data;
    }
  }

}
