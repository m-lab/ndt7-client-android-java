package net.measurementlab.ndt7.android.models;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings({"unused", "RedundantSuppression"})
public class Measurement {

  @SerializedName("ConnectionInfo")
  private final ConnectionInfo connectionInfo;
  @SerializedName("BBRInfo")
  private final BbrInfo bbrInfo;
  @SerializedName("TCPInfo")
  private final TcpInfo tcpInfo;

  public Measurement(
      ConnectionInfo connectionInfo,
      BbrInfo bbrInfo,
      TcpInfo tcpInfo
  ) {
    this.connectionInfo = connectionInfo;
    this.bbrInfo = bbrInfo;
    this.tcpInfo = tcpInfo;
  }

  public ConnectionInfo getConnectionInfo() {
    return connectionInfo;
  }

  public BbrInfo getBbrInfo() {
    return bbrInfo;
  }

  public TcpInfo getTcpInfo() {
    return tcpInfo;
  }

}

