package com.example.dyk.myapplication;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import static com.example.dyk.myapplication.UserInfo.userInfo;


/**
 * Created by User on 2/28/2017.
 */

public class DataFragment extends Fragment {
    private FragmentTabHost mTabHost;
    int tabNum = userInfo.roomList.size();

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        mTabHost = new FragmentTabHost(getActivity());
        mTabHost.setup(getActivity(), getChildFragmentManager(), R.layout.data_fragment);

        for(int i=0; i<tabNum; i++){
            Bundle arg1 = new Bundle();
            arg1.putInt("Arg for Frag1", 1);

            mTabHost.addTab(mTabHost.newTabSpec(Integer.toString(i)).setIndicator(userInfo.roomList.get(i).get("roomName")),
                    PieChartFrag.class, arg1);

        }
        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                for(int i=0; i<tabNum; i++){
                    if(tabId.equals(Integer.toString(i))){
                        userInfo.clickedTab = i;

                    }
                }
            }
        });
        return mTabHost;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mTabHost = null;
    }

}


