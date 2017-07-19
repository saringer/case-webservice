package de.agdb.views.scheduler;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Riva on 18.07.2017.
 */
public class HelperClass {

    public Date getCurrentDatePlusOneYear() {
        // Date in two weeks up from now
        int noOfDays = 14; //i.e two weeks
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_YEAR, noOfDays);
        Date date = calendar.getTime();
        return date;
    }
}
