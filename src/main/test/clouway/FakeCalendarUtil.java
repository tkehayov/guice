package clouway;

import java.util.Calendar;

/**
 * @author Tihomir Kehayov <kehayov89@gmail.com>
 */
public class FakeCalendarUtil {
  public Long dateOf(int year, int month, int day) {
    Calendar calendar = Calendar.getInstance();

    calendar.set(Calendar.MONTH, month - 1);
    calendar.set(Calendar.DAY_OF_MONTH, day);
    calendar.set(Calendar.YEAR, year);

    return calendar.getTimeInMillis();
  }
}
