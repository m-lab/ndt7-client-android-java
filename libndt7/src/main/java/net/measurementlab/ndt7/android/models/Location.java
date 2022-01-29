package net.measurementlab.ndt7.android.models;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings({"unused", "RedundantSuppression"})
public class Location {

  @SerializedName("city")
  private final String city;
  @SerializedName("country")
  private final String country;

  public Location(String city, String country) {
    this.city = city;
    this.country = country;
  }

  public String getCity() {
    return city;
  }

  public String getCountry() {
    return country;
  }

}
