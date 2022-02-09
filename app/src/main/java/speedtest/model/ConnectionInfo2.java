package speedtest.model;

import com.google.gson.annotations.SerializedName;

public class ConnectionInfo2 {

    @SerializedName("ConnectionInfo")
    public ConnectionInfo connectionInfo;

    public class ConnectionInfo{
        @SerializedName("Client")
        public String clientIp;

        @SerializedName("Server")
        public String serverIp;

        @SerializedName("UUID")
        public String uuid;

    }
}
