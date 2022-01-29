package net.measurementlab.ndt7impl.android;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import net.measurementlab.ndt7.android.NdtTest;
import net.measurementlab.ndt7.android.models.ClientResponse;
import net.measurementlab.ndt7.android.models.Measurement;
import net.measurementlab.ndt7.android.utils.DataConverter;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;

import okhttp3.OkHttpClient;

public class MainActivity extends Activity {

    class NDTTestImpl extends NdtTest {

        private TestType testType;

        public NDTTestImpl(@Nullable OkHttpClient httpClient) {
            super(httpClient);
        }

        @Override
        public void startTest(@NonNull TestType testType) {
            super.startTest(testType);
            this.testType = testType;
        }

        @Override
        public void onDownloadProgress(@NotNull ClientResponse clientResponse) {
            super.onDownloadProgress(clientResponse);
            runOnUiThread(() -> showDownloadProgress(clientResponse));
        }

        @Override
        public void onMeasurementDownloadProgress(@NotNull Measurement measurement) {
            super.onMeasurementDownloadProgress(measurement);
            System.out.println("Measurement download Progress: " + measurement);
        }

        @Override
        public void onUploadProgress(@NotNull ClientResponse clientResponse) {
            super.onUploadProgress(clientResponse);
            runOnUiThread(() -> showUploadProgress(clientResponse));
        }

        @Override
        public void onMeasurementUploadProgress(@NotNull Measurement measurement) {
            super.onMeasurementUploadProgress(measurement);
            System.out.println("Measurement upload Progress: " + measurement);
        }

        @Override
        public void onFinished(
                @Nullable ClientResponse clientResponse,
                @Nullable Throwable error,
                @NotNull TestType testType
        ) {
            assert clientResponse != null;
            System.out.println("Done Progress: " + DataConverter.convertToMbps(clientResponse));
            if (this.testType == TestType.DOWNLOAD_AND_UPLOAD && testType == TestType.DOWNLOAD) {
                return;
            }
            runOnUiThread(() -> toggleEnabledButtons(false));
        }
    }

    private final DecimalFormat decimalFormat = new DecimalFormat("#.00");
    private TextView downloadProgressTextView;
    private TextView uploadProgressTextView;
    private Button downloadButton;
    private Button uploadButton;
    private Button bothButton;
    private Button stopButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        downloadProgressTextView = findViewById(R.id.download_progress_text_view);
        uploadProgressTextView = findViewById(R.id.upload_progress_text_view);
        downloadButton = findViewById(R.id.download_button);
        uploadButton = findViewById(R.id.upload_button);
        bothButton = findViewById(R.id.both_button);
        stopButton = findViewById(R.id.stop_button);

        NDTTestImpl ndtTestImpl = new NDTTestImpl(null);

        downloadButton.setOnClickListener(v -> {
            ndtTestImpl.startTest(NdtTest.TestType.DOWNLOAD);
            toggleEnabledButtons(true);
        });
        uploadButton.setOnClickListener(v -> {
            ndtTestImpl.startTest(NdtTest.TestType.UPLOAD);
            toggleEnabledButtons(true);
        });
        bothButton.setOnClickListener(v -> {
            ndtTestImpl.startTest(NdtTest.TestType.DOWNLOAD_AND_UPLOAD);
            toggleEnabledButtons(true);
        });
        stopButton.setOnClickListener(v -> {
            ndtTestImpl.stopTest();
            toggleEnabledButtons(false);
        });
    }

    private void toggleEnabledButtons(boolean testIsRunning) {
        downloadButton.setEnabled(!testIsRunning);
        uploadButton.setEnabled(!testIsRunning);
        bothButton.setEnabled(!testIsRunning);
        stopButton.setEnabled(testIsRunning);
        if (testIsRunning) {
            downloadProgressTextView.setText("");
            uploadProgressTextView.setText("");
        }
    }

    private String formatProgress(ClientResponse clientResponse) {
        return String.format(
                "%s Mbps",
                decimalFormat.format(DataConverter.convertToMbps(clientResponse))
        );
    }

    private void showDownloadProgress(ClientResponse clientResponse) {
        String speed = formatProgress(clientResponse);
        System.out.println("Download Progress: " + speed);
        downloadProgressTextView.setText(speed);
    }

    private void showUploadProgress(ClientResponse clientResponse) {
        String speed = formatProgress(clientResponse);
        System.out.println("Upload Progress: " + speed);
        uploadProgressTextView.setText(speed);
    }
}