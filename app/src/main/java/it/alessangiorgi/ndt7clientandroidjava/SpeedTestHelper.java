package it.alessangiorgi.ndt7clientandroidjava;

import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import java.util.Arrays;
import java.util.Locale;

import okhttp3.WebSocket;
import speedtest.Settings;
import speedtest.SpeedTestClient;
import speedtest.model.ConnectionInfo2;
import speedtest.model.Measurement;

public class SpeedTestHelper {
    private static final int SSL_PORT = 443;
    private String TAG = getClass().getSimpleName();
    private OnData callback;
    private HelperClient helperClient;

    public SpeedTestHelper() {
    }

    public void setOnDataListener(OnData listener) {
        callback = listener;
    }

    public void runSpeedTest(String download, String upload) {
        Settings settings = new Settings(download,upload, SSL_PORT, true);
        helperClient = new HelperClient(settings);
        helperClient.runSpeedTest();
    }

    public void cancel() {
        if (helperClient != null) {
            helperClient.cancel();
        }
    }

    public interface OnData {
        void onPing(int ping);

        void onJitter(double jitter);

        void onDownload(double download);

        void onUpload(double upload);

        void onProviderIp(String providerIp);

        void onFinished();

        void onFinishedDownload();

        void onError();
    }

    private class HelperClient extends SpeedTestClient {

        private String TAG = getClass().getSimpleName();
        private int dc;
        private int uc;
        private boolean isPinged;
        private boolean isUploadStarted;
        private boolean isInfoRetrieved;

        private HelperClient(@NonNull Settings settings) {
            super(settings);
            dc = 0;
            uc = 0;
            isPinged = false;
            isInfoRetrieved = false;
            isUploadStarted = false;
        }
        private String convertArrayToStringMethod_IPv6(String[] strArray) {
            StringBuilder stringBuilder = new StringBuilder();
            for (String s : strArray) {
                stringBuilder.append(s);
                stringBuilder.append(":");
            }
            stringBuilder.setLength(stringBuilder.length()-1);
            return stringBuilder.toString()
                    .replace("[","")
                    .replace("]","");
        }



        @Override
        public void onServerInfo(@NonNull ConnectionInfo2 info) {
            super.onServerInfo(info);

            if (!isInfoRetrieved){
                String providerIpRaw = info.connectionInfo.clientIp;
                //String providerIpRaw = "[2001:b07:6469:6b8e:b499:99fd:e954:443d:4455]";

                String[] provIpSplittedWPort = providerIpRaw.split(":");

                String[] providerIpArray = Arrays.copyOf(provIpSplittedWPort, provIpSplittedWPort.length-1);

                String providerIp = convertArrayToStringMethod_IPv6(providerIpArray);

                callback.onProviderIp(providerIp);
                isInfoRetrieved = true;
            }
        }

        @Override
        public void onServerDownloadMeasurement(@NonNull Measurement measurement) {
            super.onServerDownloadMeasurement(measurement);

            long bytes = measurement.appInfo != null ? measurement.appInfo.num_bytes : -1;
            Log.e(TAG, " rttVar=" + measurement.tcpInfo.rtt_var +
                    " band= " + measurement.bbrInfo.max_bw +
                    " bytes= " + bytes +
                    " elapsed= " + measurement.bbrInfo.elapsed_time);
            if (!isPinged) {
                double min_rtt_sec = measurement.tcpInfo.rtt/1000;
                callback.onPing((int)min_rtt_sec);
                isPinged = true;
            }

            if (!isUploadStarted) {
                if (callback != null) {
                    double jitter_sec = measurement.tcpInfo.rtt_var/1000.0;
                    Log.e(TAG, "onServerDownloadMeasurement: JITTER " + jitter_sec );
                    callback.onJitter(jitter_sec);
                }
            }
        }

        @Override
        public void onClientMeasurement(double elapsed, long count, boolean isDownload) {
            super.onClientMeasurement(elapsed, count, isDownload);
            if (isDownload && elapsed >= 1000000) {
//                    double downloadSpeed = (count / elapsed) / 125000.0;
//                    double downloadSpeed = (count * 8e-6 ) / (int)elapsed ;
                double downloadSpeed = 8*count/ 1000f / 1000f / (elapsed/1000000f) ;
                String downloadSpeedText = String.format(Locale.getDefault(), "%.2f MBit", downloadSpeed);
                Log.e(TAG, "onClientMeasurement: download #" + dc + "   " + downloadSpeedText);
                dc++;
                if(!Double.isNaN(downloadSpeed))
                    callback.onDownload(downloadSpeed);
            }
            else if (elapsed > 1 && !isDownload) {
                if (!isUploadStarted){
                    callback.onFinishedDownload();
                    isUploadStarted = true;
                    SystemClock.sleep(2000);
                }

                double upload =  8*count/ 1000f / 1000f / (elapsed/1000000f) ;

                String uploadSpeedText = String.format(Locale.getDefault(), "%.2f MBit/s", upload);
                Log.e(TAG, "onClientMeasurement: upload #" + uc + "   " + uploadSpeedText  + " ----"+upload);
                uc++;
                callback.onUpload(upload);

            }
        }

        @Override
        public void onClosed(@NonNull WebSocket webSocket, int code, @NonNull String reason) {
            super.onClosed(webSocket, code, reason);
            Log.e(TAG, "onClosed: " + reason);
            callback.onFinished();
        }

        @Override
        public void onError(@Nullable String error) {
            super.onError(error);
            try {
                Log.d(TAG, "onError() returned: " + error);
                if (!error.equals("Connection reset"))
                    callback.onError();
                else {
                    callback.onFinished();
                }
            } catch (NullPointerException ex){
                ex.printStackTrace();
                callback.onFinished();
            }
        }
    }

}
