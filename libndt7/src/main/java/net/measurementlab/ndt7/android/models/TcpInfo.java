package net.measurementlab.ndt7.android.models;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings({"unused", "RedundantSuppression", "SpellCheckingInspection"})
public class TcpInfo {

  @SerializedName("State")
  private final long state;
  @SerializedName("CAState")
  private final long caState;
  @SerializedName("Retransmits")
  private final long retransmits;
  @SerializedName("Probes")
  private final long probes;
  @SerializedName("Backoff")
  private final long backoff;
  @SerializedName("Options")
  private final long options;
  @SerializedName("WScale")
  private final long wScale;
  @SerializedName("AppLimited")
  private final long appLimited;
  @SerializedName("RTO")
  private final long rto;
  @SerializedName("ATO")
  private final long ato;
  @SerializedName("SndMSS")
  private final long sndMss;
  @SerializedName("RcvMSS")
  private final long rcvMss;
  @SerializedName("Unacked")
  private final long unacked;
  @SerializedName("Sacked")
  private final long sacked;
  @SerializedName("Lost")
  private final long lost;
  @SerializedName("Retrans")
  private final long retrans;
  @SerializedName("Fackets")
  private final long fackets;
  @SerializedName("LastDataSent")
  private final long lastDataSent;
  @SerializedName("LastAckSent")
  private final long lastAckSent;
  @SerializedName("LastDataRecv")
  private final long lastDataRecv;
  @SerializedName("LastAckRecv")
  private final long lastAckRecv;
  @SerializedName("PMTU")
  private final long pmtu;
  @SerializedName("RcvSsThresh")
  private final long rcvSsThresh;
  @SerializedName("RTT")
  private final long rtt;
  @SerializedName("RTTVar")
  private final long rttVar;
  @SerializedName("SndSsThresh")
  private final long sndSsThresth;
  @SerializedName("SndCwnd")
  private final long sndCwnd;
  @SerializedName("AdvMSS")
  private final long advMss;
  @SerializedName("Reordering")
  private final long reordering;
  @SerializedName("RcvRTT")
  private final long rcvRtt;
  @SerializedName("RcvSpace")
  private final long rcvSpace;
  @SerializedName("TotalRetrans")
  private final long totalRetrans;
  @SerializedName("PacingRate")
  private final long pacingRate;
  @SerializedName("MaxPacingRate")
  private final long maxPacingRate;
  @SerializedName("BytesAcked")
  private final long bytesAcked;
  @SerializedName("BytesReceived")
  private final long bytesReceived;
  @SerializedName("SegsOut")
  private final long segsOut;
  @SerializedName("SegsIn")
  private final long segsIn;
  @SerializedName("NotsentBytes")
  private final long notSentBytes;
  @SerializedName("MinRTT")
  private final long minRtt;
  @SerializedName("DataSegsIn")
  private final long dataSegsIn;
  @SerializedName("DataSegsOut")
  private final long dataSegsOut;
  @SerializedName("DeliveryRate")
  private final long deliveryRate;
  @SerializedName("BusyTime")
  private final long busyTime;
  @SerializedName("RWndLimited")
  private final long rWndLimited;
  @SerializedName("SndBufLimited")
  private final long sndBufLimited;
  @SerializedName("Delivered")
  private final long delivered;
  @SerializedName("DeliveredCE")
  private final long deliveredCE;
  @SerializedName("BytesSent")
  private final long bytesSent;
  @SerializedName("BytesRetrans")
  private final long bytesRetrans;
  @SerializedName("DSackDups")
  private final long dSackDups;
  @SerializedName("ReordSeen")
  private final long reordSeen;
  @SerializedName("ElapsedTime")
  private final long elapsedTime;

