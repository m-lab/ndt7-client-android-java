package net.measurementlab.ndt7.android;

import static net.measurementlab.ndt7.android.BuildConfig.NDT7_ANDROID_VERSION_NAME;
import static net.measurementlab.ndt7.android.utils.Ndt7Constants.TEST_MAX_WAIT_TIME;
import static java.util.concurrent.TimeUnit.SECONDS;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import net.measurementlab.ndt7.android.models.CallbackRegistry;
import net.measurementlab.ndt7.android.models.ClientResponse;
import net.measurementlab.ndt7.android.models.HostnameResponse;
import net.measurementlab.ndt7.android.models.Measurement;
import net.measurementlab.ndt7.android.models.Urls;
import net.measurementlab.ndt7.android.utils.HttpClientFactory;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public abstract class NdtTest implements DataPublisher {

    public enum TestType {
        UPLOAD,
        DOWNLOAD,
        DOWNLOAD_AND_UPLOAD
    }

    private final OkHttpClient httpClient;

    public static final String MEASUREMENT_SERVER_URL =
            "https://locate.measurementlab.net/v2/nearest/ndt/ndt7?client_name=ndt7-android&client_version=%s";
    public static final String METHOD_GET = "GET";

    private Downloader downloader;
    private ExecutorService executorService;
    private final Semaphore runLock = new Semaphore(1);

    public NdtTest() {
        this(null);
    }

    public NdtTest(OkHttpClient httpClient) {
        if (httpClient == null) {
            httpClient = HttpClientFactory.createHttpClient();
        }
        this.httpClient = httpClient;
    }

    @Override
    public void onDownloadProgress(@NonNull ClientResponse clientResponse) {
        Log.d("NdtTest", "downloading...");
    }

    @Override
    public void onMeasurementDownloadProgress(@NonNull Measurement measurement) {
        // Do nothing
    }

    @Override
    public void onUploadProgress(@NonNull ClientResponse clientResponse) {
        Log.d("NdtTest", "uploading...");
    }

    @Override
    public void onMeasurementUploadProgress(@NonNull Measurement measurement) {
        // Do nothing
    }

    public void startTest(@NonNull TestType testType) {
        if (!runLock.tryAcquire()) {
            return;
        }
        Semaphore speedTestLock = new Semaphore(1);
        executorService = Executors.newSingleThreadScheduledExecutor();

        getHostname().enqueue(
                new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        onFinished(null, e, testType);
                        if (executorService != null) {
                            executorService.shutdown();
                        }
                        runLock.release();
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) {
                        try {
                            HostnameResponse hostInfo = new Gson().fromJson(
                                    response.body().string(),
                                    HostnameResponse.class
                            );
                            int numUrls = hostInfo.getResults().size();
                            for (int i = 0; i < numUrls; i++) {
                                try {
                                    selectTestType(testType, hostInfo.getResults().get(i).getUrls(), speedTestLock);
                                    return;
                                } catch (Exception e) {
                                    if (i == numUrls - 1) {
                                        throw e;
                                    }
                                }
                            }
                        } catch (Exception e) {
                            onFinished(null, e, testType);
                            if (executorService != null) {
                                executorService.shutdown();
                            }
                            runLock.release();
                        }
                    }
                }
        );
    }

    public void stopTest() {
        if (downloader != null) {
            downloader.cancel();
        }
        if (executorService != null) {
            executorService.shutdown();
        }
        runLock.release();
    }

    private void selectTestType(TestType testType, Urls urls, Semaphore speedTestLock)
            throws InterruptedException {
        if (executorService != null) {
            switch (testType) {
                case DOWNLOAD:
                    executorService.submit(
                            () -> startDownload(urls.getNdt7DownloadWss(), executorService, speedTestLock)
                    );
                    break;
                case UPLOAD:
                    speedTestLock.release();
                    executorService.submit(
                            () -> startUpload(urls.getNdt7UploadWss(), executorService, speedTestLock)
                    );
                    break;
                case DOWNLOAD_AND_UPLOAD:
                    executorService.submit(
                            () -> startDownload(urls.getNdt7DownloadWss(), executorService, speedTestLock)
                    );
                    executorService.submit(
                            () -> startUpload(urls.getNdt7UploadWss(), executorService, speedTestLock)
                    );
                    break;
            }
            executorService.awaitTermination(TEST_MAX_WAIT_TIME * 2, SECONDS);
        }
        runLock.release();
    }

    private void startUpload(String url, ExecutorService executorService, Semaphore speedTestLock) {
        try {
            speedTestLock.tryAcquire(TEST_MAX_WAIT_TIME, SECONDS);
            CallbackRegistry callbackRegistry = new CallbackRegistry(
                    this::onUploadProgress,
                    this::onMeasurementUploadProgress,
                    this::onFinished
            );

            new Uploader(callbackRegistry, executorService, speedTestLock).beginUpload(url, httpClient);
        } catch (InterruptedException e) {
            // Do nothing
        }
    }

    private void startDownload(String url, ExecutorService executorService, Semaphore speedTestLock) {
        try {
            speedTestLock.tryAcquire(TEST_MAX_WAIT_TIME, SECONDS);
            CallbackRegistry callbackRegistry = new CallbackRegistry(
                    this::onDownloadProgress,
                    this::onMeasurementDownloadProgress,
                    this::onFinished
            );

            downloader = new Downloader(callbackRegistry, executorService, speedTestLock);
            downloader.beginDownload(url, httpClient);
        } catch (InterruptedException e) {
            // Do nothing
        }
    }

    private Call getHostname() {
        String locateServerUrl = String.format(MEASUREMENT_SERVER_URL, NDT7_ANDROID_VERSION_NAME);
        Request request = (new Request.Builder())
                .method(METHOD_GET, null)
                .url(locateServerUrl)
                .build();

        return httpClient.newCall(request);
    }

}
