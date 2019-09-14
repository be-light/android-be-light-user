package com.example.a1117p.osam.user;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;

public class ReciptEditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_edit);
        final ReciptListItem item = getIntent().getParcelableExtra("item");

        findViewById(R.id.edit_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = ((EditText) findViewById(R.id.hostname)).getText().toString();
                String tel = ((EditText) findViewById(R.id.tel)).getText().toString();
                String address = ((EditText) findViewById(R.id.address)).getText().toString();
                String postalCode = ((EditText) findViewById(R.id.postalcode)).getText().toString();
                if (name.equals("")) {
                    Toast.makeText(ReciptEditActivity.this, "호스트명을 입력하세요", Toast.LENGTH_LONG).show();
                    return;
                } else if (tel.equals("")) {
                    Toast.makeText(ReciptEditActivity.this, "전화번호를 입력하세요", Toast.LENGTH_LONG).show();
                    return;
                }else if (address.equals("")) {
                    Toast.makeText(ReciptEditActivity.this, "주소를 입력하세요", Toast.LENGTH_LONG).show();
                    return;
                }else if (postalCode.equals("")) {
                    Toast.makeText(ReciptEditActivity.this, "우편번호를 입력하세요", Toast.LENGTH_LONG).show();
                    return;
                }
                final HashMap params = new HashMap<String, String>();
                params.put("idx",item.getReciptNumber());
                params.put("hostName", name);
                params.put("hostTel", tel);
                params.put("hostAddress", address);
                params.put("hostPostalCode", postalCode);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final String html = RequestHttpURLConnection.request("http://15.164.220.47/api/host?_method=PUT", params,true, "POST");
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
