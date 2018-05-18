package com.example.dyk.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class ListViewAdapter extends BaseAdapter {
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<ListViewItem> listViewItemList = new ArrayList<ListViewItem>() ;

    // ListViewAdapter의 생성자
    public ListViewAdapter() {

    }

    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return listViewItemList.size() ;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();


        LayoutInflater inf;

        inf = (LayoutInflater)context.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);


        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView roomNameTextView = (TextView) convertView.findViewById(R.id.roomName) ;
        TextView sensorTypeTextView = (TextView) convertView.findViewById(R.id.sensorType) ;
        TextView dateTextView = (TextView) convertView.findViewById(R.id.dateText);
        TextView roomIpTextView = (TextView)convertView.findViewById(R.id.roomIpText);
        TextView roomData = (TextView)convertView.findViewById(R.id.roomData);
        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        ListViewItem listViewItem = listViewItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        roomNameTextView.setText(listViewItem.getRoomName());
        sensorTypeTextView.setText(listViewItem.getElectroType());
        dateTextView.setText(listViewItem.getDateStr());
        roomIpTextView.setText(listViewItem.getRoomIp());
        roomData.setText(listViewItem.getRoomData());

        return convertView;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position ;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position) ;
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem( String roomName, String electroType, String date, String ip, String data) {
        ListViewItem item = new ListViewItem();

        item.setRoomName(roomName);
        item.setElectroType(electroType);
        item.setDateStr(date);
        item.setRoomIp(ip);
        item.setRoomData(data);
        listViewItemList.add(item);
    }
}