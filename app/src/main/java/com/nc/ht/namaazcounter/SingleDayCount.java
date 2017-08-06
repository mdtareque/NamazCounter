package com.nc.ht.namaazcounter;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Md Tareque Khan on 7/8/2017.
 */

/*
case 0: return "Ada Kiya";
case 1: return "Qaza";
case 2: return "Baki hai";
 */
public class SingleDayCount {
    public static final String TABLE_NAME = "daily";
    String fajr = "";
    String zohar = "";
    String asr = "";
    String magrib = "";
    String isha = "";

    String day = "";
    int date = 0;

    SingleDayCount() {
        Date now = new Date();
        SimpleDateFormat today = new SimpleDateFormat("ddMMyyyy");
        SimpleDateFormat week = new SimpleDateFormat("E"); // abbreviated day of week
        date = Integer.parseInt(today.format(now));
        day = week.format(now);
        fajr = zohar = asr = magrib = isha = "Baki hai";
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public String getFajr() {
        return fajr;
    }

    public String getZohar() {
        return zohar;
    }

    public String getAsr() {
        return asr;
    }

    public String getMagrib() {
        return magrib;
    }

    public String getIsha() {
        return isha;
    }

    public void setFajr(String fajr) {
        this.fajr = fajr;
    }

    public void setZohar(String zohar) {
        this.zohar = zohar;
    }

    public void setAsr(String asr) {
        this.asr = asr;
    }

    public void setMagrib(String magrib) {
        this.magrib = magrib;
    }

    public void setIsha(String isha) {
        this.isha = isha;
    }

    @Override
    public String toString() {
        return "F:" + fajr + "  Z:" + zohar + " A:" + asr + " M:" + magrib + " I:" + isha;
    }
}
