package com.example.a1117p.osam.user;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

public class ReciptMgtActivity extends AppCompatActivity {
    ListView listView;
    ListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipt_mgt);
        final ProgressDialog dialog = new ProgressDialog(ReciptMgtActivity.this);
        dialog.setMessage("예약내역을 불러오는 중 입니다.");

        dialog.show();
        listView = findViewById(R.id.hosts);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final String html = RequestHttpURLConnection.request("https://be-light.store/api/user/order", null, true, "GET");
                    JSONParser jsonParser = new JSONParser();
                    JSONArray jsonArray = (JSONArray) jsonParser.parse(html);
                    adapter = new ListViewAdapter(jsonArray);
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            dialog.dismiss();
                            listView.setAdapter(adapter);
                        }

                    });
                } catch (final Exception e) {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            Toast.makeText(ReciptMgtActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            finish();
                        }

                    });
                }
            }
        }).start();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ReciptListItem item = (ReciptListItem) adapter.getItem(position);
                Intent intent = new Intent(ReciptMgtActivity.this,ReciptEditActivity.class);
                intent.putExtra("item",item);
                startActivity(intent);
            }
        });
    }
}
