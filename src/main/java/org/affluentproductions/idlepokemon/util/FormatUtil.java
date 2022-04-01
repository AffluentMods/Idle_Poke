package org.affluentproductions.idlepokemon.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;

public class FormatUtil {

    public static int round(double toRound) {
        return new BigDecimal(String.valueOf(toRound)).round(new MathContext(1)).intValue();
    }

    public static int getBetween(int a, int b) {
        return a + ((new Random().nextInt(b - a + 1)));
    }

    public static String formatDouble(BigDecimal value) {
        return value.toBigInteger().toString();
    }

    public static String formatDouble(double value) {
        DecimalFormat decimalFormat = new DecimalFormat("#");
        return decimalFormat.format(value);
    }

    public static String formatAbbreviated(double value) {
        return formatAbbreviated(value + "");
    }

    public static String formatAbbreviated(BigDecimal amount) {
        return formatAbbreviated(amount.toBigInteger());
    }

    public static String formatAbbreviated(BigInteger amount) {
        return formatAbbreviated(amount + "");
    }

    public static String formatAbbreviated(String amount) {
        return formatAbbreviated(amount, 5);
    }

    public static String formatAbbreviated(long amount) {
        return formatAbbreviated(String.valueOf(amount), 4);
    }

    public static String formatAbbreviated(String amount, int minLength) {
        final String unformattedAmount = amount;
        if (amount.contains(".") && !amount.toLowerCase().contains("e"))
            amount = amount.substring(0, amount.indexOf("."));
        if (amount.toLowerCase().contains("e")) {
            amount = formatDouble(Double.parseDouble(amount));
        }
        if (amount.length() < minLength) return unformattedAmount;
        String amountStr = amount;
        String amountString = amount;
        if (amountStr.length() >= 15) {
            int afterE = amountStr.length() - 1;
            amountString = amountStr.substring(0, 1) + "." + amountStr.substring(1, 4) + "e" + afterE;
        } else if (amountStr.length() >= 13) {
            int sub = amountStr.length() - 12;
            amountString = amountStr.substring(0, sub) + "." + amountStr.substring(sub).substring(0, 1) + "T";
        } else if (amountStr.length() >= 10) {
            int sub = amountStr.length() - 9;
            amountString = amountStr.substring(0, sub) + "." + amountStr.substring(sub).substring(0, 1) + "B";
        } else if (amountStr.length() >= 7) {
            int sub = amountStr.length() - 6;
            amountString = amountStr.substring(0, sub) + "." + amountStr.substring(sub).substring(0, 1) + "M";
        } else if (amountStr.length() >= 4) {
            int sub = amountStr.length() - 3;
            amountString = amountStr.substring(0, sub) + "." + amountStr.substring(sub).substring(0, 1) + "K";
        }
        return amountString;
    }

    public static String formatCommas(String numberString) {
        if (numberString.contains(".")) numberString = numberString.substring(0, numberString.indexOf("."));
        return formatCommas(Long.parseLong(numberString));
    }

    public static String formatCommas(long number) {
        return new DecimalFormat("#,###").format(number);
    }

    public static String formatCommas(BigInteger number) {
        return new DecimalFormat("#,###").format(number);
    }

    public static String formatDoubleCommas(String number) {
        DecimalFormat decimalFormat = new DecimalFormat("#,##");
        decimalFormat.setGroupingUsed(true);
        decimalFormat.setGroupingSize(3);
        DecimalFormatSymbols dfs = DecimalFormatSymbols.getInstance();
        dfs.setDecimalSeparator('.');
        decimalFormat.setDecimalFormatSymbols(dfs);
        return decimalFormat.format(Double.parseDouble(number));
    }

    public static Date fromTime(long millis) {
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.of("Europe/Berlin")));
        c.setTime(new Date(millis));
        return c.getTime();
    }

    public static Date fromString(String dateString) {
        DateFormat format = new SimpleDateFormat("HH:mm:ss dd.MM.yyyy");
        try {
            return format.parse(dateString);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static Date fromStringNoTime(String dateString) {
        DateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        try {
            return format.parse(dateString);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    static Date getBefore12Hours() {
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.of("Europe/Berlin")));
        c.set(Calendar.HOUR, c.get(Calendar.HOUR) - 12);
        return c.getTime();
    }

    static Date getBefore24Hours() {
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.of("Europe/Berlin")));
        c.set(Calendar.DAY_OF_YEAR, c.get(Calendar.DAY_OF_YEAR) - 1);
        return c.getTime();
    }

    public static Date get15Minutes() {
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.of("Europe/Berlin")));
        c.add(Calendar.MINUTE, 15);
        return c.getTime();
    }

    public static Date get24Hours() {
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.of("Europe/Berlin")));
        c.add(Calendar.DAY_OF_YEAR, 1);
        return c.getTime();
    }

    public static Date get48Hours() {
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.of("Europe/Berlin")));
        c.add(Calendar.DAY_OF_YEAR, 2);
        return c.getTime();
    }

    public static Date getNow() {
        return Calendar.getInstance(TimeZone.getTimeZone(ZoneId.of("Europe/Berlin"))).getTime();
    }

    public static String fromDate(Date date) {
        DateFormat format = new SimpleDateFormat("HH:mm:ss dd.MM.yyyy");
        return format.format(date);
    }

    public static String fromDateNoTime(Date date) {
        DateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        return format.format(date);
    }

    public static String getRomicNumber(int number) {
        switch (number) {
            case 1:
                return "I";
            case 2:
                return "II";
            case 3:
                return "III";
            case 4:
                return "IV";
            case 5:
                return "V";
            case 6:
                return "VI";
            case 7:
                return "VII";
            case 8:
                return "VIII";
            case 9:
                return "IX";
            case 10:
                return "X";
        }
        return "";
    }
}