package com.example.myapp.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapp.R;
import com.example.myapp.adapter.HomeAdapter;
import com.example.myapp.api.Api;
import com.example.myapp.api.ApiConfig;
import com.example.myapp.api.TtitCallback;
import com.example.myapp.entity.CategoryEntity;
import com.example.myapp.entity.VideoCategoryResponse;
import com.flyco.tablayout.SlidingTabLayout;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends BaseFragment {

    private List<Fragment> mFragments = new ArrayList<>();
    private String[] mTitles;
    private ViewPager viewPager;
    private SlidingTabLayout slidingTabLayout;
    private HomeAdapter homeAdapter;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    protected int initLayout() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView() {
        viewPager = mRootView.findViewById(R.id.fixedViewpager);
        slidingTabLayout = mRootView.findViewById(R.id.slidingTabLayout);
    }

    @Override
    protected void initData() {
        getVideoCategoryList();
    }

    private void getVideoCategoryList() {
        HashMap<String, Object> map = new HashMap<>();
        String token = getStringFromSp("token");
        map.put("token", token);
        Api.config(ApiConfig.VIDEO_CATEGORY_LIST, map).getRequest(new TtitCallback() {
            @Override
            public void onSuccess(String res) {
               getActivity().runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
                       VideoCategoryResponse response = new Gson().fromJson(res, VideoCategoryResponse.class);
                       if (response != null && response.getCode() == 0) {
                           List<CategoryEntity> categoryEntities = response.getPage().getList();
                           mTitles = new String[categoryEntities.size()];
                           for (int i=0; i<categoryEntities.size(); i++) {
                               mTitles[i] = categoryEntities.get(i).getCategoryName();
                               mFragments.add(VideoFragment.newInstance(categoryEntities.get(i).getCategoryId()));
                           }
                           homeAdapter = new HomeAdapter(getChildFragmentManager(), mTitles, mFragments);
                           viewPager.setOffscreenPageLimit(mFragments.size());
                           viewPager.setAdapter(homeAdapter);
                           slidingTabLayout.setViewPager(viewPager);
                       }
                   }
               });
            }

            @Override
            public void onFailure(Exception e) {

            }
        });
    }

    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }
}