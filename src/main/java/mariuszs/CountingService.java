package mariuszs;

import javax.inject.Named;
import java.util.concurrent.TimeUnit;

/**
 * A simple service that can increment a number.
 */
@Named("CountingService")
public class CountingService {
  /**
   * Increment the given number by one.
   */
  public int increment(int count) {
      try {
          TimeUnit.SECONDS.sleep(2);
      } catch (InterruptedException e) {
          e.printStackTrace();
      }
      return count + 1;
  }
}
