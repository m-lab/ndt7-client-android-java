package net.measurementlab.ndt7.android.models;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings({"unused", "RedundantSuppression"})
public class BbrInfo {

  @SerializedName("BW")
  private final long bw;
  @SerializedName("MinRTT")
  private final long minRtt;
  @SerializedName("PacingGain")
  private final long pacingGain;
  @SerializedName("CwndGain")
  private final long cwndGain;
  @SerializedName("ElapsedTime")
  private final long elapsedTime;

  public BbrInfo(long bw, long minRtt, long pacingGain, long cwndGain, long elapsedTime) {
    this.bw = bw;
    this.minRtt = minRtt;
    this.pacingGain = pacingGain;
    this.cwndGain = cwndGain;
    this.elapsedTime = elapsedTime;
  }

  public long getBw() {
    return bw;
  }

  public long getMinRtt() {
    return minRtt;
  }

  public long getPacingGain() {
    return pacingGain;
  }

  public long getCwndGain() {
    return cwndGain;
  }

  public long getElapsedTime() {
    return elapsedTime;
  }

}
