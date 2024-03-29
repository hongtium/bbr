package com.bbr.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * General purpose date utilities.
 *
 * @author Mark Saarinen
 * @author Lance Lavandowska
 */
public abstract class CommonDateUtil extends Object {
    public static final long millisInDay = 86400000;

    // some static date formats
    private static SimpleDateFormat[] mDateFormats = loadDateFormats();

    public static final SimpleDateFormat mFormat8chars =
            new SimpleDateFormat("yyyyMMdd");

    public static final SimpleDateFormat mFormat6chars =
            new SimpleDateFormat("yyyyMM");

    public static final SimpleDateFormat mFormat16chars =
            new SimpleDateFormat("yyyy-MM-dd HH:mm");


    public static final SimpleDateFormat mFormatIso8601Day =
            new SimpleDateFormat("yyyy-MM-dd");

    public static final SimpleDateFormat mFormatIso8601 =
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

    // http://www.w3.org/Protocols/rfc822/Overview.html#z28
    // Using Locale.US to fix ROL-725 and ROL-628
    private static final SimpleDateFormat mFormatRfc822 =
            new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.US);

    public static boolean isEmpty(String str) {
        if (str == null) return true;
        return "".equals(str.trim());
    }

    private static SimpleDateFormat[] loadDateFormats() {
        SimpleDateFormat[] temp = {
                //new SimpleDateFormat("MM/dd/yyyy hh:mm:ss.SSS a"),
                new SimpleDateFormat("EEE MMM d HH:mm:ss z yyyy"), // standard Date.toString() results
                new SimpleDateFormat("M/d/yy hh:mm:ss"),
                new SimpleDateFormat("M/d/yyyy hh:mm:ss"),
                new SimpleDateFormat("M/d/yy hh:mm a"),
                new SimpleDateFormat("M/d/yyyy hh:mm a"),
                new SimpleDateFormat("M/d/yy HH:mm"),
                new SimpleDateFormat("M/d/yyyy HH:mm"),
                new SimpleDateFormat("dd.MM.yyyy HH:mm:ss"),
                new SimpleDateFormat("yy-MM-dd HH:mm:ss.SSS"),
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS"), // standard Timestamp.toString() results
                new SimpleDateFormat("M-d-yy HH:mm"),
                new SimpleDateFormat("M-d-yyyy HH:mm"),
                new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS"),
                new SimpleDateFormat("M/d/yy"),
                new SimpleDateFormat("M/d/yyyy"),
                new SimpleDateFormat("M-d-yy"),
                new SimpleDateFormat("M-d-yyyy"),
                new SimpleDateFormat("MMMM d, yyyyy"),
                new SimpleDateFormat("MMM d, yyyyy")
        };

        return temp;
    }
    //-----------------------------------------------------------------------

    /**
     * Gets the array of SimpleDateFormats that CommonDateUtil knows about.
     */
    private static SimpleDateFormat[] getFormats() {
        return mDateFormats;
    }

    //-----------------------------------------------------------------------

    /**
     * Returns a Date set to the last possible millisecond of the day, just
     * before midnight. If a null day is passed in, a new Date is created.
     * midnight (00m 00h 00s)
     */
    public static Date getEndOfDay(Date day) {
        return getEndOfDay(day, Calendar.getInstance());
    }

    public static Date getEndOfDay(Date day, Calendar cal) {
        if (day == null) day = new Date();
        cal.setTime(day);
        cal.set(Calendar.HOUR_OF_DAY, cal.getMaximum(Calendar.HOUR_OF_DAY));
        cal.set(Calendar.MINUTE, cal.getMaximum(Calendar.MINUTE));
        cal.set(Calendar.SECOND, cal.getMaximum(Calendar.SECOND));
        cal.set(Calendar.MILLISECOND, cal.getMaximum(Calendar.MILLISECOND));
        return cal.getTime();
    }

    //-----------------------------------------------------------------------

    /**
     * Returns a Date set to the last possible millisecond of the day, just
     * before midnight. If a null day is passed in, a new Date is created.
     * midnight (00m 00h 00s)
     */
    public static Date getEndOfMonth(Date day) {
        return getEndOfMonth(day, Calendar.getInstance());
    }

    public static Date getEndOfMonth(Date day, Calendar cal) {
        if (day == null) day = new Date();
        cal.setTime(day);

        // set time to end of day
        cal.set(Calendar.HOUR_OF_DAY, cal.getMaximum(Calendar.HOUR_OF_DAY));
        cal.set(Calendar.MINUTE, cal.getMaximum(Calendar.MINUTE));
        cal.set(Calendar.SECOND, cal.getMaximum(Calendar.SECOND));
        cal.set(Calendar.MILLISECOND, cal.getMaximum(Calendar.MILLISECOND));

        // set time to first day of month
        cal.set(Calendar.DAY_OF_MONTH, 1);

        // add one month
        cal.add(Calendar.MONTH, 1);

        // back up one day
        cal.add(Calendar.DAY_OF_MONTH, -1);

        return cal.getTime();
    }

    public static Date getStartOfMonth(Date day) {
        return getStartOfMonth(day, Calendar.getInstance());
    }

    public static Date getStartOfMonth(Date day, Calendar cal) {
        if (day == null) day = new Date();
        cal.setTime(day);

        // set time to end of day
        cal.set(Calendar.HOUR_OF_DAY, cal.getMaximum(Calendar.HOUR_OF_DAY));
        cal.set(Calendar.MINUTE, cal.getMaximum(Calendar.MINUTE));
        cal.set(Calendar.SECOND, cal.getMaximum(Calendar.SECOND));
        cal.set(Calendar.MILLISECOND, cal.getMaximum(Calendar.MILLISECOND));

        // set time to first day of month
        cal.set(Calendar.DAY_OF_MONTH, 1);

        // back up one day
        cal.add(Calendar.DAY_OF_MONTH, -1);

        return cal.getTime();
    }
    //-----------------------------------------------------------------------

    /**
     * Returns a Date set to the first possible millisecond of the day, just
     * after midnight. If a null day is passed in, a new Date is created.
     * midnight (00m 00h 00s)
     */
    public static Date getStartOfDay(Date day) {
        return getStartOfDay(day, Calendar.getInstance());
    }

    /**
     * Returns a Date set to the first possible millisecond of the day, just
     * after midnight. If a null day is passed in, a new Date is created.
     * midnight (00m 00h 00s)
     */
    public static Date getStartOfDay(Date day, Calendar cal) {
        if (day == null) day = new Date();
        cal.setTime(day);
        cal.set(Calendar.HOUR_OF_DAY, cal.getMinimum(Calendar.HOUR_OF_DAY));
        cal.set(Calendar.MINUTE, cal.getMinimum(Calendar.MINUTE));
        cal.set(Calendar.SECOND, cal.getMinimum(Calendar.SECOND));
        cal.set(Calendar.MILLISECOND, cal.getMinimum(Calendar.MILLISECOND));
        return cal.getTime();
    }

    /**
     * Returns a Date set just to Noon, to the closest possible millisecond
     * of the day. If a null day is passed in, a new Date is created.
     * nnoon (00m 12h 00s)
     */
    public static Date getNoonOfDay(Date day, Calendar cal) {
        if (day == null) day = new Date();
        cal.setTime(day);
        cal.set(Calendar.HOUR_OF_DAY, 12);
        cal.set(Calendar.MINUTE, cal.getMinimum(Calendar.MINUTE));
        cal.set(Calendar.SECOND, cal.getMinimum(Calendar.SECOND));
        cal.set(Calendar.MILLISECOND, cal.getMinimum(Calendar.MILLISECOND));
        return cal.getTime();
    }

    //-----------------------------------------------------------------------
    public static Date parseFromFormats(String aValue) {
        if (isEmpty(aValue)) return null;

        // get CommonDateUtil's formats
        SimpleDateFormat formats[] = CommonDateUtil.getFormats();
        if (formats == null) return null;

        // iterate over the array and parse
        Date myDate = null;
        for (int i = 0; i < formats.length; i++) {
            try {
                myDate = CommonDateUtil.parse(aValue, formats[i]);
                //if (myDate instanceof Date)
                return myDate;
            } catch (Exception e) {
                // do nothing because we want to try the next
                // format if current one fails
            }
        }
        // haven't returned so couldn't parse
        return null;
    }

    //-----------------------------------------------------------------------
    public static java.sql.Timestamp parseTimestampFromFormats(String aValue) {
        if (isEmpty(aValue)) return null;

        // call the regular Date formatter
        Date myDate = CommonDateUtil.parseFromFormats(aValue);
        if (myDate != null) return new java.sql.Timestamp(myDate.getTime());
        return null;
    }
    //-----------------------------------------------------------------------

    /**
     * Returns a java.sql.Timestamp equal to the current time
     */
    public static java.sql.Timestamp now() {
        return new java.sql.Timestamp(new java.util.Date().getTime());
    }

    //-----------------------------------------------------------------------

    /**
     * Returns a string the represents the passed-in date parsed
     * according to the passed-in format.  Returns an empty string
     * if the date or the format is null.
     */
    public static String format(Date aDate, SimpleDateFormat aFormat) {
        if (aDate == null || aFormat == null) {
            return "";
        }
        synchronized (aFormat) {
            return aFormat.format(aDate);
        }
    }

    //-----------------------------------------------------------------------

    /**
     * Tries to take the passed-in String and format it as a date string in the
     * the passed-in format.
     */
    public static String formatDateString(String aString, SimpleDateFormat aFormat) {
        if (isEmpty(aString) || aFormat == null) return "";
        try {
            java.sql.Timestamp aDate = parseTimestampFromFormats(aString);
            if (aDate != null) {
                return CommonDateUtil.format(aDate, aFormat);
            }
        } catch (Exception e) {
            // Could not parse aString.
        }
        return "";
    }

    //-----------------------------------------------------------------------

    /**
     * Returns a Date using the passed-in string and format.  Returns null if the string
     * is null or empty or if the format is null.  The string must match the format.
     */
    public static Date parse(String aValue, SimpleDateFormat aFormat) throws ParseException {
        if (isEmpty(aValue) || aFormat == null) {
            return null;
        }

        return aFormat.parse(aValue);
    }

    //-----------------------------------------------------------------------

    /**
     * Returns true if endDate is after startDate or if startDate equals endDate
     * or if they are the same date.  Returns false if either value is null.
     */
    public static boolean isValidDateRange(Date startDate, Date endDate) {
        return isValidDateRange(startDate, endDate, true);
    }

    //-----------------------------------------------------------------------

    /**
     * Returns true if endDate is after startDate or if startDate equals endDate.
     * Returns false if either value is null.  If equalOK, returns true if the
     * dates are equal.
     */
    public static boolean isValidDateRange(Date startDate, Date endDate, boolean equalOK) {
        // false if either value is null
        if (startDate == null || endDate == null) {
            return false;
        }

        if (equalOK) {
            // true if they are equal
            if (startDate.equals(endDate)) {
                return true;
            }
        }

        // true if endDate after startDate
        if (endDate.after(startDate)) {
            return true;
        }

        return false;
    }

    //-----------------------------------------------------------------------
    // returns full timestamp format
    public static java.text.SimpleDateFormat defaultTimestampFormat() {
        return new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    }

    //-----------------------------------------------------------------------
    // convenience method returns minimal date format
    public static java.text.SimpleDateFormat get8charDateFormat() {
        return CommonDateUtil.mFormat8chars;
    }

    // convenience method returns minimal date format
    public static java.text.SimpleDateFormat get6charDateFormat() {
        return CommonDateUtil.mFormat6chars;
    }

    //-----------------------------------------------------------------------
    // convenience method returns minimal date format
    public static java.text.SimpleDateFormat defaultDateFormat() {
        return CommonDateUtil.friendlyDateFormat(true);
    }

    //-----------------------------------------------------------------------
    // convenience method
    public static String defaultTimestamp(Date date) {
        return CommonDateUtil.format(date, CommonDateUtil.defaultTimestampFormat());
    }

    //-----------------------------------------------------------------------
    // convenience method
    public static String defaultDate(Date date) {
        return CommonDateUtil.format(date, CommonDateUtil.defaultDateFormat());
    }

    //-----------------------------------------------------------------------
    // convenience method returns long friendly timestamp format
    public static java.text.SimpleDateFormat friendlyTimestampFormat() {
        return new java.text.SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    }

    //-----------------------------------------------------------------------
    // convenience method returns long friendly formatted timestamp
    public static String friendlyTimestamp(Date date) {
        return CommonDateUtil.format(date, CommonDateUtil.friendlyTimestampFormat());
    }

    //-----------------------------------------------------------------------
    // convenience method returns long friendly formatted timestamp
    public static String format8chars(Date date) {
        return CommonDateUtil.format(date, mFormat8chars);
    }

    //-----------------------------------------------------------------------
    /** convenience method returns long friendly formatted timestamp
     * 格式化成 yyyy-MM-DD 格式
     */
    public static String formatIso8601Day(Date date) {
        return CommonDateUtil.format(date, mFormatIso8601Day);
    }

    /** 格式化成 yyyy-MM-DD HH:mm 格式 */
    public static String formatYMDhm(Date date) {
        return CommonDateUtil.format(date, mFormat16chars);
    }

    //-----------------------------------------------------------------------
    public static String formatRfc822(Date date) {
        return CommonDateUtil.format(date, mFormatRfc822);
    }

    //-----------------------------------------------------------------------
    // This is a hack, but it seems to work
    public static String formatIso8601(Date date) {
        if (date == null) return "";

        // Add a colon 2 chars before the end of the string
        // to make it a valid ISO-8601 date.

        String str = CommonDateUtil.format(date, mFormatIso8601);
        StringBuffer sb = new StringBuffer();
        sb.append(str.substring(0, str.length() - 2));
        sb.append(":");
        sb.append(str.substring(str.length() - 2));
        return sb.toString();
    }

    //-----------------------------------------------------------------------
    // convenience method returns minimal date format
    public static java.text.SimpleDateFormat minimalDateFormat() {
        return CommonDateUtil.friendlyDateFormat(true);
    }

    //-----------------------------------------------------------------------
    // convenience method using minimal date format
    public static String minimalDate(Date date) {
        return CommonDateUtil.format(date, CommonDateUtil.minimalDateFormat());
    }

    //-----------------------------------------------------------------------
    // convenience method that returns friendly data format
    // using full month, day, year digits.
    public static java.text.SimpleDateFormat fullDateFormat() {
        return CommonDateUtil.friendlyDateFormat(false);
    }

    //-----------------------------------------------------------------------
    public static String fullDate(Date date) {
        return CommonDateUtil.format(date, CommonDateUtil.fullDateFormat());
    }

    //-----------------------------------------------------------------------

    /**
     * Returns a "friendly" date format.
     *
     * @param minimalFormat Should the date format allow single digits.
     */
    public static java.text.SimpleDateFormat friendlyDateFormat(boolean minimalFormat) {
        if (minimalFormat) {
            return new java.text.SimpleDateFormat("d.M.yy");
        }

        return new java.text.SimpleDateFormat("dd.MM.yyyy");
    }

    //-----------------------------------------------------------------------

    /**
     * Format the date using the "friendly" date format.
     */
    public static String friendlyDate(Date date, boolean minimalFormat) {
        return CommonDateUtil.format(date, CommonDateUtil.friendlyDateFormat(minimalFormat));
    }

    //-----------------------------------------------------------------------
    // convenience method
    public static String friendlyDate(Date date) {
        return CommonDateUtil.format(date, CommonDateUtil.friendlyDateFormat(true));
    }

    public static void main(String[] v) throws Exception {
        if ("parse".equals(v[0])) {
            System.out.println(parse(v[1], CommonDateUtil.mFormatIso8601Day));
        }
    }
}

