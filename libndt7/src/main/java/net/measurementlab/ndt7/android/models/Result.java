package net.measurementlab.ndt7.android.models;

import com.google.gson.annotations.SerializedName;

public class Result {

  @SerializedName("location")
  private final Location location;
  @SerializedName("machine")
  private final String machine;
  @SerializedName("urls")
  private final Urls urls;

  public Result(Location location, String machine, Urls urls) {
    this.location = location;
    this.machine = machine;
    this.urls = urls;
  }

  public Location getLocation() {
    return location;
  }

  public String getMachine() {
    return machine;
  }

  public Urls getUrls() {
    return urls;
  }

}
