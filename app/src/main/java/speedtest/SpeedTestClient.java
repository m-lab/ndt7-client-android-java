package speedtest;


import android.util.Log;

import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;

import java.security.cert.X509Certificate;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;
import speedtest.model.ConnectionInfo2;
import speedtest.model.Measurement;



public class SpeedTestClient extends WebSocketListener {

    private final static int HTTP101 = 101;
    private final static int TEST_TIME = 10;
    private final static int CLOSE_SUCCESS = 1000;
    @NonNull
    private static final String TAG = "Client";
    private long count = 0;
    private boolean isDownload = false;
    @NonNull
    private final Settings settings;
    private final long measurementInterval = TimeUnit.NANOSECONDS.convert(250, TimeUnit.MILLISECONDS);
    private double elapsed = 0;
    private Gson gson = new Gson();
    private boolean rv = true;
    private long t0 = 0;
    private long tLast = 0;
    private WebSocket webSocket;

    public SpeedTestClient(@NonNull Settings settings) {
        this.settings = settings;
    }

    public boolean runSpeedTest() {
        return runDownload();
    }

    private void onLogInfo(@Nullable String message) {
        Log.d(TAG, "onLogInfo: " + message);
    }

    public void onError(@Nullable String error) {
        Log.d(TAG, "onError: " + error);
        if (webSocket != null)
            webSocket.close(1000,"error");
        count = 0;
        elapsed = 0;
    }

    public void onServerDownloadMeasurement(@NonNull Measurement measurement) {
        Log.d(TAG, "onServerDownloadMeasurement: " + measurement);
    }

    public void onClientMeasurement(double elapsed, long count, boolean isDownload) {
        Log.d(TAG, "onClientDownloadMeasurement numbytes: " + count);
        Log.d(TAG, "onClientDownloadMeasurement elapsed: " + elapsed);
    }

    public void onServerInfo(@NonNull ConnectionInfo2 info){}


    @Override
    public final void onOpen(WebSocket ws, Response resp) {
        webSocket = ws;
        onLogInfo("WebSocket onOpen");
        onLogInfo(resp.toString());
        if (resp.code() == HTTP101 && !isDownload) UploadData(ws);
    }

    @Override
    public final void onMessage(WebSocket ws,
                                String text) {
        onLogInfo("WebSocket onMessage");
        Measurement measurement;
        measurement = gson.fromJson(text, Measurement.class);
        if(isDownload) {
            count += text.getBytes().length;
            ConnectionInfo2 info = gson.fromJson(text, ConnectionInfo2.class);
            if (info.connectionInfo != null && info.connectionInfo.clientIp != null) {
                Log.e(TAG, "ip_object: " + info.connectionInfo.clientIp);
                onServerInfo(info);
            }

            onServerDownloadMeasurement(measurement);
        }else {
            count = measurement.tcpInfo.bytes_received;
        }
        elapsed = measurement.tcpInfo.elapsed_time;
        periodic();
    }

    @Override
    public final void onMessage(WebSocket ws,
                                ByteString bytes) {
        onLogInfo("WebSocket onMessage");
        count += bytes.size();
        periodic();
    }


    @Override
    public final void onClosing(WebSocket ws,
                                int code,
                                String reason) {
        Log.d(TAG, "WebSocket closed");
        if (isDownload) {
            isDownload = false;
            elapsed = 0;
            count = 0;
            if (!runUpload()) onLogInfo("RunUpload Failed");
        }
    }

    @Override
    public final void onFailure(WebSocket ws,
                                Throwable t,
                                Response r) {
        onError(t.getMessage());
        rv = false;
    }

    @CheckResult
    private boolean runUpload() {
        openWs("upload");

        t0 = tLast = System.nanoTime();

        return rv;
    }

    @CheckResult
    private boolean runDownload() {
        isDownload = true;

        openWs("download");

        t0 = tLast = System.nanoTime();
        return rv;
    }

    //ToDo find the best compromise to upload datas
    private void UploadData(WebSocket ws) {
        new Thread(() -> {
            long totalTime = TEST_TIME * 1000000;
            boolean toFinish = false;
            while (!toFinish) {
                byte[] dataChunk = new byte[1 << 18];
                new Random().nextBytes(dataChunk);
                //Ws max queuesize is 16mb
                if (ws.queueSize() < dataChunk.length) {
                    ws.send(ByteString.of(dataChunk));
                }
                toFinish = (elapsed >= totalTime);
            }
            ws.close(CLOSE_SUCCESS, "Upload completed !");

        }).start();
    }

    private void openWs(String subtest) {

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (settings.isSkipTlsCertificateVerification()) {
            X509TrustManager x509TrustManager = new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain,
                                               String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain,
                                               String authType) {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[]{};
                }
            };

            try {
                final TrustManager[] trustAllCerts = new TrustManager[]{x509TrustManager};
                final SSLContext sslContext = SSLContext.getInstance("SSL");
                sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
                final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
                builder.sslSocketFactory(sslSocketFactory, x509TrustManager);
            } catch (Exception e) {
                Log.e(TAG, "Encountered exception", e);
            }

            builder.hostnameVerifier((hostname, session) -> true);
        }

        OkHttpClient client = builder
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();

        String server = null;
        if(subtest.equals("download"))
            server = settings.getHostnameDownload();
        else
            server = settings.getHostnameUpload();
        Log.e(TAG, "openWs: URI " + server);

        Request request = new Request.Builder()
                .addHeader("User-Agent", "ooniprobe/3.0.0 ndt7-client-go/0.1.0")
                .addHeader("Connection", "Upgrade")
                .addHeader("Upgrade", "websocket")
                .url(server)
                .addHeader("Sec-WebSocket-Protocol", "net.measurementlab.ndt.v7")
                .build();

        client.newWebSocket(request, this);

    }

    private void periodic() {
        long now = System.nanoTime();

        if (now - tLast > measurementInterval) {
            tLast = now;
            onClientMeasurement(elapsed, count, isDownload);
        }
    }

    public void cancel() {
        if (webSocket != null) {
            webSocket.cancel();
        }
    }
}



