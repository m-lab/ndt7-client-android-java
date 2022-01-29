package net.measurementlab.ndt7.android.models;

import com.google.gson.annotations.SerializedName;

public class Urls {

  @SerializedName("ws:///ndt/v7/download")
  private final String ndt7DownloadWS;
  @SerializedName("ws:///ndt/v7/upload")
  private final String ndt7UploadWS;
  @SerializedName("wss:///ndt/v7/download")
  private final String ndt7DownloadWss;
  @SerializedName("wss:///ndt/v7/upload")
  private final String ndt7UploadWss;

  public Urls(
      String ndt7DownloadWS,
      String ndt7UploadWS,
      String ndt7DownloadWss,
      String ndt7UploadWss
  ) {
    this.ndt7DownloadWS = ndt7DownloadWS;
    this.ndt7UploadWS = ndt7UploadWS;
    this.ndt7DownloadWss = ndt7DownloadWss;
    this.ndt7UploadWss = ndt7UploadWss;
  }

  public String getNdt7DownloadWS() {
    return ndt7DownloadWS;
  }

  public String getNdt7UploadWS() {
    return ndt7UploadWS;
  }

  public String getNdt7DownloadWss() {
    return ndt7DownloadWss;
  }

  public String getNdt7UploadWss() {
    return ndt7UploadWss;
  }

}
