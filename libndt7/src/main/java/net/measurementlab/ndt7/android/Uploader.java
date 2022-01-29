package net.measurementlab.ndt7.android;

import static net.measurementlab.ndt7.android.NdtTest.TestType.UPLOAD;
import static net.measurementlab.ndt7.android.utils.Ndt7Constants.MAX_QUEUE_SIZE;
import static net.measurementlab.ndt7.android.utils.Ndt7Constants.MAX_RUN_TIME;
import static net.measurementlab.ndt7.android.utils.Ndt7Constants.MEASUREMENT_INTERVAL;
import static net.measurementlab.ndt7.android.utils.Ndt7Constants.MIN_MESSAGE_SIZE;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.gson.Gson;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Semaphore;
import net.measurementlab.ndt7.android.models.CallbackRegistry;
import net.measurementlab.ndt7.android.models.ClientResponse;
import net.measurementlab.ndt7.android.models.Measurement;
import net.measurementlab.ndt7.android.utils.DataConverter;
import net.measurementlab.ndt7.android.utils.PayloadTransformer;
import net.measurementlab.ndt7.android.utils.SocketFactory;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class Uploader extends WebSocketListener {

  private final CallbackRegistry callbackRegistry;
  private final ExecutorService executorService;
  private final Semaphore speedTestLock;
  private long startTime;
  private long previous;
  private double totalBytesSent;
  private final Gson gson = new Gson();

  public Uploader(
      @NonNull CallbackRegistry callbackRegistry,
      @NonNull ExecutorService executorService,
      @NonNull Semaphore speedTestLock
  ) {
    this.callbackRegistry = callbackRegistry;
    this.executorService = executorService;
    this.speedTestLock = speedTestLock;
  }

  @Override
  public void onMessage(@NonNull WebSocket webSocket, @NonNull String text) {
    try {
      Measurement measurement = gson.fromJson(text, Measurement.class);
      callbackRegistry.onMeasurementProgress(measurement);
    } catch (Exception e) {
      // we don't care that much if a single measurement has trouble
    }
  }

  @Override
  public void onClosing(WebSocket webSocket, int code, @NonNull String reason) {
    ClientResponse clientResponse = DataConverter.generateResponse(
        startTime,
        totalBytesSent - webSocket.queueSize(),
        UPLOAD
    );
    if (code == 1000) {
      callbackRegistry.onFinished(clientResponse, null, UPLOAD);
    } else {
      callbackRegistry.onFinished(clientResponse, new Error(reason), UPLOAD);
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
        DataConverter.generateResponse(
            startTime,
            totalBytesSent - webSocket.queueSize(),
            UPLOAD
        ),
        t,
        UPLOAD
    );

    releaseResources();
    webSocket.close(1001, null);
  }

  void beginUpload(@NonNull String url, @Nullable OkHttpClient httpClient) {
    WebSocket ws = SocketFactory.establishSocketConnection(url, httpClient, this);
    startTime = DataConverter.currentTimeInMicroseconds();
    previous = startTime;

    createBytePayloads(ws);
  }

  private void createBytePayloads(WebSocket ws) {
    long timerStart = DataConverter.currentTimeInMicroseconds();
    long elapsedTime = DataConverter.currentTimeInMicroseconds() - timerStart;
    ByteString bytes = ByteString.of(new byte[MIN_MESSAGE_SIZE]); /* (1<<13) */

    // only allow this to run for 10 seconds, then stop
    while (elapsedTime < MAX_RUN_TIME) {
      bytes = PayloadTransformer.performDynamicTuning(bytes, ws.queueSize(), totalBytesSent);
      sendToWebSocket(bytes, ws);
      elapsedTime = DataConverter.currentTimeInMicroseconds() - timerStart;
    }
  }

  private void sendToWebSocket(ByteString data, WebSocket ws) {
    while (ws.queueSize() + data.size() < MAX_QUEUE_SIZE) {
      ws.send(data);
      totalBytesSent += data.size();
    }
    tryToUpdateUpload(totalBytesSent, ws);
  }

  private void tryToUpdateUpload(double total, WebSocket ws) {
    long now = DataConverter.currentTimeInMicroseconds();

    // if we haven't sent an update in 250ms, lets send one
    if (now - previous > MEASUREMENT_INTERVAL) {
      previous = now;
      callbackRegistry.onSpeedTestProgress(
          DataConverter.generateResponse(
              startTime,
              total - ws.queueSize(),
              UPLOAD
          )
      );
    }
  }

  private void releaseResources() {
    speedTestLock.release();
    executorService.shutdown();
  }

}
