package net.measurementlab.ndt7.android.models;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings({"unused", "RedundantSuppression"})
public class ConnectionInfo {

  @SerializedName("Client")
  private final String client;
  @SerializedName("Server")
  private final String server;
  @SerializedName("UUID")
  private final String uuid;

  public ConnectionInfo(String client, String server, String uuid) {
    this.client = client;
    this.server = server;
    this.uuid = uuid;
  }

  public String getClient() {
    return client;
  }

  public String getServer() {
    return server;
  }

  public String getUuid() {
    return uuid;
  }

}
