package net.measurementlab.ndt7.android.models;

import com.google.gson.annotations.SerializedName;

public class ClientResponse {

  @SerializedName("AppInfo")
  private final AppInfo appInfo;
  @SerializedName("Origin")
  private final String origin;
  @SerializedName("Test")
  private final String test;

  public ClientResponse(AppInfo appInfo, String origin, String test) {
    this.appInfo = appInfo;
    this.origin = origin == null ? "client" : origin;
    this.test = test;
  }

  public AppInfo getAppInfo() {
    return appInfo;
  }

  public String getOrigin() {
    return origin;
  }

  public String getTest() {
    return test;
  }

}
