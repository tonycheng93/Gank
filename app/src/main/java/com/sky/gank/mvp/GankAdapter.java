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
import com.sky.gank.utils.DensityUtil;
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

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;

    private Context mContext;
    private List<GankEntity> mData;
    private boolean mShowFooter = true;

    private int mScreenWidth;

    public GankAdapter(Context context) {
        mContext = context;
        mScreenWidth = DensityUtil.getWidthInPx(mContext);
    }

    public void setData(List<GankEntity> data) {
        mData = data;
        this.notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View rootView = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_gank, parent, false);
            return new GankViewHolder(rootView);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_footer, null);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            return new FooterViewViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof GankViewHolder) {
            GankEntity gankEntity = mData.get(position);
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

                ((GankViewHolder)holder).mBanner.setImageLoader(new ImageLoader());
                ((GankViewHolder)holder).mBanner.setImages(imageUrl);
                ((GankViewHolder)holder).mBanner.setIndicatorGravity(BannerConfig.CENTER);
                ((GankViewHolder)holder).mBanner.start();

//                Uri uri = Uri.parse(imageUrl.get(0));
//                ((GankViewHolder)holder).mImageView.setImageURI(uri);


//            Glide.with(mContext)
//                    .load(imageUrl.get(0))
//                    .asBitmap()
//                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                    .into(new SimpleTarget<Bitmap>(mScreenWidth / 2, mScreenWidth / 2) {
//                        @Override
//                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                            int width = resource.getWidth();
//                            int height = resource.getHeight();
//                            //计算宽高比
//                            int finalHeight;
//                            if (width > height) {//尽量保持图片处于一个正常的宽高比率
//                                finalHeight = (mScreenWidth / 2) * width / height;
//                            } else {
//                                finalHeight = (mScreenWidth / 2) * height / width;
//                            }
//                            if (gankEntity.getItemHeight() <= 0) {
//                                gankEntity.setItemHeight(finalHeight);
//                                ViewGroup.LayoutParams params = holder.mLinearLayout.getLayoutParams();
//                                params.height = gankEntity.getItemHeight();
//                            }
//                            holder.mImageView.setTag(gankEntity.getUrl());
//                            if (holder.mImageView.getTag().equals(gankEntity.getUrl())) {
//                                holder.mImageView.setImageBitmap(resource);
//                            }
//                        }
//                    });
            } else {
//                ((GankViewHolder) holder).mImageView.setVisibility(View.GONE);
                ((GankViewHolder)holder).mBanner.setVisibility(View.GONE);
                ((GankViewHolder)holder).mLinearLayout.setBackgroundResource(R.color.no_image_item_color);
            }

            if (!TextUtils.isEmpty(desc)) {
                ((GankViewHolder) holder).mTitleTextView.setText(desc);
            }

            if (gankEntity.getWho() == null) {
                ((GankViewHolder) holder).mAuthorTextView.setText("");
            } else {
                if (!TextUtils.isEmpty(who)) {
                    ((GankViewHolder) holder).mAuthorTextView.setText("via " + who);
                }
            }

            if (!TextUtils.isEmpty(time)) {
                ((GankViewHolder) holder).mTimeTextView.setText(time);
            }
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
        //最后一个Item设置为FooterView
        if (!mShowFooter) {
            return TYPE_ITEM;
        }
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    public class GankViewHolder extends RecyclerView.ViewHolder
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

        public GankViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mOnItemClickListener.onItemClick(v, getAdapterPosition());
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
