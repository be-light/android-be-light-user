package com.example.a1117p.osam.user;


import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import java.util.*;
import android.widget.EditText;
import android.widget.Toast;

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
                        final String html = RequestHttpURLConnection.request("http://121.184.10.219/api/auth/register",params,"POST");
                        runOnUiThread(new Runnable(){
                            
                            @Override
                            public void run() {
                                
                                Toast.makeText(RegisterActivity.this,html,Toast.LENGTH_LONG).show();
                            }
                            
                        });
                        
                    }
                }).start();
            }
        });
    }

    

}
