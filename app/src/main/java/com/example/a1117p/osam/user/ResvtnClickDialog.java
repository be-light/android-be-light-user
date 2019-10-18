package com.example.a1117p.osam.user;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class ResvtnClickDialog extends Dialog {
    static InfoWindowData drop_data = null, pick_data = null;
    static String drop_date, pick_date;
    InfoWindowData data;
    Activity context;
    int checkInCount=0;


    public ResvtnClickDialog(@NonNull Activity activity, InfoWindowData data) {
        super(activity);
        this.context = activity;
        this.data = data;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        getWindow().setAttributes(layoutParams);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                checkInCount = Integer.parseInt(((EditText) findViewById(R.id.checkInCount)).getText().toString());
            }
        });
        if(checkInCount!=0)
            ((EditText) findViewById(R.id.checkInCount)).setText(checkInCount+"");
        setContentView(R.layout.resvtn_click_dialog);
        final Button resvtn = findViewById(R.id.rcipt_btn);
        final Calendar cal = Calendar.getInstance();

        final Button checkinB = findViewById(R.id.checkIn);
        final Button checkoutB = findViewById(R.id.checkOut);
        if (drop_date != null)
            checkinB.setText(drop_date);
        if (pick_date != null)
            checkoutB.setText(pick_date);

        checkinB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                DatePickerDialog dialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                        drop_date = String.format("%04d-%02d-%02d", year, month + 1, date);
                        checkinB.setText(drop_date);
                    }
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));

                dialog.getDatePicker().setMinDate(new Date().getTime());    //입력한 날짜 이후로 클릭 안되게 옵션
                dialog.show();


            }
        });
        checkoutB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                DatePickerDialog dialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                        pick_date = String.format("%04d-%02d-%02d", year, month + 1, date);
                        checkoutB.setText(pick_date);
                    }
                }, cal.get(Calendar.YEAR) + 1, cal.get(Calendar.MONTH), cal.get(Calendar.DATE));

                dialog.getDatePicker().setMinDate(new Date().getTime());    //입력한 날짜 이후로 클릭 안되게 옵션
                dialog.show();


            }
        });
        if (drop_data != null) {
            ((TextView) findViewById(R.id.drop_addr)).setText(drop_data.hostAddress);
        }
        if (pick_data != null) {
            ((TextView) findViewById(R.id.pick_addr)).setText(pick_data.hostAddress);
        }
        if (pick_data == null || drop_data == null) {
            resvtn.setText("확인");
        }
        findViewById(R.id.drop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drop_data = data;
                //((TextView) findViewById(R.id.drop_what)).setText("여기가 뭐하는 부분인지 모름");
                ((TextView) findViewById(R.id.drop_addr)).setText(drop_data.hostAddress);
                if (pick_data != null) {
                    resvtn.setText("예약");
                }
            }
        });
        findViewById(R.id.pick).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pick_data = data;
                //((TextView) findViewById(R.id.pick_what)).setText("여기가 뭐하는 부분인지 모름");
                ((TextView) findViewById(R.id.pick_addr)).setText(pick_data.hostAddress);
                if (drop_data != null) {
                    resvtn.setText("예약");
                }
            }
        });
        resvtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final HashMap params = new HashMap<String, String>();
                String paid = "6000";
                String checkin = ((Button) findViewById(R.id.checkIn)).getText().toString();
                String checkout = ((Button) findViewById(R.id.checkOut)).getText().toString();

                params.put("checkIn", checkin);
                params.put("checkOut", checkout);
                params.put("paid", paid);
                params.put("hostIdx", drop_data.hostIdx + "");
                params.put("ghostIdx", pick_data.hostIdx + "");
                params.put("itemCount", checkInCount);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final String html = RequestHttpURLConnection.request("https://be-light.store/api/user/order", params, true, "POST");
                        context.runOnUiThread(new Runnable() {

                            @Override
                            public void run() {

                                Toast.makeText(context, html, Toast.LENGTH_LONG).show();
                            }

                        });

                    }
                }).start();
            }
        });

        findViewById(R.id.cancel_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

}
