package com.example.a1117p.osam.user;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {
    ArrayList<ReciptListItem> hosts = new ArrayList<>();
    ListViewAdapter(JSONArray jsonArray){
        for(Object object:jsonArray){
            hosts.add(new ReciptListItem((JSONObject) object));
        }
    }
    @Override
    public int getCount() {
        return hosts.size();
    }

    @Override
    public Object getItem(int position) {
        return hosts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.hostitem, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView checkin = convertView.findViewById(R.id.checkin) ;
        TextView checkOut = convertView.findViewById(R.id.checkOut) ;
        TextView paid = convertView.findViewById(R.id.paid) ;
        TextView hostaddress = convertView.findViewById(R.id.hostaddress) ;
        TextView hostPostalCode = convertView.findViewById(R.id.hostPostalCode) ;
        TextView hostName = convertView.findViewById(R.id.hostName) ;
        TextView hostUserPhoneNumber = convertView.findViewById(R.id.hostUserPhoneNumber) ;

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        ReciptListItem listViewItem = hosts.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        checkin.setText("체크인 : "+listViewItem.getCheckin());
        checkOut.setText("체크아웃 : "+listViewItem.getCheckOut());
        paid.setText("가격 : "+listViewItem.getPaid());
        hostaddress.setText("주소 : "+listViewItem.getHostaddress());
        hostPostalCode.setText("우편주소 : "+listViewItem.getHostPostalCode());
        hostName.setText("호스트명 : "+listViewItem.getHostName());
        hostUserPhoneNumber.setText("호스트전화번호 : "+listViewItem.getHostUserPhoneNumber());

        return convertView;
    }
}
