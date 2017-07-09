package com.nc.ht.namaazcounter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Md Tareque Khan on 7/8/2017.
 */

public class NamazDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "namazcounter.db";
    private static final int SCHEMA_VERSION = 2;
    protected static final String LOG_TAG = "NamazDBHelper";


    public NamazDBHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA_VERSION);
        context.deleteDatabase(DATABASE_NAME);
    }

    public NamazDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE daily (date INTEGER PRIMARY KEY " +
                ",day TEXT" +
                ",fajr INTEGER" + // 0: Ada kiya, 1:qaza, 2:baki
                ",zohar INTEGER" +
                ",asr INTEGER" +
                ",magrib INTEGER" +
                ",isha INTEGER);");


        db.execSQL("CREATE TABLE stats (_id INTEGER PRIMARY KEY AUTOINCREMENT" +
                ",monthyear INTEGER" +
                ",fa INTEGER, fq INTEGER, fb INTEGER" +
                ",za INTEGER, zq INTEGER, zb INTEGER" +
                ",aa INTEGER, aq INTEGER, ab INTEGER" +
                ",ma INTEGER, mq INTEGER, mb INTEGER" +
                ",ia INTEGER, iq INTEGER, ib INTEGER);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public Cursor getAllDaily() {
        return (getReadableDatabase().rawQuery("SELECT date, day, fajr, zohar, asr, magrib, isha " +
                "FROM daily", null));
    }


    public Cursor getStats() {
        return (getReadableDatabase().rawQuery("SELECT _id, month, fa,fq,fb, za,zq,zb, aa,aq,ab, ma,mq,mb, ia,iq,ib " +
                "FROM stats", null));
    }

    public void insertSingleDay(SingleDayCount obj) {
        ContentValues cv = new ContentValues();
        Date now = new Date();

        SimpleDateFormat date = new SimpleDateFormat("ddMMyyyy");
        SimpleDateFormat week = new SimpleDateFormat("E"); // abbreviated day of week
        cv.put("date", Integer.parseInt(date.format(now)));
        cv.put("day", week.format(now));
        cv.put("fajr", getNamazStatusInInt(obj.getFajr()));
        cv.put("zohar", getNamazStatusInInt(obj.getZohar()));
        cv.put("asr", getNamazStatusInInt(obj.getAsr()));
        cv.put("magrib", getNamazStatusInInt(obj.getMagrib()));
        cv.put("isha", getNamazStatusInInt(obj.getIsha()));
        Log.d("DB", cv.toString());
        getWritableDatabase().insertWithOnConflict("daily", null, cv, SQLiteDatabase.CONFLICT_REPLACE);
    }

    private int getNamazStatusInInt(String s) {
        switch (s) {
            case "Ada Kiya":
                return 0;
            case "Qaza":
                return 1;
            case "Baki hai":
                return 2;
            default:
                return 0;
        }
    }

    public int getDate(Cursor c) {
        return c.getInt(1);
    }

    public String getDay(Cursor c) {
        return c.getString(2);
    }

    public int getFajr(Cursor c) {
        return c.getInt(3);
    }

    public int getZohar(Cursor c) {
        return c.getInt(4);
    }

    public int getAsr(Cursor c) {
        return c.getInt(5);
    }

    public int getMagrib(Cursor c) {
        return c.getInt(6);
    }

    public int getIsha(Cursor c) {
        return c.getInt(7);
    }

}
