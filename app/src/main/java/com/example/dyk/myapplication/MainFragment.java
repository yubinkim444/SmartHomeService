package com.example.dyk.myapplication;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import static android.R.attr.fragment;
import static android.R.attr.type;
import static com.example.dyk.myapplication.UserInfo.userInfo;


public class MainFragment extends Fragment {

    int port;
    Thread Thread;
    Socket socket;
    String ip;
    String outputMsg ;
    char msg[] = new char[8];
    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);


        ListViewAdapter adapter;

        // Adapter 생성
        adapter = new ListViewAdapter() ;

        // 리스트뷰 참조 및 Adapter달기
        View v = inflater.inflate(R.layout.fragment_main,container,false);
       ListView listview = (ListView)v.findViewById(R.id.roomListFragment);
        listview.setAdapter(adapter);
//---------------------------------------------------------------------------------------
        ip = userInfo.myInfo.get(0).get("managerIp");
        Log.d("IPPPPPPP",ip);
        port = 5100;
        GetRoomData getRoom = new GetRoomData();
        getRoom.start();
        try {
            getRoom.join();
        }
        catch (Exception e){
            Toast.makeText(getActivity() ,"Interrupt 발생",
                    Toast.LENGTH_SHORT).show();

        }

        int size = userInfo.roomList.size();
        for(int i=0; i<size; i++){
            String ip = userInfo.roomList.get(i).get("roomIp");
            switch (userInfo.roomList.get(i).get("electroType")){
                case "냉방기":
                case "난방기":
                    outputMsg= "RT" + ip+'\0';

                    break;
                case "가습기":
                case "제습기":
                    outputMsg= "RH" + ip+'\0';
                    break;
                case "조명":
                    outputMsg= "RL" + ip+'\0';
                    break;
                case "움직임 감지":
                    outputMsg= "RM" + ip+'\0';
                    break;
                default: break;

            }
          DataManager();
            try {
               Thread.join();
            }
            catch (Exception e){
                Toast.makeText(getActivity(),"Interrupt 발생",
                        Toast.LENGTH_SHORT).show();
            }
           String roomData = charToString(msg);
           //String roomData = ""; //임시... 되돌려 놓으셈
            Log.d("roomName",userInfo.roomList.get(i).get("roomName"));
            adapter.addItem(userInfo.roomList.get(i).get("roomName"), userInfo.roomList.get(i).get("electroType"), userInfo.roomList.get(i).get("roomInsertDate"),userInfo.roomList.get(i).get("roomIp"),roomData) ;

        }

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // get item
                ListViewItem item = (ListViewItem) parent.getItemAtPosition(position) ;

                Intent intent = new Intent(getActivity(),OptimumSetting.class);
                intent.putExtra("roomNum",position);
                Log.d("roomNum",Integer.toString(position));
                startActivity(intent);
            }
        }) ;
        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            Button modiBtn ;
            Button delBtn;
            EditText name;
            EditText roomIp;
            Spinner type ;
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, final int position, long arg3){
                final Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.modify_room_info);
                modiBtn = (Button)dialog.findViewById(R.id.modifyButton);
                delBtn = (Button)dialog.findViewById(R.id.delButton);
                name = (EditText) dialog.findViewById(R.id.roomName);
                roomIp = (EditText) dialog.findViewById(R.id.roomIp);
                type = (Spinner) dialog.findViewById(R.id.spinner);
                name.setText(userInfo.roomList.get(position).get("roomName"));
                roomIp.setText(userInfo.roomList.get(position).get("roomIp"));
                ArrayAdapter<CharSequence> aa= ArrayAdapter.createFromResource(userInfo.context,R.array.items ,android.R.layout.simple_spinner_dropdown_item);
// spin에  adapter를 붙여넣는다.
                type.setAdapter(aa);
                dialog.setTitle("방 정보 수정");

                modiBtn.setOnClickListener(new View.OnClickListener() {
                    int pos = position;

                    @Override
                    public void onClick(View view) {
                        modifyRoom(pos,name.getText().toString(),roomIp.getText().toString(), ((TextView)type.getSelectedView()).getText().toString());
                        reLoadFragment();
                        dialog.dismiss();
                    }
                });
                delBtn.setOnClickListener(new View.OnClickListener(){
                    int pos = position;
                    public void onClick(View view){
                        delRoom(pos);
                        reLoadFragment();
                        dialog.dismiss();
                    }

                });
                dialog.show();
                return true;
            }

            public void delRoom(int pos){
                DelRoom del = new DelRoom(pos);
                del.start();
                try {
                    del.join();
                    userInfo.roomList.remove(pos);
                }
                catch (Exception e){
                    return;
                }
            }
        public void modifyRoom(int pos,String name,String ip,String type){
            ModiRoom modi = new ModiRoom(pos,name,ip,type);
            modi.start();
            try {
                modi.join();
                userInfo.roomList.get(pos).put("roomName", name);
                userInfo.roomList.get(pos).put("roomIp", ip);
                userInfo.roomList.get(pos).put("electroType", type);
            }
            catch (Exception e){
                return;
            }
        }
        });

        // Inflate the layout for this fragment
        return v;
    }


    public void reLoadFragment()
    {

        Fragment fragment = null;
        Class fragmentClass = null;
        fragmentClass = MainFragment.class;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
            android.support.v4.app.FragmentTransaction fragTransaction =  this.getFragmentManager().beginTransaction();
            fragTransaction.replace(R.id.flContent, fragment).commit();

    }
    public String charToString(char [] ch){
        String input = "";
        for(int i=2; i<ch.length; i++){
            input += ch[i];
        }
        return input;
    }

    public void DataManager() {
        Thread = new Thread() {
            public void run(){
                super.run();
                try {
                    socket = new Socket(ip, port);
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    out.write(outputMsg);
                    out.flush();
                    in.read(msg);
                    socket.close();
                }
                catch(IOException e) {
                    System.out.println(e.getMessage());
                }
                finally {
                }
            }
        };
        Thread.start();
    }

}
