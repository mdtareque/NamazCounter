package com.nc.ht.namaazcounter;

/**
 * Created by Md Tareque Khan on 8/7/2017.
 */

public enum NState {
    ADA_KIYA(0), QAZA(1), BAKI_HAI(2);

    private final int code;

    NState(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
