package com.example.eabiii.firebasetest;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eabiii on 06/03/2018.
 */

public class FragmentAdapter extends FragmentStatePagerAdapter {

    private final List<Fragment> fragmentList=new ArrayList<>();
    private final List<String>titleList=new ArrayList<>();

    public FragmentAdapter(FragmentManager fm) {
        super(fm);
    }


    public void addFragment(Fragment fragment, String title){
        fragmentList.add(fragment);
        titleList.add(title);


    }



    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}