  public TcpInfo(long state, long caState, long retransmits, long probes, long backoff,
      long options, long wScale, long appLimited, long rto, long ato, long sndMss,
      long rcvMss, long unacked, long sacked, long lost, long retrans, long fackets,
      long lastDataSent, long lastAckSent, long lastDataRecv, long lastAckRecv, long pmtu,
      long rcvSsThresh, long rtt, long rttVar, long sndSsThresth, long sndCwnd, long advMss,
      long reordering, long rcvRtt, long rcvSpace, long totalRetrans, long pacingRate,
      long maxPacingRate, long bytesAcked, long bytesReceived, long segsOut, long segsIn,
      long notSentBytes, long minRtt, long dataSegsIn, long dataSegsOut, long deliveryRate,
      long busyTime, long rWndLimited, long sndBufLimited, long delivered, long deliveredCE,
      long bytesSent, long bytesRetrans, long dSackDups, long reordSeen, long elapsedTime) {
    this.state = state;
    this.caState = caState;
    this.retransmits = retransmits;
    this.probes = probes;
    this.backoff = backoff;
    this.options = options;
    this.wScale = wScale;
    this.appLimited = appLimited;
    this.rto = rto;
    this.ato = ato;
    this.sndMss = sndMss;
    this.rcvMss = rcvMss;
    this.unacked = unacked;
    this.sacked = sacked;
    this.lost = lost;
    this.retrans = retrans;
    this.fackets = fackets;
    this.lastDataSent = lastDataSent;
    this.lastAckSent = lastAckSent;
    this.lastDataRecv = lastDataRecv;
    this.lastAckRecv = lastAckRecv;
    this.pmtu = pmtu;
    this.rcvSsThresh = rcvSsThresh;
    this.rtt = rtt;
    this.rttVar = rttVar;
    this.sndSsThresth = sndSsThresth;
    this.sndCwnd = sndCwnd;
    this.advMss = advMss;
    this.reordering = reordering;
    this.rcvRtt = rcvRtt;
    this.rcvSpace = rcvSpace;
    this.totalRetrans = totalRetrans;
    this.pacingRate = pacingRate;
    this.maxPacingRate = maxPacingRate;
    this.bytesAcked = bytesAcked;
    this.bytesReceived = bytesReceived;
    this.segsOut = segsOut;
    this.segsIn = segsIn;
    this.notSentBytes = notSentBytes;
    this.minRtt = minRtt;
    this.dataSegsIn = dataSegsIn;
    this.dataSegsOut = dataSegsOut;
    this.deliveryRate = deliveryRate;
    this.busyTime = busyTime;
    this.rWndLimited = rWndLimited;
    this.sndBufLimited = sndBufLimited;
    this.delivered = delivered;
    this.deliveredCE = deliveredCE;
    this.bytesSent = bytesSent;
    this.bytesRetrans = bytesRetrans;
    this.dSackDups = dSackDups;
    this.reordSeen = reordSeen;
    this.elapsedTime = elapsedTime;
  }

  public long getState() {
    return state;
  }

  public long getCaState() {
    return caState;
  }

  public long getRetransmits() {
    return retransmits;
  }

  public long getProbes() {
    return probes;
  }

  public long getBackoff() {
    return backoff;
  }

  public long getOptions() {
    return options;
  }

  public long getwScale() {
    return wScale;
  }

  public long getAppLimited() {
    return appLimited;
  }

  public long getRto() {
    return rto;
  }

  public long getAto() {
    return ato;
  }

  public long getSndMss() {
    return sndMss;
  }

  public long getRcvMss() {
    return rcvMss;
  }

  public long getUnacked() {
    return unacked;
  }

  public long getSacked() {
    return sacked;
  }

  public long getLost() {
    return lost;
  }

  public long getRetrans() {
    return retrans;
  }

  public long getFackets() {
    return fackets;
  }

  public long getLastDataSent() {
    return lastDataSent;
  }

  public long getLastAckSent() {
    return lastAckSent;
  }

  public long getLastDataRecv() {
    return lastDataRecv;
  }

  public long getLastAckRecv() {
    return lastAckRecv;
  }

  public long getPmtu() {
    return pmtu;
  }

  public long getRcvSsThresh() {
    return rcvSsThresh;
  }

  public long getRtt() {
    return rtt;
  }

  public long getRttVar() {
    return rttVar;
  }

  public long getSndSsThresth() {
    return sndSsThresth;
  }

  public long getSndCwnd() {
    return sndCwnd;
  }

  public long getAdvMss() {
    return advMss;
  }

  public long getReordering() {
    return reordering;
  }

  public long getRcvRtt() {
    return rcvRtt;
  }

  public long getRcvSpace() {
    return rcvSpace;
  }

  public long getTotalRetrans() {
    return totalRetrans;
  }

  public long getPacingRate() {
    return pacingRate;
  }

  public long getMaxPacingRate() {
    return maxPacingRate;
  }

  public long getBytesAcked() {
    return bytesAcked;
  }

  public long getBytesReceived() {
    return bytesReceived;
  }

  public long getSegsOut() {
    return segsOut;
  }

  public long getSegsIn() {
    return segsIn;
  }

  public long getNotSentBytes() {
    return notSentBytes;
  }

  public long getMinRtt() {
    return minRtt;
  }

  public long getDataSegsIn() {
    return dataSegsIn;
  }

  public long getDataSegsOut() {
    return dataSegsOut;
  }

  public long getDeliveryRate() {
    return deliveryRate;
  }

  public long getBusyTime() {
    return busyTime;
  }

  public long getrWndLimited() {
    return rWndLimited;
  }

  public long getSndBufLimited() {
    return sndBufLimited;
  }

  public long getDelivered() {
    return delivered;
  }

  public long getDeliveredCE() {
    return deliveredCE;
  }

  public long getBytesSent() {
    return bytesSent;
  }

  public long getBytesRetrans() {
    return bytesRetrans;
  }

  public long getdSackDups() {
    return dSackDups;
  }

  public long getReordSeen() {
    return reordSeen;
  }

  public long getElapsedTime() {
    return elapsedTime;
  }

}
