package com.nc.ht.namaazcounter;

import java.util.Date;

/**
 * Created by Md Tareque Khan on 7/8/2017.
 * <p>
 * case 0: return "Ada Kiya";
 * case 1: return "Qaza";
 * case 2: return "Baki hai";
 */
class SingleDayCount {

    static final String TABLE_NAME = "daily";
    private String fajr, zohar, asr, magrib, isha;
    private String day = "";
    private String date = "";
    private Date now = new Date();

    SingleDayCount() {
        date = Util.sdfToday.format(now);
        day = Util.sdfWeek.format(now);
        fajr = zohar = asr = magrib = isha = "Baki hai";
    }

    public String getFajr() {
        return fajr;
    }

    public void setFajr(String fajr) {
        this.fajr = fajr;
    }

    public String getZohar() {
        return zohar;
    }

    public void setZohar(String zohar) {
        this.zohar = zohar;
    }

    public String getAsr() {
        return asr;
    }

    public void setAsr(String asr) {
        this.asr = asr;
    }

    public String getMagrib() {
        return magrib;
    }

    public void setMagrib(String magrib) {
        this.magrib = magrib;
    }

    public String getIsha() {
        return isha;
    }

    public void setIsha(String isha) {
        this.isha = isha;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return day + "-" + date + "   F:" + fajr + "  Z:" + zohar + " A:" + asr + " M:" + magrib + " I:" + isha;
    }
}
