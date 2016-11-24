package com.sky.gank.Android.view;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.sky.gank.Android.AndroidAdapter;
import com.sky.gank.Android.presenter.IAndroidPresenter;
import com.sky.gank.Android.presenter.impl.AndroidPresenterImpl;
import com.sky.gank.R;
import com.sky.gank.WebActivity;
import com.sky.gank.entity.GankEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class AndroidFragment extends Fragment implements IAndroidView,
        SwipeRefreshLayout.OnRefreshListener, AndroidAdapter.OnItemClickListener {

    @BindView(R.id.swipe_refresh_widget)
    SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.recycle_view)
    RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private AndroidAdapter mAdapter;

    private IAndroidPresenter mPresenter;
    private List<GankEntity> mData;
    private int mCount = 10;
    private int mPage = 1;

    public AndroidFragment() {
        // Required empty public constructor
    }

    public static AndroidFragment newInstance(String type) {
        Bundle args = new Bundle();
        args.putString("type", type);
        AndroidFragment fragment = new AndroidFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new AndroidPresenterImpl(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_android, container, false);
        ButterKnife.bind(this, rootView);

        mRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark);
        mRefreshLayout.setOnRefreshListener(this);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addOnScrollListener(mOnScrollListener);

        mAdapter = new AndroidAdapter(getActivity());
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mAdapter);

        onRefresh();

        return rootView;
    }

    @Override
    public void showLoading() {
        mRefreshLayout.setRefreshing(true);
    }

    @Override
    public void addAndroid(List<GankEntity> androidList) {
        if (mData == null) {
            mData = new ArrayList<>();
        }
        mData.addAll(androidList);
        mAdapter.setData(mData);
    }

    @Override
    public void hideLoading() {
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showLoadFailMsg() {
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        mPresenter.loadAndroidList(mCount, mPage);
    }

    @Override
    public void onItemClick(View view, int position) {
        String url = mData.get(position).getUrl();
        startActivity(WebActivity.newIntent(getActivity(),url));
    }

    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {

        private int lastVisibleItem;

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == RecyclerView.SCROLL_STATE_IDLE
                    && lastVisibleItem + 1 == mAdapter.getItemCount()) {
                //加载更多
                Toast.makeText(getActivity(), "加载更多...", Toast.LENGTH_SHORT).show();
                mPresenter.loadAndroidList(mCount, ++mPage);
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
        }
    };

}
