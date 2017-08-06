package com.nc.ht.namaazcounter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    SingleDayCount namazStatus;
    protected static final String           LOG_TAG = "MainActivity";

    NamazDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new NamazDBHelper(this);
        namazStatus = dbHelper.populateTodaysAndGetObject();

        //https://stackoverflow.com/questions/2562248/how-to-keep-onitemselected-from-firing-off-on-a-newly-instantiated-spinner

        int[] spinners = new int[] {
                R.id.spinner_fajr, R.id.spinner_zohar, R.id.spinner_asr, R.id.spinner_magrib, R.id.spinner_isha
        };
        int[] initialStatus = getInitialPositionArray(namazStatus);
        for(int index=0; index<5; index++) {
            Spinner tmp = (Spinner) findViewById(spinners[index]);
            tmp.setSelection( initialStatus[index], false);
        }
        Log.d("MAIN", "spinners initial status set");

        MyOnItemSelectedListener spinnerListener = new MyOnItemSelectedListener();
        for(int i : spinners) {
            Spinner tmp = (Spinner) findViewById(i);
            tmp.setOnItemSelectedListener(spinnerListener);
        }
        Log.d("MAIN", "ItemSelectedListener set");

    }

    private int getPositionFromStatus(String s) {
        switch (s) {
            case "Baki hai":
                return 0;
            case "Ada Kiya":
                return 1;
            case "Qaza":
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

    class MyOnItemSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            int viewId = parent.getId();
            String update = parent.getItemAtPosition(pos).toString();

            String msg = "";
            switch(viewId) {
                case R.id.spinner_fajr:
                    msg = "Fajr selected " + update;
                    if(namazStatus.getFajr() == update) break;
                    namazStatus.setFajr(update);
                    dbHelper.updateToday(namazStatus, "fajr", update);

                break;
                case R.id.spinner_zohar:
                    msg = "Zohar selected " + update;
                    if(namazStatus.getZohar() == update) break;
                    namazStatus.setZohar(update);
                    dbHelper.updateToday(namazStatus, "zohar", update);

                    break;
                case R.id.spinner_asr:
                    msg = "asr selected " + update;
                    if(namazStatus.getAsr() == update) break;
                    namazStatus.setAsr(update);
                    dbHelper.updateToday(namazStatus, "asr", update);

                    break;
                case R.id.spinner_magrib:
                    msg = "magrib selected " + update;
                    if(namazStatus.getMagrib() == update) break;
                    namazStatus.setMagrib(update);
                    dbHelper.updateToday(namazStatus, "magrib", update);

                    break;
                case R.id.spinner_isha:
                    msg = "Isha selected " + update;
                    if(namazStatus.getIsha() == update) break;
                    namazStatus.setIsha(update);
                    dbHelper.updateToday(namazStatus, "isha", update);
                    break;
                default:
                    Log.d("MAIN SPINNER_SELECT", "Unidentified spinner. Any new Salah Type? Need to set.");
                    break;

            }
            Log.d("MAIN SPINNER_SELECT",  msg);
            Toast.makeText(MainActivity.this, "MAIN SPINNER_SELECT : " + msg, Toast.LENGTH_LONG).show();

        }

        public void onNothingSelected(AdapterView parent) {
            // Do nothing.
        }
    }
    private View.OnClickListener onSave = new View.OnClickListener() {
        public void onClick(View v) {
            Spinner hold;
            hold = (Spinner) findViewById(R.id.spinner_fajr);
            namazStatus.setFajr(hold.getSelectedItem().toString());

            hold = (Spinner) findViewById(R.id.spinner_zohar);
            namazStatus.setZohar(hold.getSelectedItem().toString());

            hold = (Spinner) findViewById(R.id.spinner_asr);
            namazStatus.setAsr(hold.getSelectedItem().toString());

            hold = (Spinner) findViewById(R.id.spinner_magrib);
            namazStatus.setMagrib(hold.getSelectedItem().toString());

            hold = (Spinner) findViewById(R.id.spinner_isha);
            namazStatus.setIsha(hold.getSelectedItem().toString());

            Toast.makeText(MainActivity.this, namazStatus.toString(), Toast.LENGTH_LONG).show();
            dbHelper.insertSingleDay(namazStatus);
            Toast.makeText(MainActivity.this, "Row insert", Toast.LENGTH_LONG).show();

        }
    };

    @Override
    protected void onDestroy() {
        Log.d("MAIN", "destroy");
        super.onDestroy();
        dbHelper.close();
    }


}
