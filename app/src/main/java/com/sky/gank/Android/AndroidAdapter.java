package com.sky.gank.Android;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sky.gank.R;
import com.sky.gank.entity.GankEntity;
import com.sky.gank.utils.DensityUtil;
import com.sky.gank.utils.ImageLoader;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by tonycheng on 2016/11/24.
 */

public class AndroidAdapter extends RecyclerView.Adapter<AndroidAdapter.AndroidViewHolder> {

    private Context mContext;
    private List<GankEntity> mData;

    private int mScreenWidth;

    public AndroidAdapter(Context context) {
        mContext = context;
        mScreenWidth = DensityUtil.getWidthInPx(mContext);
    }

    public void setData(List<GankEntity> data) {
        mData = data;
        this.notifyDataSetChanged();
    }

    @Override
    public AndroidViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_android, parent, false);
        return new AndroidViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(final AndroidViewHolder holder, int position) {
        final GankEntity gankEntity = mData.get(position);

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

            ImageLoader.displayAsGif(mContext, imageUrl.get(0), holder.mImageView);

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
            holder.mImageView.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(desc)) {
            holder.mTitleTextView.setText(desc);
        }

        if (gankEntity.getWho() == null) {
            holder.mAuthorTextView.setText("");
        } else {
            if (!TextUtils.isEmpty(who)) {
                holder.mAuthorTextView.setText("via " + who);
            }
        }

        if (!TextUtils.isEmpty(time)) {
            holder.mTimeTextView.setText(time);
        }
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public class AndroidViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        @BindView(R.id.ll_root)
        LinearLayout mLinearLayout;
        @BindView(R.id.android_image_view)
        ImageView mImageView;
        @BindView(R.id.title_text_view)
        TextView mTitleTextView;
        @BindView(R.id.author_text_view)
        TextView mAuthorTextView;
        @BindView(R.id.time_text_view)
        TextView mTimeTextView;

        public AndroidViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mOnItemClickListener.onItemClick(v, getAdapterPosition());
        }
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}
