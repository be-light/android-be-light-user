package com.example.a1117p.osam.user;


import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity {


    void OvalProfile() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ImageView imageView = findViewById(R.id.profile_img);
            imageView.setBackground(new ShapeDrawable(new OvalShape()));
            imageView.setClipToOutline(true);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        OvalProfile();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final String html = RequestHttpURLConnection.request("https://be-light.store/api/user", null, true, "GET");
                    JSONParser jsonParser = new JSONParser();
                    JSONObject jsonObj = (JSONObject) jsonParser.parse(html);
                    final JSONObject jsonObj2 = (JSONObject) jsonObj.get("user");
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            ((TextView) findViewById(R.id.id)).setText((String) jsonObj2.get("userId"));
                            ((TextView) findViewById(R.id.name)).setText((String) jsonObj2.get("userName"));
                            ((EditText) findViewById(R.id.email)).setText((String) jsonObj2.get("userEmail"));
                            ((EditText) findViewById(R.id.phone)).setText((String) jsonObj2.get("userPhoneNumber"));
                            ((EditText) findViewById(R.id.address)).setText((String) jsonObj2.get("userAddress"));
                        }

                    });
                } catch (final Exception e) {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Toast.makeText(ProfileActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            finish();
                        }

                    });
                }
            }
        }).start();
        findViewById(R.id.edit_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = ((EditText) findViewById(R.id.email)).getText().toString();
                String phone = ((EditText) findViewById(R.id.phone)).getText().toString();
                String address = ((EditText) findViewById(R.id.address)).getText().toString();
                String passwd = ((EditText) findViewById(R.id.passwd)).getText().toString();
                String passwd_confirm = ((EditText) findViewById(R.id.passwd_confirm)).getText().toString();
                if (email.equals("")) {
                    Toast.makeText(ProfileActivity.this, "이메일을 입력하세요", Toast.LENGTH_LONG).show();
                    return;
                } else if (phone.equals("")) {
                    Toast.makeText(ProfileActivity.this, "전화번호를 입력하세요", Toast.LENGTH_LONG).show();
                    return;
                } else if (address.equals("")) {
                    Toast.makeText(ProfileActivity.this, "주소를 입력하세요", Toast.LENGTH_LONG).show();
                    return;
                } else if (!passwd.equals("")) {
                    if (passwd_confirm.equals("")) {
                        Toast.makeText(ProfileActivity.this, "비밀번호확인을 입력하세요", Toast.LENGTH_LONG).show();
                        return;
                    } else if (!passwd_confirm.equals(passwd)) {
                        Toast.makeText(ProfileActivity.this, "비밀번호와 비밀번호 확인이 같지않습니다.", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                final HashMap params = new HashMap<String, String>();

                params.put("userEmail", email);
                params.put("userPhoneNumber", phone);
                params.put("userAddress", address);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final String html = RequestHttpURLConnection.request("https://be-light.store/api/user?_method=PUT", params, true, "POST");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                JSONParser parser = new JSONParser();
                                try {
                                    JSONObject object = (JSONObject) parser.parse(html);
                                    Long status = (Long) object.get("status");
                                    if (status == 200) {
                                        Toast.makeText(ProfileActivity.this, "프로필을 수정하였습니다.", Toast.LENGTH_LONG).show();
                                        finish();
                                    } else {
                                        Toast.makeText(ProfileActivity.this, "프로필수정을 실패하였습니다.", Toast.LENGTH_LONG).show();
                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                    Toast.makeText(ProfileActivity.this, "에러가 발생하였습니다.", Toast.LENGTH_LONG).show();
                                    finish();
                                }
                            }
                        });

                    }
                }).start();
            }
        });
    }


}
