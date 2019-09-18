package com.example.a1117p.osam;


import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.*;
import android.webkit.CookieManager;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.util.*;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
    
         new Thread(new Runnable(){
            @Override
            public void run() {
                try{
                    final String html = RequestHttpURLConnection.request("http://15.164.220.47/api/user",null,true,"GET");
                    JSONParser jsonParser = new JSONParser();
                    JSONObject jsonObj = (JSONObject) jsonParser.parse(html);
                    final JSONObject jsonObj2 = (JSONObject) jsonObj.get("user");
                    runOnUiThread(new Runnable(){

                        @Override
                        public void run() {
                            
                            ((TextView)findViewById(R.id.profile)).setText(html);
                            ((TextView)findViewById(R.id.id)).setText((String)jsonObj2.get("userId"));
                            ((TextView)findViewById(R.id.name)).setText((String)jsonObj2.get("userName"));
                            ((EditText)findViewById(R.id.email)).setText((String)jsonObj2.get("userEmail"));
                            ((EditText)findViewById(R.id.phone)).setText((String)jsonObj2.get("userPhoneNumber"));
                            ((EditText)findViewById(R.id.address)).setText((String)jsonObj2.get("userAddress"));
                        }

                    });
                }
                catch(Exception e){
                    Toast.makeText(ProfileActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        }).start();
        findViewById(R.id.edit_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               
                String email = ((EditText)findViewById(R.id.email)).getText().toString();
                String phone = ((EditText)findViewById(R.id.phone)).getText().toString();
                String address = ((EditText)findViewById(R.id.address)).getText().toString();
                if(email.equals("")){
                    Toast.makeText(ProfileActivity.this,"이메일을 입력하세요",Toast.LENGTH_LONG).show();
                    return;
                }
                else if(phone.equals("")){
                    Toast.makeText(ProfileActivity.this,"전화번호를 입력하세요",Toast.LENGTH_LONG).show();
                    return;
                }
                else if(address.equals("")){
                    Toast.makeText(ProfileActivity.this,"주소를 입력하세요",Toast.LENGTH_LONG).show();
                    return;
                }
                final HashMap params = new HashMap<String, String>(); 

                params.put("userEmail",email);
                params.put("userPhoneNumber",phone);
                params.put("userAddress",address);
                new Thread(new Runnable(){
                    @Override
                    public void run() {
                        final String html = RequestHttpURLConnection.request("http://15.164.220.47/api/user?_method=PUT",params,true,"POST");
                        runOnUiThread(new Runnable(){
                            
                            @Override
                            public void run() {
                                
                                Toast.makeText(ProfileActivity.this,html,Toast.LENGTH_LONG).show();
                            }
                            
                        });
                        
                    }
                }).start();
            }
        });
    }

    

}
