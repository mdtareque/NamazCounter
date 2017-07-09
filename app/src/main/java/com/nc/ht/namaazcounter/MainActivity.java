package com.nc.ht.namaazcounter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    SingleDayCount namazStatus = new SingleDayCount();
    protected static final String           LOG_TAG = "MainActivity";

    NamazDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new NamazDBHelper(this);
        Button save = (Button) findViewById(R.id.save);
        save.setOnClickListener(onSave);

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
        super.onDestroy();
        dbHelper.close();
    }


}
