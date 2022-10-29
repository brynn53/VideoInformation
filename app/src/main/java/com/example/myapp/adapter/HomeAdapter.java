package com.example.myapp.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

/**
 * @author brynn
 * @since 2022/06/29 7:07
 */
public class HomeAdapter extends FragmentPagerAdapter {

    private String[] mTitle;
    private List<Fragment> mFragment;

    public HomeAdapter(@NonNull FragmentManager fm, String[] titles, List<Fragment> fragments) {
        super(fm);
        this.mFragment = fragments;
        this.mTitle = titles;
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        return mFragment.get(position);
    }

    @Override
    public int getCount() {
        return mFragment.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitle[position];
    }
}
