package net.measurementlab.ndt7.android.utils;

import static java.util.concurrent.TimeUnit.MICROSECONDS;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

public abstract class Ndt7Constants {

  public static final long MEASUREMENT_INTERVAL = MICROSECONDS.convert(250, MILLISECONDS);
  public static final long MAX_RUN_TIME = MICROSECONDS.convert(10, SECONDS); // 10 seconds
  public static final int MAX_MESSAGE_SIZE = 16777216; // (1<<24) = 16MB
  public static final int MIN_MESSAGE_SIZE = 8192; // (1<<13)
  public static final long TEST_MAX_WAIT_TIME = 20L; // seconds
  public static final int MAX_QUEUE_SIZE = 16777216; // 16MB

}
