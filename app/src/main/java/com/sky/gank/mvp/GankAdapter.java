package com.sky.gank.mvp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sky.gank.R;
import com.sky.gank.entity.GankEntity;
import com.sky.gank.utils.ImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by tonycheng on 2016/11/24.
 */

public class GankAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<GankEntity> mData;

    public static enum ITEM_TYPE {
        VIEW_TYPE_IAMGE,
        VIEW_TYPE_TEXT,
        VIEW_TYPE_FOOTER
    }

    public GankAdapter(List<GankEntity> data) {
        mData = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        RecyclerView.ViewHolder holder = null;

        if (viewType == ITEM_TYPE.VIEW_TYPE_TEXT.ordinal()) {
            View textItemView = inflater.inflate(R.layout.item_gank_with_text, parent, false);
            holder = new TextViewHolder(textItemView);
        }

        if (viewType == ITEM_TYPE.VIEW_TYPE_IAMGE.ordinal()) {
            View imageItemView = inflater.inflate(R.layout.item_gank_with_image, parent, false);
            holder = new ImageViewHolder(imageItemView);
        }

        if (viewType == ITEM_TYPE.VIEW_TYPE_FOOTER.ordinal()) {
            View footerView = inflater.inflate(R.layout.view_footer, parent, false);
            holder = new FooterViewViewHolder(footerView);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        GankEntity gankEntity = mData.get(position);

        if (holder instanceof ImageViewHolder) {
            if (gankEntity == null) {
                return;
            }

            String desc = gankEntity.getDesc();
            String time = gankEntity.getPublishedAt().split("T")[0];
            String who = gankEntity.getWho();
            String url = gankEntity.getUrl();
            List<String> imageUrl = null;

            if (gankEntity.getImages() != null && gankEntity.getImages().size() > 0) {
                imageUrl = gankEntity.getImages();
            }

            if (imageUrl != null && imageUrl.size() > 0) {

                ((ImageViewHolder) holder).mBanner.setImageLoader(new ImageLoader());
                ((ImageViewHolder) holder).mBanner.setImages(imageUrl);
                ((ImageViewHolder) holder).mBanner.setIndicatorGravity(BannerConfig.CENTER);
                ((ImageViewHolder) holder).mBanner.start();
            }

            if (!TextUtils.isEmpty(desc)) {
                ((ImageViewHolder) holder).mTitleTextView.setText(desc);
            }

            if (gankEntity.getWho() == null) {
                ((ImageViewHolder) holder).mAuthorTextView.setText("");
            } else {
                if (!TextUtils.isEmpty(who)) {
                    ((ImageViewHolder) holder).mAuthorTextView.setText("via " + who);
                }
            }

            if (!TextUtils.isEmpty(time)) {
                ((ImageViewHolder) holder).mTimeTextView.setText(time);
            }
        }

        if (holder instanceof TextViewHolder) {
            if (gankEntity == null) {
                return;
            }

            ((TextViewHolder) holder).mTitle.setText(gankEntity.getDesc());
            ((TextViewHolder) holder).mAuthor.setText(gankEntity.getWho());
            ((TextViewHolder) holder).mTime.setText(gankEntity.getPublishedAt().split("T").toString());
        }
    }

    @Override
    public int getItemCount() {
        int begin = mShowFooter ? 1 : 0;
        if (mData == null) {
            return begin;
        }
        return mData.size() + begin;
    }

    public GankEntity getItem(int position) {
        return mData == null ? null : mData.get(position);
    }

    @Override
    public int getItemViewType(int position) {
//        //最后一个Item设置为FooterView
//        if (!mShowFooter) {
//            return TYPE_FOOTER;
//        }
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        }

        GankEntity gankEntity = mData.get(position);

        if (gankEntity.getImages() != null && gankEntity.getImages().size() > 0) {
            return TYPE_IMAGE;
        } else {
            return TYPE_TEXT;
        }
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        @BindView(R.id.ll_root)
        LinearLayout mLinearLayout;
        //
        @BindView(R.id.banner)
        Banner mBanner;
        @BindView(R.id.title_text_view)
        TextView mTitleTextView;
        @BindView(R.id.author_text_view)
        TextView mAuthorTextView;
        @BindView(R.id.time_text_view)
        TextView mTimeTextView;

        public ImageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mOnItemClickListener.onItemClick(v, getAdapterPosition());
        }
    }

    public class TextViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

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
                mOnItemClickListener.onItemClick(itemView, getAdapterPosition());
            }
        }
    }

    public class FooterViewViewHolder extends RecyclerView.ViewHolder {

        public FooterViewViewHolder(View itemView) {
            super(itemView);
        }
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

    }

    public boolean isShowFooter() {
        return mShowFooter;
    }

    public void isShowFooter(boolean showFooter) {
        mShowFooter = showFooter;
    }
}
