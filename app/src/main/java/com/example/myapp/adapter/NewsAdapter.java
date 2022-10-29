package com.example.myapp.adapter;

import android.content.Context;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.myapp.R;
import com.example.myapp.entity.NewsEntity;

import java.io.Serializable;
import java.util.List;

/**
 * @author brynn
 * @since 2022/07/09 16:34
 */
public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<NewsEntity> mDatas;
    private OnItemClickListener mOnItemClickListener;

    public NewsAdapter(Context context) {
        this.mContext = context;
    }

    public NewsAdapter(Context mContext, List<NewsEntity> mDatas) {
        this.mContext = mContext;
        this.mDatas = mDatas;
    }

    public void setDatas(List<NewsEntity> datas) {
        this.mDatas = datas;
    }

    //获取当前item类型，可以在RecycleView中实现多布局，返回的type会作为onCreateViewHolder的参数
    @Override
    public int getItemViewType(int position) {
        int type = mDatas.get(position).getType();
        return type;
    }

    //将viewHolder与布局文件绑定，返回viewHolder
    //根据ViewType（当前item类型）确定加载那个ViewHolder
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 1) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_news_layout_one, parent, false);
            NewsViewHolderOne viewHolderOne = new NewsViewHolderOne(view);
            return viewHolderOne;
        } else if (viewType == 2) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_news_layout_two, parent, false);
            NewsViewHolderTwo viewHolderTwo = new NewsViewHolderTwo(view);
            return viewHolderTwo;
        } else if (viewType == 3) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_news_layout_three, parent, false);
            NewsViewHolderThree viewHolderThree = new NewsViewHolderThree(view);
            return viewHolderThree;
        }
        return null;
    }

    //将数据与viewHolder绑定
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int type = getItemViewType(position);
        NewsEntity newsEntity = mDatas.get(position);
        if (type == 1) {
            NewsViewHolderOne vh = (NewsViewHolderOne) holder;
            vh.title.setText(newsEntity.getNewsTitle());
            vh.author.setText(newsEntity.getAuthorName());
            vh.comment.setText(newsEntity.getCommentCount() + "评论 .");
            vh.time.setText(newsEntity.getReleaseDate());
            vh.newsEntity = newsEntity;
            //加载网络图片
            Glide.with(mContext).load(newsEntity.getHeaderUrl())
                    //Glide加载圆形图片
                    .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                    .into(vh.header);
            Glide.with(mContext).load(newsEntity.getThumbEntities().get(0).getThumbUrl())
                    .into(vh.thumb);
        } else if (type == 2) {
            NewsViewHolderTwo vh = (NewsViewHolderTwo) holder;
            vh.title.setText(newsEntity.getNewsTitle());
            vh.author.setText(newsEntity.getAuthorName());
            vh.comment.setText(newsEntity.getCommentCount() + "评论 .");
            vh.time.setText(newsEntity.getReleaseDate());
            vh.newsEntity = newsEntity;
            //加载网络图片
            Glide.with(mContext).load(newsEntity.getHeaderUrl())
                    //Glide加载圆形图片
                    .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                    .into(vh.header);
            Glide.with(mContext).load(newsEntity.getThumbEntities().get(0).getThumbUrl())
                    .into(vh.pic1);
            Glide.with(mContext).load(newsEntity.getThumbEntities().get(1).getThumbUrl())
                    .into(vh.pic2);
            Glide.with(mContext).load(newsEntity.getThumbEntities().get(2).getThumbUrl())
                    .into(vh.pic3);
        } else if (type == 3) {
            NewsViewHolderThree vh = (NewsViewHolderThree) holder;
            vh.title.setText(newsEntity.getNewsTitle());
            vh.author.setText(newsEntity.getAuthorName());
            vh.comment.setText(newsEntity.getCommentCount() + "评论 .");
            vh.time.setText(newsEntity.getReleaseDate());
            vh.newsEntity = newsEntity;
            //加载网络图片
            Glide.with(mContext).load(newsEntity.getHeaderUrl())
                    //Glide加载圆形图片
                    .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                    .into(vh.header);
            Glide.with(mContext).load(newsEntity.getThumbEntities().get(0).getThumbUrl())
                    .into(vh.thumb);
        }
    }

    @Override
    public int getItemCount() {
        if (mDatas != null && mDatas.size() > 0) {
            return mDatas.size();
        }
        return 0;
    }

    public class NewsViewHolderOne extends RecyclerView.ViewHolder{
        private TextView title;
        private TextView author;
        private TextView comment;
        private TextView time;
        private ImageView header;
        private ImageView thumb;
        private NewsEntity newsEntity;

        public NewsViewHolderOne(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            author = itemView.findViewById(R.id.author);
            comment = itemView.findViewById(R.id.comment);
            time = itemView.findViewById(R.id.time);
            header = itemView.findViewById(R.id.header);
            thumb = itemView.findViewById(R.id.thumb);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(newsEntity);
                }
            });
        }
    }

    public class NewsViewHolderTwo extends RecyclerView.ViewHolder{
        private TextView title;
        private TextView author;
        private TextView comment;
        private TextView time;
        private ImageView header;
        private ImageView pic1, pic2, pic3;
        private NewsEntity newsEntity;
        public NewsViewHolderTwo(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            author = itemView.findViewById(R.id.author);
            comment = itemView.findViewById(R.id.comment);
            time = itemView.findViewById(R.id.time);
            header = itemView.findViewById(R.id.header);
            pic1 = itemView.findViewById(R.id.pic1);
            pic2 = itemView.findViewById(R.id.pic2);
            pic3 = itemView.findViewById(R.id.pic3);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(newsEntity);
                }
            });
        }
    }

    public class NewsViewHolderThree extends RecyclerView.ViewHolder{
        private TextView title;
        private TextView author;
        private TextView comment;
        private TextView time;
        private ImageView header;
        private ImageView thumb;
        private NewsEntity newsEntity;
        public NewsViewHolderThree(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            author = itemView.findViewById(R.id.author);
            comment = itemView.findViewById(R.id.comment);
            time = itemView.findViewById(R.id.time);
            header = itemView.findViewById(R.id.header);
            thumb = itemView.findViewById(R.id.thumb);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(newsEntity);
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Serializable obj);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}
