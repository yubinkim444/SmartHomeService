package com.example.dyk.myapplication;



import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import static com.example.dyk.myapplication.UserInfo.userInfo;


public class MainActivity extends AppCompatActivity {
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView nvDrawer;

    class TabInfo{
        int roomNum;

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userInfo.context = getApplicationContext();
        // Set a Toolbar to replace the ActionBar.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Find our drawer view
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = setupDrawerToggle();

        // Tie DrawerLayout events to the ActionBarToggle
        mDrawer.setDrawerListener(drawerToggle);

        // Find our drawer navigation view
        nvDrawer = (NavigationView) findViewById(R.id.nvView);
        // Setup drawer navigation view
        setupDrawerContent(nvDrawer);



        Fragment fragment = null;
        Class fragmentClass = null;
        fragmentClass = MainFragment.class;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        // Highlight the selected item, update the title, and close the drawer
        // Highlight the selected item has been done by NavigationView
        // menuItem.setChecked(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
       switch (item.getItemId()){
           case R.id.action_addRoom:
               Intent in = new Intent(this,AddRoomActivity.class);
               startActivity(in);
               break;
       }
        return super.onOptionsItemSelected(item);
    }

    // `onPostCreate` called when activity start-up is complete after `onStart()`
    // NOTE! Make sure to override the method with only a single `Bundle` argument
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }


    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    @SuppressWarnings("StatementWithEmptyBody")
    public void selectDrawerItem(MenuItem menuItem) {
        mDrawer.closeDrawers();

        int id =menuItem.getItemId();
        Fragment fragment = null;
        // f.isDetached();
        //f.onDetach();

        Class fragmentClass = null;
        if (id == R.id.nav_first_fragment) {
            fragmentClass = MainFragment.class;


        } else if (id == R.id.nav_second_fragment) {
            fragmentClass = DataFragment.class;

        } else if (id == R.id.nav_third_fragment) {
            fragmentClass = TimeFragment.class;

        } else {
            fragmentClass = SettingFragment.class;

        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        // Highlight the selected item, update the title, and close the drawer
        // Highlight the selected item has been done by NavigationView
        // menuItem.setChecked(true);
        setTitle(menuItem.getTitle());


    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }
    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.navigation_drawer_open,  R.string.navigation_drawer_close);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * 스크린에 맞는 OptionMenu를 화면에 표시하기 위해 준비할 때 호출 된다.
     * 옵션메뉴가 화면에 나타날 때마다 호출 된다.
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        return super.onPrepareOptionsMenu(menu);
    }



    /**
     * OptionMenu가 종료될 때 호출 된다.
     */
    @Override
    public void onOptionsMenuClosed(Menu menu) {
        Log.d("OptionMenu", "onOptionsMenuClosed()");
        super.onOptionsMenuClosed(menu);
    }

    /**
     * OptionMenu가 강제로 Open될 때 호출 된다.
     */
    @Override
    public void openOptionsMenu() {
        Toast.makeText(MainActivity.this, "OptionMenu 강제 호출",
                Toast.LENGTH_SHORT).show();
        super.openOptionsMenu();
    }

    /**
     * OptionMenu가 강제로 Close될 때 호출 된다.
     */
    @Override
    public void closeOptionsMenu() {
        Toast.makeText(MainActivity.this, "OptionMenu 강제 종료",
                Toast.LENGTH_SHORT).show();
        super.closeOptionsMenu();
    }




}
