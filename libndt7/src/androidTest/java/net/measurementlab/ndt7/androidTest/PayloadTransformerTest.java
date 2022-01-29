package net.measurementlab.ndt7.androidTest;

import static net.measurementlab.ndt7.android.utils.Ndt7Constants.MAX_MESSAGE_SIZE;
import static org.junit.Assert.assertEquals;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import net.measurementlab.ndt7.android.utils.PayloadTransformer;
import okio.ByteString;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class PayloadTransformerTest {

  @Test
  public void testDynamicTuningDoesNotChangeIfMaxSize() {
    ByteString oldBytes = ByteString.of(new byte[MAX_MESSAGE_SIZE]); /* (1<<13) */
    ByteString newBytes = PayloadTransformer.performDynamicTuning(oldBytes, 0L, 0.0);
    assertEquals(newBytes.size(), oldBytes.size());
  }

  @Test
  public void testDynamicTuningDoesNotChangeIfQueueIsSaturated() {
    ByteString oldBytes = ByteString.of(new byte[1000]); /* (1<<13) */
    ByteString newBytes = PayloadTransformer.performDynamicTuning(oldBytes, 0L, 16000.0);
    assertEquals(newBytes.size(), oldBytes.size());
  }

  @Test
  public void testDynamicTuningWillDouble() {
    ByteString oldBytes = ByteString.of(new byte[10]); /* (1<<13) */
    ByteString newBytes = PayloadTransformer.performDynamicTuning(oldBytes, 10000L, 16000.0);
    assertEquals(newBytes.size(), oldBytes.size() * 2);
  }

}
