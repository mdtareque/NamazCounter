package com.nc.ht.namaazcounter;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Md Tareque Khan on 8/7/2017.
 */

public class Util {

    static final String ADA_KIYA = "Ada Kiya";
    static final String BAKI_HAI = "Baki hai";
    static final String QAZA = "Qaza";
    static SimpleDateFormat sdfToday = new SimpleDateFormat("ddMMyyyy");
    static SimpleDateFormat sdfWeek = new SimpleDateFormat("E"); // abbreviated day of week
    static SimpleDateFormat sdfHeader = new SimpleDateFormat("E - dd MMM");
    private static Date now = new Date();

    public static String getTodaysDate() {
        return sdfToday.format(now);
    }

    public static String getTodaysDay() {
        return sdfWeek.format(now);
    }

    public static int getNamazStatusInInt(String s) {
        switch (s) {
            case ADA_KIYA:
                return NState.ADA_KIYA.getCode();
            case QAZA:
                return NState.QAZA.getCode();
            case BAKI_HAI:
                return NState.BAKI_HAI.getCode();
            default:
                return NState.BAKI_HAI.getCode();
        }
    }

    public static String getNamazStatusFromInt(int i) {
        switch (i) {
            case 0:
                return ADA_KIYA;
            case 1:
                return QAZA;
            case 2:
                return BAKI_HAI;
            default:
                return BAKI_HAI;
        }
    }
}
