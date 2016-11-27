package com.sky.gank.mvp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sky.gank.R;
import com.sky.gank.entity.GankEntity;
import com.sky.gank.utils.ImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.Transformer;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by tonycheng on 2016/11/24.
 */

public class GankAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_PLAIN_TEXT = 0;
    private static final int TYPE_TEXT_WITH_IAMGES = 1;
    private static final int TYPE_FOOTER = 2;

    private List<GankEntity> mData;
    private boolean isShowFooter;

    public GankAdapter() {
        mData = new ArrayList<>();
    }

    public void setShowFooter(boolean showFooter) {
        if (isShowFooter == showFooter) {
            return;
        }
        isShowFooter = showFooter;
        if (showFooter) {
            notifyItemInserted(getItemCount());
        } else {
            notifyItemRemoved(getItemCount() - 1);
        }
    }

    public boolean isShowFooter() {
        return isShowFooter;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_PLAIN_TEXT:
                View rootTextView = LayoutInflater.from(parent.getContext()).inflate(R.layout
                        .item_gank_with_text, parent, false);
                return new TextViewHolder(rootTextView);
            case TYPE_TEXT_WITH_IAMGES:
                View rootImageView = LayoutInflater.from(parent.getContext()).inflate(R.layout
                        .item_gank_with_image, parent, false);
                return new ImageViewHolder(rootImageView);
            case TYPE_FOOTER:
                View rootFooterView = LayoutInflater.from(parent.getContext()).inflate(R.layout
                        .view_footer, parent, false);
                return new FooterViewHolder(rootFooterView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int type = getItemViewType(position);

        if (type != TYPE_FOOTER) {
            GankEntity gankEntity = mData.get(position);
            TextViewHolder textViewHolder = (TextViewHolder) holder;
            textViewHolder.mTitle.setText(gankEntity.getDesc());
            if (gankEntity.getWho() != null) {
                textViewHolder.mAuthor.setText(String.format("via %s", gankEntity.getWho()));
            } else {
                textViewHolder.mAuthor.setText("");
            }
            textViewHolder.mTime.setText(gankEntity.getPublishedAt().split("T")[0]);

            if (type == TYPE_TEXT_WITH_IAMGES) {
                ImageViewHolder imageViewHolder = (ImageViewHolder) holder;
                imageViewHolder.mBanner.setImages(gankEntity.getImages())
                        .setImageLoader(new ImageLoader())
                        .setBannerAnimation(Transformer.DepthPage)
                        .start();
            }
        }
    }

    @Override
    public int getItemCount() {
        return mData.size() + (isShowFooter ? 1 : 0);
    }

    @Override
    public int getItemViewType(int position) {
        if (isShowFooter && position == getItemCount() - 1) {
            return TYPE_FOOTER;
        }
        GankEntity gankEntity = mData.get(position);
        if (gankEntity.getImages() == null || gankEntity.getImages().size() == 0) {
            return TYPE_PLAIN_TEXT;
        } else {
            return TYPE_TEXT_WITH_IAMGES;
        }
    }

    public void setData(List<GankEntity> data) {
        int positionStart = mData.size();
        mData.addAll(data);
        notifyItemRangeInserted(positionStart, data.size());
    }

    public void clear() {
        int itemCount = mData.size();
        mData.clear();
        notifyItemRangeRemoved(0, itemCount);
    }

    public GankEntity getItem(int position) {
        return mData.get(position);
    }

    static class TextViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.title_text_view)
        TextView mTitle;
        @BindView(R.id.author_text_view)
        TextView mAuthor;
        @BindView(R.id.time_text_view)
        TextView mTime;

        public TextViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(v, getAdapterPosition());
            }
        }
    }

    static class ImageViewHolder extends TextViewHolder {

        @BindView(R.id.image_banner)
        Banner mBanner;

        public ImageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class FooterViewHolder extends RecyclerView.ViewHolder {

        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }

    private static OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}
