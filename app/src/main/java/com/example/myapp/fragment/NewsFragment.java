package com.example.myapp.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.myapp.R;
import com.example.myapp.activity.LoginActivity;
import com.example.myapp.activity.WebActivity;
import com.example.myapp.adapter.NewsAdapter;
import com.example.myapp.adapter.VideoAdapter;
import com.example.myapp.api.Api;
import com.example.myapp.api.ApiConfig;
import com.example.myapp.api.TtitCallback;
import com.example.myapp.entity.NewsEntity;
import com.example.myapp.entity.NewsListResponse;
import com.example.myapp.entity.VideoEntity;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsFragment extends BaseFragment {
    private RefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private NewsAdapter newsAdapter;
    private List<NewsEntity> datas = new ArrayList<>();
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    newsAdapter.setDatas(datas);
                    //刷新界面
                    newsAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    //分页属性
    private int pageNum = 1;

    public NewsFragment() {
        // Required empty public constructor
    }

    public static NewsFragment newInstance() {
        NewsFragment fragment = new NewsFragment();
        return fragment;
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_news;
    }

    @Override
    protected void initView() {
        refreshLayout = mRootView.findViewById(R.id.news_refreshLayout);
        recyclerView = mRootView.findViewById(R.id.news_recycleview);
    }

    @Override
    protected void initData() {
        // 设置布局管理器（线性布局管理器，网格布局管理器，瀑布流布局管理器）
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        newsAdapter = new NewsAdapter(getActivity());
//        for (int i = 0; i < 15; i++) {
//            NewsEntity newsEntity = new NewsEntity();
//            newsEntity.setType(i%3+1);
//            datas.add(newsEntity);
//        }
        newsAdapter.setDatas(datas);
        recyclerView.setAdapter(newsAdapter);
        newsAdapter.setOnItemClickListener(new NewsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Serializable obj) {
                //加载h5页面
                NewsEntity newsEntity = (NewsEntity) obj;
                String url = "http://?title=" + newsEntity.getAuthorName();
                Bundle bundle = new Bundle();
                bundle.putString("url", url);
                navigateToWithBundle(WebActivity.class, bundle);
            }
        });

        // 添加下拉刷新监听
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishRefresh(2000); //延时完成刷新
                // 每次刷新将pageNum置为1
                pageNum = 1;
                getNewsList(true); // 刷新
            }
        });
        // 添加上拉加载监听事件
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
//                refreshLayout.finishLoadMore(2000); //延时完成加载动画
                // 下拉加载下一页数据
                pageNum++;
                getNewsList(false); //加载
            }
        });
        getNewsList(true);
    }

    private void getNewsList(boolean isRefresh) {
        String token = getStringFromSp("token");
        if (TextUtils.isEmpty(token)) {
            showToastSync("please login");
            navigateTo(LoginActivity.class);
        }

        HashMap<String, Object> param = new HashMap<>();
        param.put("token", token);
        param.put("page", pageNum);
        param.put("limit", ApiConfig.PAGE_SIZE);
        //发送post请求
        Api.config(ApiConfig.NEWS_LIST, param).getRequest(new TtitCallback() {
            @Override
            public void onSuccess(String res) {
                if (isRefresh) {
                    //刷新
                    refreshLayout.finishRefresh(true);
                } else {
                    //加载更多数据
                    refreshLayout.finishLoadMore(true);
                }
                //接收响应数据
                NewsListResponse response = new Gson().fromJson(res, NewsListResponse.class);
                if(response != null && response.getCode() == 0) {
                    List<NewsEntity> list = response.getPageBean().getList();
                    if(list != null && list.size() > 0) {
                        if (isRefresh) {
                            //刷新，将新请求到的数据，放到界面展示
                            datas = list;
                        } else {
                            //加载，将新请求到的数据添加到旧数据末尾
                            datas.addAll(list);
                        }
                        //让主线程刷新界面
                        mHandler.sendEmptyMessage(0);
                    } else { //发送请求后没有获取到数据
                        if (isRefresh) {
                            showToastSync("暂无加载数据");
                        } else {
                            showToastSync("没有更多数据");
                        }
                    }
                }
            }

            @Override
            public void onFailure(Exception e) {
                if (isRefresh) {
                    refreshLayout.finishRefresh();
                } else {
                    refreshLayout.finishLoadMore();
                }
            }
        });
    }
}