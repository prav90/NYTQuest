package com.codepath.nytquest.utils;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by rpraveen on 6/25/16.
 */
public class DateHelper {
  public static final SimpleDateFormat FORMATTER =
  new SimpleDateFormat("yyyy-mm-dd");
  public static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-mm-dd");
  public static final SimpleDateFormat TIME_FORMATTER = new SimpleDateFormat("h:mm a");
  public static Date getCurrentSQLDate() {
    java.util.Date utilDate = new java.util.Date();
    return new Date(utilDate.getTime());
  }

  public static Date parseDate(String DateTime) {
    java.util.Date utilDate  = new java.util.Date();
    try {
      utilDate = FORMATTER.parse(DateTime);
    } catch(Exception ex) {};
    return new Date(utilDate.getTime());
  }

  public static String getFormattedTime(int hour, int minute) {
    Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.HOUR_OF_DAY, hour);
    calendar.set(Calendar.MINUTE, minute);
    return TIME_FORMATTER.format(calendar.getTime());
  }

  public static String getFormatteDate(int year, int month, int day) {
    Calendar calendar = Calendar.getInstance();
    calendar.set(year, month, day);
    return DATE_FORMATTER.format(calendar.getTime());
  }
}