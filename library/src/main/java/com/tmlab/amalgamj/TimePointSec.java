package com.tmlab.amalgamj;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class TimePointSec {

    public int value;

    public TimePointSec(int value) {
        this.value = value;
    }

    public static TimePointSec fromString(String s) throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return new TimePointSec((int) (dateFormat.parse(s).getTime() / 1000L));
    }

    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return dateFormat.format(new Date(value * 1000L));
    }

    public TimePointSec add(int v) {
        return new TimePointSec(value + v);
    }
}
