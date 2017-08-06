package com.nc.ht.namaazcounter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;

public class NamaazActivity extends AppCompatActivity {

    private static final String LOG_TAG = "NamaazActivity";
    private SingleDayCount namazStatus;
    private NamazDBHelper dbHelper;
    private int[] spinners = new int[]{
            R.id.spinner_fajr, R.id.spinner_zohar, R.id.spinner_asr, R.id.spinner_magrib, R.id.spinner_isha
    };
    private Spinner tempSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_namaaz);

        dbHelper = new NamazDBHelper(this);
        namazStatus = dbHelper.populateTodaysAndGetObject();

        TextView header = (TextView) findViewById(R.id.date_header);
        try {
            header.setText(Util.sdfHeader.format(Util.sdfToday.parse(namazStatus.getDate())));
        } catch (ParseException e) {
            header.setText("<data not set>");
            e.printStackTrace();
        }

        int[] initialStatus = getInitialPositionArray(namazStatus);

        for (int index = 0; index < 5; index++) {
            tempSpinner = (Spinner) findViewById(spinners[index]);
            tempSpinner.setSelection(initialStatus[index], false);
        }
        Log.d("MAIN", "spinners initial status set");

        MyOnItemSelectedListener spinnerListener = new MyOnItemSelectedListener();
        for (int i : spinners) {
            tempSpinner = (Spinner) findViewById(i);
            tempSpinner.setOnItemSelectedListener(spinnerListener);
        }
    }

    private int getPositionFromStatus(String s) {
        switch (s) {
            case Util.BAKI_HAI:
                return 0;
            case Util.ADA_KIYA:
                return 1;
            case Util.QAZA:
                return 2;
            default:
                return 0;
        }
    }

    private int[] getInitialPositionArray(SingleDayCount namazStatus) {
        int[] ret = new int[5];
        ret[0] = getPositionFromStatus(namazStatus.getFajr());
        ret[1] = getPositionFromStatus(namazStatus.getZohar());
        ret[2] = getPositionFromStatus(namazStatus.getAsr());
        ret[3] = getPositionFromStatus(namazStatus.getMagrib());
        ret[4] = getPositionFromStatus(namazStatus.getIsha());
        return ret;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("MAIN", "destroy");
        dbHelper.close();
    }

    class MyOnItemSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

            int viewId = parent.getId();
            String update = parent.getItemAtPosition(pos).toString();
            String msg = "";

            switch (viewId) {

                case R.id.spinner_fajr:
                    msg = "Fajr selected " + update;
                    if (namazStatus.getFajr().equals(update)) break;
                    namazStatus.setFajr(update);
                    dbHelper.updateToday(namazStatus, "fajr", update);
                    break;

                case R.id.spinner_zohar:
                    msg = "Zohar selected " + update;
                    if (namazStatus.getZohar().equals(update)) break;
                    namazStatus.setZohar(update);
                    dbHelper.updateToday(namazStatus, "zohar", update);
                    break;

                case R.id.spinner_asr:
                    msg = "asr selected " + update;
                    if (namazStatus.getAsr().equals(update)) break;
                    namazStatus.setAsr(update);
                    dbHelper.updateToday(namazStatus, "asr", update);
                    break;

                case R.id.spinner_magrib:
                    msg = "magrib selected " + update;
                    if (namazStatus.getMagrib().equals(update)) break;
                    namazStatus.setMagrib(update);
                    dbHelper.updateToday(namazStatus, "magrib", update);
                    break;

                case R.id.spinner_isha:
                    msg = "Isha selected " + update;
                    if (namazStatus.getIsha().equals(update)) break;
                    namazStatus.setIsha(update);
                    dbHelper.updateToday(namazStatus, "isha", update);
                    break;

                default:
                    Log.d("MAIN SPINNER_SELECT", "Unidentified spinner. Any new Salah Type? Need to set.");
                    break;

            }
            Log.d("MAIN SPINNER_SELECT", msg);
            Toast.makeText(NamaazActivity.this, "MAIN SPINNER_SELECT : " + msg, Toast.LENGTH_LONG).show();

        }

        public void onNothingSelected(AdapterView parent) {
            // Do nothing. Already set "BAKI_HAI" during new object creation
        }
    }

}
