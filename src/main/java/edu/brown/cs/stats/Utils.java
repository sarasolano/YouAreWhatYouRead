package edu.brown.cs.stats;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

public class Utils {
  public static final int WEEK = 7;
  public static final int YEAR_MONTHS = 12;
  public static final int DECIMAL_PLACE = 3;

  public static double round(double d, int decimalPlace) {
    BigDecimal bd = new BigDecimal(Double.toString(d));
    bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
    return bd.doubleValue();
  }

  public static Date minusHours(Date d, int hours) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(d);
    cal.add(Calendar.HOUR, -hours);
    return cal.getTime();
  }

  public static Date minusDays(Date d, int days) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(d);
    cal.add(Calendar.DATE, -days);
    return cal.getTime();
  }

  public static Date minusWeeks(Date d, int weeks) {
    return minusDays(d, WEEK * weeks);
  }

  public static Date minusMonths(Date d, int months) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(d);
    cal.add(Calendar.MONTH, -months);
    return cal.getTime();
  }

  public static Date minusYears(Date d, int years) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(d);
    cal.add(Calendar.YEAR, -years);
    return cal.getTime();
  }
}
