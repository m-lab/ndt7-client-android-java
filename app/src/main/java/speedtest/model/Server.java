package speedtest.model;

import com.google.gson.annotations.SerializedName;


public class Server {


    @SerializedName("machine")
    public String machine;

    @SerializedName("location")
    public Location location;

    @SerializedName("urls")
    public Urls urls;

    public class Location {
        @SerializedName("city")
        public String city;
        @SerializedName("country")
        public String country;
    }

    public class Urls {
        @SerializedName("ws:///ndt/v7/download")
        public String wsDownload;
        @SerializedName("ws:///ndt/v7/upload")
        public String wsUpload;
        @SerializedName("wss:///ndt/v7/download")
        public String wssDownload;
        @SerializedName("wss:///ndt/v7/upload")
        public String wssUpload;

    }

}
