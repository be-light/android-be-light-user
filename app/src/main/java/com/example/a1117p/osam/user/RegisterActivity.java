package com.example.a1117p.osam.user;


import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import java.util.*;
import android.widget.EditText;
import android.widget.Toast;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        
        findViewById(R.id.register_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = ((EditText)findViewById(R.id.name)).getText().toString();
                String id = ((EditText)findViewById(R.id.id)).getText().toString();
                String passwd = ((EditText)findViewById(R.id.passwd)).getText().toString();
                String passwd_confirm = ((EditText)findViewById(R.id.passwd_confirm)).getText().toString();
                String email = ((EditText)findViewById(R.id.email)).getText().toString();
                String phone = ((EditText)findViewById(R.id.phone)).getText().toString();
                String address = ((EditText)findViewById(R.id.address)).getText().toString();
                if(id.equals("")){
                    Toast.makeText(RegisterActivity.this,"ID를 입력하세요",Toast.LENGTH_LONG).show();
                    return;
                }
                else if(name.equals("")){
                    Toast.makeText(RegisterActivity.this,"이름을 입력하세요",Toast.LENGTH_LONG).show();
                    return;
                }
                else if(passwd.equals("")){
                    Toast.makeText(RegisterActivity.this,"비밀번호를 입력하세요",Toast.LENGTH_LONG).show();
                    return;
                }
                else if(passwd_confirm.equals("")){
                    Toast.makeText(RegisterActivity.this,"비밀번호확인을 입력하세요",Toast.LENGTH_LONG).show();
                    return;
                }
                else if(!passwd.equals(passwd_confirm)){
                    Toast.makeText(RegisterActivity.this,"비밀번호와 비밀번호 확인이 같지않습니다.",Toast.LENGTH_LONG).show();
                    return;
                }
                else if(email.equals("")){
                    Toast.makeText(RegisterActivity.this,"이메일을 입력하세요",Toast.LENGTH_LONG).show();
                    return;
                }
                else if(phone.equals("")){
                    Toast.makeText(RegisterActivity.this,"전화번호를 입력하세요",Toast.LENGTH_LONG).show();
                    return;
                }
                else if(address.equals("")){
                    Toast.makeText(RegisterActivity.this,"주소를 입력하세요",Toast.LENGTH_LONG).show();
                    return;
                }
                final HashMap params = new HashMap<String, String>(); 
                
                params.put("userId",id);
                params.put("userName",name);
                params.put("userPassword",passwd);
                params.put("userEmail",email);
                params.put("userPhoneNumber",phone);
                params.put("userAddress",address);
                new Thread(new Runnable(){
                    @Override
                    public void run() {
                        final String html = RequestHttpURLConnection.request("https://be-light.store/api/auth/register",params,"POST");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                JSONParser parser = new JSONParser();
                                try {
                                    JSONObject object = (JSONObject) parser.parse(html);
                                    Long status = (Long) object.get("status");
                                    if(status==200){
                                        Intent i = new Intent(RegisterActivity.this,SplashActivity.class);
                                        i.putExtra("fromRegister",true);
                                        startActivity(i);
                                        Toast.makeText(RegisterActivity.this,"회원가입에 성공하였습니다.",Toast.LENGTH_LONG).show();
                                    }
                                    else{
                                        Toast.makeText(RegisterActivity.this,"회원가입에 실패하였습니다.",Toast.LENGTH_LONG).show();
                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                    Toast.makeText(RegisterActivity.this,"에러가 발생하였습니다.",Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                        
                    }
                }).start();
            }
        });
    }

    

}
