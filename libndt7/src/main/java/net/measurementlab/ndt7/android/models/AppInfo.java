package net.measurementlab.ndt7.android.models;

import com.google.gson.annotations.SerializedName;

public class AppInfo {

  @SerializedName("ElapsedTime")
  private final long elapsedTime;
  @SerializedName("NumBytes")
  private final double numBytes;

  public AppInfo(long elapsedTime, double numBytes) {
    this.elapsedTime = elapsedTime;
    this.numBytes = numBytes;
  }

  public long getElapsedTime() {
    return elapsedTime;
  }

  public double getNumBytes() {
    return numBytes;
  }

}
