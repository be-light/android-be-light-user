package com.example.a1117p.osam.user;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.HashMap;

public class ResvtnClickDialog extends Dialog {
    static InfoWindowData drop_data = null, pick_data = null;
    static String drop_date,pick_date;
    InfoWindowData data;
    Activity activity;


    public ResvtnClickDialog(@NonNull Activity activity, InfoWindowData data) {
        super(activity);
        this.activity = activity;
        this.data = data;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        getWindow().setAttributes(layoutParams);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        setContentView(R.layout.resvtn_click_dialog);
        final Button resvtn = findViewById(R.id.rcipt_btn);
        if (drop_data != null) {
            ((TextView) findViewById(R.id.drop_name)).setText(drop_data.hostName);
            ((TextView) findViewById(R.id.drop_addr)).setText(drop_data.hostAddress);
            ((TextView) findViewById(R.id.drop_tel)).setText(drop_data.hostTel);
        }
        if (pick_data != null) {
            ((TextView) findViewById(R.id.pick_name)).setText(pick_data.hostName);
            ((TextView) findViewById(R.id.pick_addr)).setText(pick_data.hostAddress);
            ((TextView) findViewById(R.id.pick_tel)).setText(pick_data.hostTel);
        }
        if (pick_data == null || drop_data == null) {
            resvtn.setText("확인");
        }
        findViewById(R.id.drop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drop_data = data;
                //((TextView) findViewById(R.id.drop_what)).setText("여기가 뭐하는 부분인지 모름");
                ((TextView) findViewById(R.id.drop_name)).setText(drop_data.hostName);
                ((TextView) findViewById(R.id.drop_addr)).setText(drop_data.hostAddress);
                ((TextView) findViewById(R.id.drop_tel)).setText(drop_data.hostTel);
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
                ((TextView) findViewById(R.id.pick_name)).setText(pick_data.hostName);
                ((TextView) findViewById(R.id.pick_addr)).setText(pick_data.hostAddress);
                ((TextView) findViewById(R.id.pick_tel)).setText(pick_data.hostTel);
                if (drop_data != null) {
                    resvtn.setText("예약");
                }
            }
        });
        resvtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity,"예약API미구현",Toast.LENGTH_LONG).show();
                final HashMap params = new HashMap<String, String>();
                String paid="6000";
                String checkin="2019-10-14",checkout="2019-10-15",itemCount="1";
                params.put("checkIn", checkin);
                params.put("checkOut", checkout);
                params.put("paid", paid);
                params.put("hostIdx",drop_data.hostIdx+"");
                params.put("ghostIdx",pick_data.hostIdx+"");
                params.put("itemCount",itemCount);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final String html = RequestHttpURLConnection.request("https://be-light.store/api/user/order", params,true, "POST");
                        activity.runOnUiThread(new Runnable() {

                            @Override
                            public void run() {

                                Toast.makeText(activity, html, Toast.LENGTH_LONG).show();
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
