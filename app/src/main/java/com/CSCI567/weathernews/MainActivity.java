/*
 * Weather & News App.
 * CSCI 567 Project
 * Author : Ganesh Joshi
 * 
*/
package com.CSCI567.weathernews;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener {

    ViewPager viewPager=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager= (ViewPager) findViewById(R.id.pager);


        final ActionBar actionBar=getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        addTabs(actionBar);
        viewPager.setAdapter(new TabAdapter(getSupportFragmentManager()));
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {

            }

            @Override
            public void onPageSelected(int i) {
                actionBar.setSelectedNavigationItem(i);

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

    }
    private void addTabs(ActionBar actionBar)
    {
        ActionBar.Tab tab1=actionBar.newTab();
        tab1.setText("Weather");
        tab1.setTabListener(this);

        ActionBar.Tab tab2=actionBar.newTab();
        tab2.setText("News");
        tab2.setTabListener(this);

        ActionBar.Tab tab3=actionBar.newTab();
        tab3.setText("Stock");
        tab3.setTabListener(this);

        actionBar.addTab(tab1);
        actionBar.addTab(tab2);
        actionBar.addTab(tab3);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        viewPager.setCurrentItem(tab.getPosition());

    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }



}

class TabAdapter extends FragmentStatePagerAdapter
{

    public TabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        Fragment fragment=null;
        if(i==0)
        {
            fragment=new Weather();
        }
        if(i==1)
        {
            fragment=new News();
        }
        if(i==2)
        {
            fragment=new Stocks();
        }
        return fragment;
    }


    @Override
    public int getCount() {
        return 3;
    }
}
