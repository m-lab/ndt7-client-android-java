package net.measurementlab.ndt7.android.utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public abstract class SocketFactory {

  public static final String WEB_SOCKET_PROTOCOL_HEADER = "Sec-WebSocket-Protocol";
  public static final String WEB_SOCKET_PROTOCOL_VALUE = "net.measurementlab.ndt.v7";

  /**
   * Establishes a web socket with an NDT server.
   */
  public static @NonNull WebSocket establishSocketConnection(
      @NonNull String url,
      @Nullable OkHttpClient httpClient,
      @NonNull WebSocketListener listener
  ) {
    Request request = new Builder()
        .url(url)
        .addHeader(WEB_SOCKET_PROTOCOL_HEADER, WEB_SOCKET_PROTOCOL_VALUE)
        .build();

    if (httpClient != null) {
      return httpClient.newWebSocket(request, listener);
    }
    throw new Error("Unable to create socket");
  }

}
