package speedtest.model;

import com.google.gson.annotations.SerializedName;

public class Measurement {

    @SerializedName("TCPInfo")
    public TcpInfo tcpInfo;

    @SerializedName("AppInfo")
    public AppInfo appInfo;

    @SerializedName("BBRInfo")
    public BBRInfo bbrInfo;

    public static class AppInfo {
        @SerializedName("ElapsedTime")
        public long elapsed_time;
        @SerializedName("NumBytes")
        public long num_bytes;

    }
    public class BBRInfo {
        @SerializedName("ElapsedTime")
        public long elapsed_time;
        @SerializedName("MaxBandwidth")
        public long max_bw;
        @SerializedName("MinRTT")
        public int min_rtt;

    }
    public class TcpInfo {
        @SerializedName("BytesAcked")
        public int bytes_acked;
        @SerializedName("BytesReceived")
        public long bytes_received;
        @SerializedName("BytesSent")
        public double bytes_sent;
        @SerializedName("BytesRetrans")
        public double bytes_retrans;
        @SerializedName("ElapsedTime")
        public int elapsed_time;
        @SerializedName("MinRTT")
        public int min_rtt;
        @SerializedName("RTT")
        public double rtt;
        @SerializedName("RTTVar")
        public int rtt_var;
        @SerializedName("RWndLimited")
        public double rwnd_limited;
        @SerializedName("SndBufLimited")
        public double sndbuf_limited;



    }
}

