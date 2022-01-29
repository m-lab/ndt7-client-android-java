package net.measurementlab.ndt7.android.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class HostnameResponse {

  @SerializedName("results")
  private final List<Result> results;

  public HostnameResponse(List<Result> results) {
    this.results = results;
  }

  public List<Result> getResults() {
    return results;
  }

}

