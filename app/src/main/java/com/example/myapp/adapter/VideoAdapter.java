package com.example.myapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.dueeeke.videocontroller.component.PrepareView;
import com.example.myapp.R;
import com.example.myapp.entity.VideoEntity;
import com.example.myapp.listener.OnItemChildClickListener;
import com.example.myapp.listener.OnItemClickListener;

import java.util.List;

/**
 * @author brynn
 * @since 2022/07/02 13:53
 */
public class VideoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private OnItemChildClickListener mOnItemChildClickListener;
    private OnItemClickListener mOnItemClickListener;

    public List<VideoEntity> getmDatas() {
        return mDatas;
    }

    public void setDatas(List<VideoEntity> mDatas) {
        this.mDatas = mDatas;
    }

    private List<VideoEntity> mDatas;

    public VideoAdapter(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_video_layout, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        // 将viewHolder和数据绑定
        MyViewHolder viewHolder = (MyViewHolder) holder;
        VideoEntity videoEntity = mDatas.get(position);
        viewHolder.tvTitle.setText(videoEntity.getVtitle());
        viewHolder.tvAuthor.setText(videoEntity.getAuthor());
        viewHolder.tvComment.setText(String.valueOf(videoEntity.getCommentNum()));
        viewHolder.tvCollect.setText(String.valueOf(videoEntity.getCollectNum()));
        viewHolder.tvDz.setText(String.valueOf(videoEntity.getLikeNum()));

        // 加载网络图片
        Glide.with(mContext)
                .load(videoEntity.getHeadurl())
                //Glide加载圆形图片
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(viewHolder.imgHeader);
//        Glide.with(mContext).load(videoEntity.getCoverurl()).into(viewHolder.imgCover);
        Glide.with(mContext).load(videoEntity.getCoverurl()).into(viewHolder.mThumb);

        viewHolder.mPosition = position;
    }

    @Override
    public int getItemCount() {
        if (mDatas != null && mDatas.size() > 0) {
            return mDatas.size();
        }
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvTitle;
        private TextView tvAuthor;
        private TextView tvComment;
        private TextView tvDz;
        private TextView tvCollect;
        private ImageView imgHeader;
        // 缩略图
        public ImageView mThumb;
        public PrepareView mPrepareView;
        public FrameLayout mPlayerContainer;
        public int mPosition;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.title);
            tvAuthor = itemView.findViewById(R.id.author);
            tvComment = itemView.findViewById(R.id.comment);
            tvDz = itemView.findViewById(R.id.dianzan);
            tvCollect = itemView.findViewById(R.id.collect);
            imgHeader = itemView.findViewById(R.id.img_header);

            mPlayerContainer = itemView.findViewById(R.id.player_container);
            mPrepareView = itemView.findViewById(R.id.prepare_view);
            mThumb = mPrepareView.findViewById(R.id.thumb);
            if (mOnItemChildClickListener != null) {
                mPlayerContainer.setOnClickListener(this);
            }
            if (mOnItemClickListener != null) {
                itemView.setOnClickListener(this);
            }
            itemView.setTag(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.player_container) {
                if (mOnItemChildClickListener != null) {
                    mOnItemChildClickListener.onItemChildClick(mPosition);
                }
            } else {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(mPosition);
                }
            }
        }
    }

    public void setOnItemChildClickListener(OnItemChildClickListener onItemChildClickListener) {
        mOnItemChildClickListener = onItemChildClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }
}
