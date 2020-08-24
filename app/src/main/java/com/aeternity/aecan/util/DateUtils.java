package com.aeternity.aecan.util;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZoneId;
import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.format.DateTimeParseException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by a on 08/11/2016.
 */

public class DateUtils {


    public static final String STANDARD_DATE_FORMAT = "dd/MM/yyyy";
    public static final String DATE_WITH_DAY = "EEEE dd/MM/yyyy";
    public static final String FULL_DATE = "EEEE dd 'de' MMMM 'de' yyyy";
    public static final String STANDARD_TIME_FORMAT = "HH:mm";
    public static final String API_DATE_FORMAT = "yyyy-MM-dd";
    public static final String NAME_DAY_MONTH = "EEEE dd/MM. HH:mm";
    public static final String SMALL_DATE = "dd/MM";
    public static final String DATE_WITH_TIME = "dd/MM/yyyy - HH:mm";
    public static final String FULL_SERVER_DATE = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";


    public static String forApi(Date date) {
        return dateToString(date, API_DATE_FORMAT);
    }

    public static String forApi(String sDate, String oldFormat) throws ParseException {
        return forApi(stringToDate(sDate, oldFormat));
    }

    public static String dateToString(Date date, String format) {
        SimpleDateFormat formater = new SimpleDateFormat(format);
        return formater.format(date);
    }

    public static String dateToStringCapitalized(Date date, String format) {
        StringBuilder sb = new StringBuilder(dateToString(date, format));
        sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
        return sb.toString();
    }

    public static Date stringToDate(String sDate, String format) {
        if (sDate == null || sDate.isEmpty()) return null;
        DateFormat formater = new SimpleDateFormat(format);
        formater.setTimeZone(TimeZone.getDefault());
        try {
            return formater.parse(sDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String stringToString(String s, String inputFormat, String outputFormat) {
        Date d = stringToDate(s, inputFormat);
        return dateToString(d, outputFormat);
    }

    public static String getUTCOffset() {
        return String.valueOf(TimeZone.getDefault().getRawOffset() / 1000 / 60 / 60);
    }

    public static int[] StringToYYMMDD(String input) {
        int[] date = new int[5];
        Arrays.fill(date, 4);
        try {
            String[] splitted = input.split("-");
            date[0] = Integer.valueOf(splitted[0]);
            date[1] = Integer.valueOf(splitted[1]);
            date[2] = Integer.valueOf(splitted[2]);
        } catch (NumberFormatException | NullPointerException ex) {
            ex.printStackTrace();
        }

        return date;
    }

    public static ZonedDateTime parseTextZoned(String str) throws NullPointerException {
        try {
            return ZonedDateTime.parse(str);
        } catch (DateTimeParseException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static String localDateToString(ZonedDateTime date) {
        try {
            ZonedDateTime dateTime = date.withZoneSameInstant(ZoneId.systemDefault());
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern(DATE_WITH_TIME, Locale.ENGLISH);
            return outputFormatter.format(dateTime);
        } catch (NullPointerException ex) {
            return "";
        }

    }

    public static String parseToString(String str) {
        return localDateToString(parseTextZoned(str));
    }


}
