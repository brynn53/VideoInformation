package com.example.myapp.activity;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.myapp.R;
import com.example.myapp.adapter.MyPagerAdapter;
import com.example.myapp.entity.TabEntity;
import com.example.myapp.fragment.NewsFragment;
import com.example.myapp.fragment.HomeFragment;
import com.example.myapp.fragment.MyFragment;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends BaseActivity {

    private String[] mTitles = {"首页", "资讯", "我的"};

    // 导航栏图片
    private int[] mIconUnselectIds = {
            R.mipmap.home_unselect, R.mipmap.collect_unselect, R.mipmap.my_unselect};
    private int[] mIconSelectIds = {
            R.mipmap.home_selected, R.mipmap.collect_selected, R.mipmap.my_selected};

    private ViewPager viewPager;
    private CommonTabLayout commonTabLayout;

    // ViewPager中的fragment
    private List<Fragment> mFragments = new ArrayList<>();
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();

    private MyPagerAdapter myPagerAdapter;


    @Override
    protected int initLayout() {
        return R.layout.activity_home;
    }

    @Override
    protected void initView() {
        viewPager = findViewById(R.id.viewpager);
        commonTabLayout = findViewById(R.id.commonTabLayout);
    }

    @Override
    protected void initData() {
        // ViewPager中要添加的fragment
        mFragments.add(HomeFragment.newInstance());
        mFragments.add(NewsFragment.newInstance());
        mFragments.add(MyFragment.newInstance());

        // 标题栏和图标
        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }

        //添加标题栏数据
        commonTabLayout.setTabData(mTabEntities);
        commonTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            // 当tab选中时
            @Override
            public void onTabSelect(int position) {
                // 通过下标选择viewPager中的页面
                viewPager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
        // 给viewPager添加适配器
        myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), mTitles, mFragments);
        viewPager.setAdapter(myPagerAdapter);
        // 预加载viewPager中的fragment，当前页为0时，会预加载页面1
        viewPager.setOffscreenPageLimit(mFragments.size());
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            // 当页面处于滑动状态时
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            // 当前选中页面和上一个选中的页面不是同一页面时
            @Override
            public void onPageSelected(int position) {
                commonTabLayout.setCurrentTab(position);
            }

            // 页面滑动状态改变
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}