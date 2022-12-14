package com.example.myapp.fragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;

import com.dueeeke.videocontroller.StandardVideoController;
import com.dueeeke.videocontroller.component.CompleteView;
import com.dueeeke.videocontroller.component.ErrorView;
import com.dueeeke.videocontroller.component.GestureView;
import com.dueeeke.videocontroller.component.TitleView;
import com.dueeeke.videocontroller.component.VodControlView;
import com.dueeeke.videoplayer.player.VideoView;
import com.example.myapp.R;
import com.example.myapp.activity.LoginActivity;
import com.example.myapp.adapter.VideoAdapter;
import com.example.myapp.api.Api;
import com.example.myapp.api.ApiConfig;
import com.example.myapp.api.TtitCallback;
import com.example.myapp.entity.VideoEntity;
import com.example.myapp.entity.VideoListResponse;
import com.example.myapp.listener.OnItemChildClickListener;
import com.example.myapp.util.Tag;
import com.example.myapp.util.Utils;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VideoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VideoFragment extends BaseFragment implements OnItemChildClickListener {

    private int categoryId;
    private RecyclerView recyclerView;
    private SmartRefreshLayout refreshLayout;
    private VideoAdapter videoAdapter;
    private List<VideoEntity> datas = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;

    protected VideoView mVideoView;
    protected StandardVideoController mController;
    protected ErrorView mErrorView;
    protected CompleteView mCompleteView;
    protected TitleView mTitleView;

    //分页属性
    private int pageNum = 1;

    public VideoFragment() {
        // Required empty public constructor
    }

    public static VideoFragment newInstance(int categoryId) {
        VideoFragment fragment = new VideoFragment();
        fragment.categoryId = categoryId;
        return fragment;
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_vedio;
    }

    @Override
    protected void initView() {
        initVideoView();
        recyclerView = mRootView.findViewById(R.id.recycleview);
        refreshLayout = mRootView.findViewById(R.id.refreshLayout);
    }

    @Override
    protected void initData() {
        // 设置布局管理器（线性布局管理器，网格布局管理器，瀑布流布局管理器）
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);

        videoAdapter = new VideoAdapter(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        videoAdapter.setOnItemChildClickListener(this);
        recyclerView.setAdapter(videoAdapter);

        // recyclerView中item状态变化时
        recyclerView.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(@NonNull View view) {

            }
            // 当item脱离窗口
            @Override
            public void onChildViewDetachedFromWindow(@NonNull View view) {
                FrameLayout playerContainer = view.findViewById(R.id.player_container);
                View v = playerContainer.getChildAt(0);
                if (v != null && v == mVideoView && !mVideoView.isFullScreen()) {
                    releaseVideoView();
                }
            }
        });

        // 添加下拉刷新监听
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishRefresh(2000); //延时完成刷新
                // 每次刷新将pageNum置为1
                pageNum = 1;
                getVideoList(true); // 刷新
            }
        });
        // 添加上拉加载监听事件
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
//                refreshLayout.finishLoadMore(2000); //延时完成加载动画
                // 下拉加载下一页数据
                pageNum++;
                getVideoList(false); //加载
            }
        });

        getVideoList(true);
        /*List<VideoEntity> datas = new ArrayList<>();
        for (int i=0; i<8; i++) {
            VideoEntity videoEntity = new VideoEntity();
            videoEntity.setTitle("韭菜盒子新做法，不发面不烫面");
            videoEntity.setAuthor("大胃王");
            videoEntity.setCommentCount(i*6);
            videoEntity.setCollectCount(i*4);
            videoEntity.setDzCount(i*2);
            datas.add(videoEntity);
        }*/
    }

    /**
     * 当前播放的位置
     */
    protected int mCurPos = -1;
    /**
     * 上次播放的位置，用于页面切回来之后恢复播放
     */
    protected int mLastPos = mCurPos;

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    videoAdapter.setDatas(datas);
                    //刷新界面
                    videoAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    private void getVideoList(boolean isRefresh) {
        String token = getStringFromSp("token");
        if (TextUtils.isEmpty(token)) {
            showToastSync("please login");
            navigateTo(LoginActivity.class);
            return;
        }
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", token);
        param.put("page", pageNum);
        param.put("limit", ApiConfig.PAGE_SIZE);
        param.put("categoryId", categoryId);
        Api.config(ApiConfig.VIDEO_LIST_BY_CATEGORY, param).getRequest(new TtitCallback() {
            @Override
            public void onSuccess(String res) {
                if (isRefresh) {
                    refreshLayout.finishRefresh(true);
                } else {
                    refreshLayout.finishLoadMore(true);
                }
                // 从接口获取响应的数据
                VideoListResponse response = new Gson().fromJson(res, VideoListResponse.class);
                if (response != null && response.getCode() == 0) {
                    List<VideoEntity> list = response.getPage().getList();
                    if (list != null && list.size() > 0) {
                        if (isRefresh) {
                            datas = list;
                        } else {
                            datas.addAll(list);
                        }
                        mHandler.sendEmptyMessage(0);
                    } else {
                        if (isRefresh) {
                            showToastSync("暂时加载无数据");
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

    protected void initVideoView() {
        mVideoView = new VideoView(getActivity());
        mVideoView.setOnStateChangeListener(new com.dueeeke.videoplayer.player.VideoView.SimpleOnStateChangeListener() {
            @Override
            public void onPlayStateChanged(int playState) {
                //监听VideoViewManager释放，重置状态
                if (playState == com.dueeeke.videoplayer.player.VideoView.STATE_IDLE) {
                    Utils.removeViewFormParent(mVideoView);
                    mLastPos = mCurPos;
                    mCurPos = -1;
                }
            }
        });
        mController = new StandardVideoController(getActivity());
        mErrorView = new ErrorView(getActivity());
        mController.addControlComponent(mErrorView);
        mCompleteView = new CompleteView(getActivity());
        mController.addControlComponent(mCompleteView);
        mTitleView = new TitleView(getActivity());
        mController.addControlComponent(mTitleView);
        mController.addControlComponent(new VodControlView(getActivity()));
        mController.addControlComponent(new GestureView(getActivity()));
        mController.setEnableOrientation(true);
        mVideoView.setVideoController(mController);
    }

    @Override
    public void onPause() {
        super.onPause();
        pause();
    }

    /**
     * 由于onPause必须调用super。故增加此方法，
     * 子类将会重写此方法，改变onPause的逻辑
     */
    protected void pause() {
        releaseVideoView();
    }

    @Override
    public void onResume() {
        super.onResume();
        resume();
    }

    /**
     * 由于onResume必须调用super。故增加此方法，
     * 子类将会重写此方法，改变onResume的逻辑
     */
    protected void resume() {
        if (mLastPos == -1)
            return;
        //恢复上次播放的位置
        startPlay(mLastPos);
    }

    @Override
    public void onItemChildClick(int position) {
        startPlay(position);
    }

    /**
     * 开始播放
     *
     * @param position 列表位置
     */
    protected void startPlay(int position) {
        if (mCurPos == position) return;
        if (mCurPos != -1) {
            releaseVideoView();
        }
        VideoEntity videoEntity = datas.get(position);
        //边播边存
//        String proxyUrl = ProxyVideoCacheManager.getProxy(getActivity()).getProxyUrl(videoBean.getUrl());
//        mVideoView.setUrl(proxyUrl);

        mVideoView.setUrl(videoEntity.getPlayurl());
        mTitleView.setTitle(videoEntity.getVtitle());
        View itemView = linearLayoutManager.findViewByPosition(position);
        if (itemView == null) return;
        VideoAdapter.MyViewHolder viewHolder = (VideoAdapter.MyViewHolder) itemView.getTag();
        //把列表中预置的PrepareView添加到控制器中，注意isPrivate此处只能为true。
        mController.addControlComponent(viewHolder.mPrepareView, true);
        Utils.removeViewFormParent(mVideoView);
        viewHolder.mPlayerContainer.addView(mVideoView, 0);
        //播放之前将VideoView添加到VideoViewManager以便在别的页面也能操作它
        getVideoViewManager().add(mVideoView, Tag.LIST);
        mVideoView.start();
        mCurPos = position;

    }

    private void releaseVideoView() {
        mVideoView.release();
        if (mVideoView.isFullScreen()) {
            mVideoView.stopFullScreen();
        }
        if (getActivity().getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        mCurPos = -1;
    }
}