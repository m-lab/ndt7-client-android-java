package net.measurementlab.ndt7.androidTest;

import static net.measurementlab.ndt7.android.NdtTest.TestType.DOWNLOAD;
import static net.measurementlab.ndt7.android.NdtTest.TestType.DOWNLOAD_AND_UPLOAD;
import static net.measurementlab.ndt7.android.NdtTest.TestType.UPLOAD;
import static org.junit.Assert.assertTrue;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import net.measurementlab.ndt7.android.NdtTest;
import net.measurementlab.ndt7.android.models.ClientResponse;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class Ndt7HealthTest {

  private static class NdtTestImpl extends NdtTest {

    private final NdtWrapper dataWrapper = new NdtWrapper();
    private final Gson gson = new Gson();

    @Override
    public void onDownloadProgress(@NonNull ClientResponse clientResponse) {
      super.onDownloadProgress(clientResponse);
      dataWrapper.downloadSpeedAppNdt7.add(gson.toJson(clientResponse));
    }

    @Override
    public void onUploadProgress(@NonNull ClientResponse clientResponse) {
      super.onUploadProgress(clientResponse);
      dataWrapper.uploadSpeedAppNdt7.add(gson.toJson(clientResponse));
    }

    @Override
    public void onFinished(
        @Nullable ClientResponse clientResponse,
        @Nullable Throwable error,
        @NonNull TestType testType
    ) {
      if (error != null) {
        error.printStackTrace();
      }

      if (testType == UPLOAD) {
        dataWrapper.uploadFinished = true;
      }
      if (testType == DOWNLOAD) {
        dataWrapper.downloadFinished = true;
      }
    }
  }

  private static class NdtWrapper {
    @SerializedName("downloadSpeedAppNDT7")
    private final ArrayList<String> downloadSpeedAppNdt7 = new ArrayList<>();
    @SerializedName("uploadSpeedAppNDT7")
    private final ArrayList<String> uploadSpeedAppNdt7 = new ArrayList<>();
    @SerializedName("downloadFinished")
    private Boolean downloadFinished = false;
    @SerializedName("uploadFinished")
    private Boolean uploadFinished = false;
  }

  @Test
  public void testNetworkCallableCanPerformNdt7SpeedTest() throws InterruptedException {
    NdtTestImpl client = new NdtTestImpl();
    client.startTest(DOWNLOAD_AND_UPLOAD);

    // let speed test run for a bit
    Thread.sleep(120000L);

    assertTrue(client.dataWrapper.downloadSpeedAppNdt7.size() > 5);
    assertTrue(client.dataWrapper.uploadSpeedAppNdt7.size() > 5);
    assertTrue(client.dataWrapper.uploadFinished);
    assertTrue(client.dataWrapper.downloadFinished);
  }

  @Test
  public void testNetworkCallableCanContinueSpeedTestDespiteError()
      throws InterruptedException {

    NdtTestImpl client = new NdtTestImpl();
    client.startTest(DOWNLOAD_AND_UPLOAD);
    client.onFinished(null, new Throwable("test", null), DOWNLOAD);
    // let speed test run for a bit
    Thread.sleep(120000L);

    assertTrue(client.dataWrapper.downloadSpeedAppNdt7.size() > 5);
    assertTrue(client.dataWrapper.uploadSpeedAppNdt7.size() > 5);
    assertTrue(client.dataWrapper.uploadFinished);
    assertTrue(client.dataWrapper.downloadFinished);
  }

}
