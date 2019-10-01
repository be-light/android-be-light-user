package com.example.a1117p.osam.user;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class ReciptEditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipt_edit);
        final ReciptListItem item = getIntent().getParcelableExtra("item");
        ((TextView)findViewById(R.id.hostidx)).setText(item.getHostIdx());

        final Button checkin = findViewById(R.id.checkIn);
        final Button checkout = findViewById(R.id.checkOut);
        checkin.setText(item.getCheckin());
        checkout.setText(item.getCheckOut());

        checkin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            String[] tmp = checkin.getText().toString().split("-");
            int[] cal = {Integer.valueOf(tmp[0]),Integer.valueOf(tmp[1]),Integer.valueOf(tmp[2])};

                DatePickerDialog dialog = new DatePickerDialog(ReciptEditActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                        checkin.setText(String.format("%04d-%02d-%02d", year, month+1, date));
                    }
                }, cal[0], cal[1]-1, cal[2]);

                dialog.getDatePicker().setMinDate(new Date().getTime());    //입력한 날짜 이후로 클릭 안되게 옵션
                dialog.show();


            }
        });
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String[] tmp = checkout.getText().toString().split("-");
                int[] cal = {Integer.valueOf(tmp[0]),Integer.valueOf(tmp[1]),Integer.valueOf(tmp[2])};

                DatePickerDialog dialog = new DatePickerDialog(ReciptEditActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                        checkout.setText(String.format("%04d-%02d-%02d", year, month+1, date));
                    }
                }, cal[0], cal[1]-1, cal[2]);

                dialog.getDatePicker().setMinDate(new Date().getTime());    //입력한 날짜 이후로 클릭 안되게 옵션
                dialog.show();


            }
        });

        findViewById(R.id.edit_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String checkIn = ((Button) findViewById(R.id.checkIn)).getText().toString();
                String checkOut = ((Button) findViewById(R.id.checkOut)).getText().toString();

                final HashMap params = new HashMap<String, String>();
                params.put("reciptNumber",item.getReciptNumber());
                params.put("checkIn", checkIn);
                params.put("checkOut", checkOut);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final String html = RequestHttpURLConnection.request("https://be-light.store/api/user/order?_method=PUT", params,true, "POST");
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {

                                Toast.makeText(ReciptEditActivity.this, html, Toast.LENGTH_LONG).show();
                            }

                        });

                    }
                }).start();
            }
        });
    }
}
