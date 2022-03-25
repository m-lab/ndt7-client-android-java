package net.measurementlab.ndt7.android;

import static net.measurementlab.ndt7.android.NdtTest.TestType.DOWNLOAD;
import static net.measurementlab.ndt7.android.utils.Ndt7Constants.MEASUREMENT_INTERVAL;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.gson.Gson;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Semaphore;
import net.measurementlab.ndt7.android.models.CallbackRegistry;
import net.measurementlab.ndt7.android.models.ClientResponse;
import net.measurementlab.ndt7.android.models.Measurement;
import net.measurementlab.ndt7.android.utils.DataConverter;
import net.measurementlab.ndt7.android.utils.SocketFactory;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class Downloader extends WebSocketListener {

  private final CallbackRegistry callbackRegistry;
  private final ExecutorService executorService;
  private final Semaphore speedTestLock;
  private long startTime;
  private long previous;
  private double numBytes;
  private final Gson gson = new Gson();
  private WebSocket webSocket;

  public Downloader(
      @NonNull CallbackRegistry callbackRegistry,
      @NonNull ExecutorService executorService,
      @NonNull Semaphore speedTestLock
  ) {
    this.callbackRegistry = callbackRegistry;
    this.executorService = executorService;
    this.speedTestLock = speedTestLock;
  }

  @Override
  public void onOpen(@NonNull WebSocket webSocket, @NonNull Response response) {
    startTime = DataConverter.currentTimeInMicroseconds();
  }

  @Override
  public void onMessage(@NonNull WebSocket webSocket, @NonNull String text) {
    numBytes += text.length();
    tryToUpdateClient();

    try {
      Measurement measurement = gson.fromJson(text, Measurement.class);
      callbackRegistry.onMeasurementProgress(measurement);
    } catch (Exception e) {
      // Do nothing
    }
  }

  @Override
  public void onMessage(@NonNull WebSocket webSocket, @NonNull ByteString bytes) {
    numBytes += bytes.size();
    tryToUpdateClient();
  }

  @Override
  public void onClosing(@NonNull WebSocket webSocket, int code, @NonNull String reason) {
    ClientResponse clientResponse = DataConverter.generateResponse(startTime, numBytes, DOWNLOAD);
    if (code == 1000) {
      callbackRegistry.onFinished(clientResponse, null, DOWNLOAD);
    } else {
      callbackRegistry.onFinished(clientResponse, new Error(reason), DOWNLOAD);
    }

    releaseResources();
    webSocket.close(1000, null);
  }

  @Override
  public void onFailure(
      @NonNull WebSocket webSocket,
      @NonNull Throwable t,
      @Nullable Response response
  ) {
    callbackRegistry.onFinished(
        DataConverter.generateResponse(startTime, numBytes, DOWNLOAD),
        t,
        DOWNLOAD
    );
    releaseResources();
    webSocket.close(1001, null);
  }

  public void beginDownload(@NonNull String url, @Nullable OkHttpClient httpClient) {
    webSocket = SocketFactory.establishSocketConnection(url, httpClient, this);
  }

  public void cancel() {
    webSocket.cancel();
    releaseResources();
  }

  private void tryToUpdateClient() {
    long now = DataConverter.currentTimeInMicroseconds();

    // if we haven't sent an update in 250ms, lets send one
    if (now - previous > MEASUREMENT_INTERVAL) {
      callbackRegistry.onSpeedTestProgress(DataConverter.generateResponse(startTime, numBytes, DOWNLOAD));
      previous = now;
    }
  }

  private void releaseResources() {
    speedTestLock.release();
    executorService.shutdown();
  }

}
