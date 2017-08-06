package com.nc.ht.namaazcounter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Md Tareque Khan on 7/8/2017.
 */

// http://sqlite.1065341.n5.nabble.com/command-line-does-not-accept-arrow-keys-td56316.html

/*
$ adb devices
$ adb shell
$ cd /data/data/com.nc.ht.namaazcounter/databases
$ sqlite3 namazcounter.db
> .tables
> .schema

 */
public class NamazDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "namazcounter.db";
    private static final int SCHEMA_VERSION = 2;
    private static final String LOG_TAG = "NamazDBHelper";
    private static final String D = SingleDayCount.TABLE_NAME;
    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + SingleDayCount.TABLE_NAME;
    private SQLiteDatabase wdb, rdb;


    public NamazDBHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA_VERSION);
        wdb = getWritableDatabase();
        rdb = getReadableDatabase();
        // hack to recreate db always
        // context.deleteDatabase(DATABASE_NAME);
    }

    public NamazDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + SingleDayCount.TABLE_NAME + " (_id INTEGER PRIMARY KEY " +
                ",date TEXT" +
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
//        db.execSQL(SQL_DELETE_ENTRIES);
//        onCreate(db);
    }

    public Cursor getAllDaily() {
        return (rdb.rawQuery("SELECT date, day, fajr, zohar, asr, magrib, isha " +
                "FROM " + SingleDayCount.TABLE_NAME, null));
    }

    public Cursor getStats() {
        return (wdb.rawQuery("SELECT _id, month, fa,fq,fb, za,zq,zb, aa,aq,ab, ma,mq,mb, ia,iq,ib " +
                "FROM stats", null));
    }

    public void insertSingleDay(SingleDayCount obj) {

        Log.d("DBHelper INSERT", "start");
        ContentValues cv = new ContentValues();

        cv.put("date", Util.getTodaysDate());
        cv.put("day", Util.getTodaysDay());
        cv.put("fajr", Util.getNamazStatusInInt(obj.getFajr()));
        cv.put("zohar", Util.getNamazStatusInInt(obj.getZohar()));
        cv.put("asr", Util.getNamazStatusInInt(obj.getAsr()));
        cv.put("magrib", Util.getNamazStatusInInt(obj.getMagrib()));
        cv.put("isha", Util.getNamazStatusInInt(obj.getIsha()));
        Log.d("DB", cv.toString());
        getWritableDatabase().insertWithOnConflict(SingleDayCount.TABLE_NAME, null, cv, SQLiteDatabase.CONFLICT_REPLACE);

    }


    public SingleDayCount populateTodaysAndGetObject() {

        String queryDate = Util.getTodaysDate();
        Log.d("DBHelper POPULATE_TODAY", "populating for " + queryDate);
        Cursor output = null;

        SingleDayCount namazStatus = new SingleDayCount();

        try {
            output = rdb.rawQuery("SELECT fajr, zohar, asr, magrib, isha, date, day " +
                    "FROM " + SingleDayCount.TABLE_NAME + " WHERE date=?", new String[]{queryDate});

            Log.d("DBHelper POPULATE_TODAY", "returned rows count " + output.getCount() + " for " + queryDate);

            if (output.getCount() <= 0) {
                insertSingleDay(namazStatus);
            } else {
                output.moveToFirst();
                namazStatus.setFajr(Util.getNamazStatusFromInt(output.getInt(0)));
                namazStatus.setZohar(Util.getNamazStatusFromInt(output.getInt(1)));
                namazStatus.setAsr(Util.getNamazStatusFromInt(output.getInt(2)));
                namazStatus.setMagrib(Util.getNamazStatusFromInt(output.getInt(3)));
                namazStatus.setIsha(Util.getNamazStatusFromInt(output.getInt(4)));
                namazStatus.setDate(output.getString(5));
                namazStatus.setDay(output.getString(6));
            }
            Log.d("DBHelper POPULATE_TODAY", namazStatus.toString());
        } finally {
            if (output != null)
                output.close();
        }
        return namazStatus;
    }

    public void updateToday(SingleDayCount namazStatus, String col, String updatedValue) {
        ContentValues cv = new ContentValues();
        cv.put(col, Util.getNamazStatusInInt(updatedValue));
        getWritableDatabase().update(SingleDayCount.TABLE_NAME, cv, "date= ?", new String[]{namazStatus.getDate()});
        Log.d("DBHelper UPDATE_TODAY", col + " updated to " + updatedValue);
    }
}
