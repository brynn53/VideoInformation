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

    //????????????
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
        // ???????????????????????????????????????????????????????????????????????????????????????????????????
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);

        videoAdapter = new VideoAdapter(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        videoAdapter.setOnItemChildClickListener(this);
        recyclerView.setAdapter(videoAdapter);

        // recyclerView???item???????????????
        recyclerView.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(@NonNull View view) {

            }
            // ???item????????????
            @Override
            public void onChildViewDetachedFromWindow(@NonNull View view) {
                FrameLayout playerContainer = view.findViewById(R.id.player_container);
                View v = playerContainer.getChildAt(0);
                if (v != null && v == mVideoView && !mVideoView.isFullScreen()) {
                    releaseVideoView();
                }
            }
        });

        // ????????????????????????
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishRefresh(2000); //??????????????????
                // ???????????????pageNum??????1
                pageNum = 1;
                getVideoList(true); // ??????
            }
        });
        // ??????????????????????????????
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
//                refreshLayout.finishLoadMore(2000); //????????????????????????
                // ???????????????????????????
                pageNum++;
                getVideoList(false); //??????
            }
        });

        getVideoList(true);
        /*List<VideoEntity> datas = new ArrayList<>();
        for (int i=0; i<8; i++) {
            VideoEntity videoEntity = new VideoEntity();
            videoEntity.setTitle("??????????????????????????????????????????");
            videoEntity.setAuthor("?????????");
            videoEntity.setCommentCount(i*6);
            videoEntity.setCollectCount(i*4);
            videoEntity.setDzCount(i*2);
            datas.add(videoEntity);
        }*/
    }

    /**
     * ?????????????????????
     */
    protected int mCurPos = -1;
    /**
     * ???????????????????????????????????????????????????????????????
     */
    protected int mLastPos = mCurPos;

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    videoAdapter.setDatas(datas);
                    //????????????
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
                // ??????????????????????????????
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
                            showToastSync("?????????????????????");
                        } else {
                            showToastSync("??????????????????");
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
                //??????VideoViewManager?????????????????????
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
     * ??????onPause????????????super????????????????????????
     * ????????????????????????????????????onPause?????????
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
     * ??????onResume????????????super????????????????????????
     * ????????????????????????????????????onResume?????????
     */
    protected void resume() {
        if (mLastPos == -1)
            return;
        //???????????????????????????
        startPlay(mLastPos);
    }

    @Override
    public void onItemChildClick(int position) {
        startPlay(position);
    }

    /**
     * ????????????
     *
     * @param position ????????????
     */
    protected void startPlay(int position) {
        if (mCurPos == position) return;
        if (mCurPos != -1) {
            releaseVideoView();
        }
        VideoEntity videoEntity = datas.get(position);
        //????????????
//        String proxyUrl = ProxyVideoCacheManager.getProxy(getActivity()).getProxyUrl(videoBean.getUrl());
//        mVideoView.setUrl(proxyUrl);

        mVideoView.setUrl(videoEntity.getPlayurl());
        mTitleView.setTitle(videoEntity.getVtitle());
        View itemView = linearLayoutManager.findViewByPosition(position);
        if (itemView == null) return;
        VideoAdapter.MyViewHolder viewHolder = (VideoAdapter.MyViewHolder) itemView.getTag();
        //?????????????????????PrepareView??????????????????????????????isPrivate???????????????true???
        mController.addControlComponent(viewHolder.mPrepareView, true);
        Utils.removeViewFormParent(mVideoView);
        viewHolder.mPlayerContainer.addView(mVideoView, 0);
        //???????????????VideoView?????????VideoViewManager????????????????????????????????????
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