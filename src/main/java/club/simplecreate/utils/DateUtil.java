package club.simplecreate.utils;

import java.util.Calendar;

public class DateUtil {

    static {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(2020,2,1);
        millis = calendar.getTimeInMillis();
    }
    public static final long millis ;


}
