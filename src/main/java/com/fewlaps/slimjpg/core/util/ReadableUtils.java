package com.fewlaps.slimjpg.core.util;

import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

public class ReadableUtils {

    public static String formatFileSize(long size) {
        if (size <= 0) return "0";
        final String[] units = new String[]{"B", "kB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.0#").format(size / Math.pow(1024, digitGroups)) + "" + units[digitGroups];
    }

    public static String formatPercentage(double rate) {
        return new DecimalFormat("#0.00%").format(rate);
    }

    public static String formatPercentage(int rate) {
        return rate + "%";
    }

    public static String formatElapsedTime(final long l) {
        final long hr = TimeUnit.MILLISECONDS.toHours(l);
        final long min = TimeUnit.MILLISECONDS.toMinutes(l - TimeUnit.HOURS.toMillis(hr));
        final long sec = TimeUnit.MILLISECONDS.toSeconds(l - TimeUnit.HOURS.toMillis(hr) - TimeUnit.MINUTES.toMillis(min));
        final long ms = TimeUnit.MILLISECONDS.toMillis(l - TimeUnit.HOURS.toMillis(hr) - TimeUnit.MINUTES.toMillis(min) - TimeUnit.SECONDS.toMillis(sec));
        return String.format("%02d:%02d:%02d.%03d", hr, min, sec, ms);
    }
}
