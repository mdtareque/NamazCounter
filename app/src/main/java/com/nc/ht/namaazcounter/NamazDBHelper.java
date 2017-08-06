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
    protected static final String LOG_TAG = "NamazDBHelper";
    private static final String D = SingleDayCount.TABLE_NAME;

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + SingleDayCount.TABLE_NAME ;


    public NamazDBHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA_VERSION);
        // hack to recreate db always
        // context.deleteDatabase(DATABASE_NAME);
    }

    public NamazDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE daily (_id INTEGER PRIMARY KEY " +
                ",date INTEGER" +
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
        return (getReadableDatabase().rawQuery("SELECT date, day, fajr, zohar, asr, magrib, isha " +
                "FROM daily", null));
    }

    public Cursor getStats() {
        return (getReadableDatabase().rawQuery("SELECT _id, month, fa,fq,fb, za,zq,zb, aa,aq,ab, ma,mq,mb, ia,iq,ib " +
                "FROM stats", null));
    }

    public void insertSingleDay(SingleDayCount obj) {
        Log.d("DBHelper INSERT", "start");
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

        Cursor out = getReadableDatabase().rawQuery("select * from daily", null);
        Log.d("DB INSERT", "check just inserted count " + out.getCount());
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

    private String getNamazStatusFromInt(int i) {
        switch (i) {
            case 0: return "Ada Kiya";
            case 1: return "Qaza";
            case 2: return "Baki hai";
            default: return "Baki hai";
        }
    }

    public SingleDayCount populateTodaysAndGetObject() {


        Date now = new Date();
        SimpleDateFormat date = new SimpleDateFormat("ddMMyyyy");
        SimpleDateFormat week = new SimpleDateFormat("E"); // abbreviated day of week
        int queryDate = Integer.parseInt(date.format(now));
        Log.d("DBHelper POPULATE_TODAY", "populating for " + queryDate);
        // https://stackoverflow.com/questions/12473194/get-a-single-row-from-table
        // https://stackoverflow.com/questions/14745027/how-to-print-query-executed-by-query-method-of-android
        // https://stackoverflow.com/questions/20415309/android-sqlite-how-to-check-if-a-record-exists
        // https://stackoverflow.com/questions/10600670/sqlitedatabase-query-method
        Cursor output = null;
        SingleDayCount namazStatus = new SingleDayCount();
        SQLiteDatabase db = getReadableDatabase();
//        Cursor out2 = db.rawQuery("select * from daily where date = 6082017", null);
//        Log.d("DB", "out2.count " + out2.getCount());

//        String sqlQry = SQLiteQueryBuilder.buildQueryString(false,"daily", null, "date="+ queryDate, null, null, null, null);
//        Log.i("DB", sqlQry);
//        String selectQuery = "SELECT  * FROM daily  WHERE _id ==  1";
//        Log.d("DBLOG", selectQuery);
//        Cursor c = db.rawQuery(selectQuery, null);
//        Log.d("DBLOG", "" + c.getCount());
        try {
            output = db.rawQuery("SELECT fajr, zohar, asr, magrib, isha, date, day " +
                    "FROM daily WHERE date=?", new String[]{String.valueOf(queryDate)});

            Log.d("DBHelper POPULATE_TODAY", "returned rows count " + output.getCount() + " for " + queryDate );

            if (output == null || output.getCount() <= 0) {
                insertSingleDay(namazStatus);
            } else {
                output.moveToFirst();
                namazStatus.setFajr(getNamazStatusFromInt(output.getInt(0)));
                namazStatus.setZohar(getNamazStatusFromInt(output.getInt(1)));
                namazStatus.setAsr(getNamazStatusFromInt(output.getInt(2)));
                namazStatus.setMagrib(getNamazStatusFromInt(output.getInt(3)));
                namazStatus.setIsha(getNamazStatusFromInt(output.getInt(4)));
                namazStatus.setDate(output.getInt(5));
                namazStatus.setDay(output.getString(6));
            }
            Log.d("DBHelper POPULATE_TODAY", namazStatus.toString());
        } finally {
            output.close();
        }
        return namazStatus;
    }

    public void updateToday(SingleDayCount namazStatus, String col, String updatedValue) {
        ContentValues cv = new ContentValues();
        cv.put(col, getNamazStatusInInt(updatedValue));
        getWritableDatabase().update("daily", cv,  "date= ?", new String[] {String.valueOf(namazStatus.getDate())});
        Log.d("DBHelper UPDATE_TODAY", col +" updated to "+ updatedValue);
    }
}
