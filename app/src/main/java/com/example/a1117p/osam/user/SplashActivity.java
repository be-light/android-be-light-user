package com.example.a1117p.osam.user;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.HashMap;

import static java.lang.Thread.sleep;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        findViewById(R.id.register_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(SplashActivity.this, RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });
        findViewById(R.id.login_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                findViewById(R.id.login_form).setVisibility(View.VISIBLE);
                ((Button) v).setText("Log in");
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String id = ((EditText) findViewById(R.id.id)).getText().toString();
                        String passwd = ((EditText) findViewById(R.id.passwd)).getText().toString();
                        if (id.equals("")) {
                            Toast.makeText(SplashActivity.this, "ID를 입력하세요", Toast.LENGTH_LONG).show();
                            return;
                        } else if (passwd.equals("")) {
                            Toast.makeText(SplashActivity.this, "비밀번호를 입력하세요", Toast.LENGTH_LONG).show();
                            return;
                        }
                        final HashMap params = new HashMap<String, String>();

                        params.put("userId", id);
                        params.put("userPassword", passwd);
                        ProgressDialog dialog = new ProgressDialog(SplashActivity.this);

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                final String html = RequestHttpURLConnection.request("https://be-light.store/api/auth/login", params, "POST");

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        JSONParser parser = new JSONParser();
                                        try {
                                            JSONObject object = (JSONObject) parser.parse(html);
                                            Long status = (Long) object.get("status");
                                            if (status == 200) {
                                                Intent i = new Intent(SplashActivity.this, MapActivity.class);
                                                startActivity(i);
                                                finish();
                                            } else {
                                                Toast.makeText(SplashActivity.this, "ID와 PW를 확인하세요.", Toast.LENGTH_LONG).show();
                                            }
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                            Toast.makeText(SplashActivity.this, "에러가 발생하였습니다.", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });

                            }
                        }).start();
                    }
                });
            }
        });
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 0);

        } else {
            StartThread(getIntent().getBooleanExtra("needLoading", true));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (grantResults.length <= 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "권한없음!!!!", Toast.LENGTH_LONG).show();
            finish();
        } else {
            StartThread(true);
        }

    }

    void StartThread(boolean b) {
        Intent i = new Intent(SplashActivity.this, MapActivity.class);
        startActivity(i);
        finish();
        /*if (b) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        sleep(1500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            View v = findViewById(R.id.btns);
                            v.setVisibility(View.VISIBLE);
                            AlphaAnimation animation = new AlphaAnimation(0, 1);
                            animation.setDuration(1000);
                            v.startAnimation(animation);

                        }
                    });
                }
            }).start();
        } else {
            findViewById(R.id.btns).setVisibility(View.VISIBLE);
            findViewById(R.id.login_btn).performClick();

        }*/
    }
}
