package com.nc.ht.namaazcounter;

/**
 * Created by Md Tareque Khan on 7/8/2017.
 */

public class SingleDayCount {
    String fajr = "";
    String zohar = "";
    String asr = "";
    String magrib = "";
    String isha = "";

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
