package com.sky.gank.mvp.view;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sky.gank.R;
import com.sky.gank.entity.GankEntity;
import com.sky.gank.mvp.GankAdapter;
import com.sky.gank.mvp.presenter.IGankPresenter;
import com.sky.gank.mvp.presenter.impl.GankPresenterImpl;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class GankListFragment extends Fragment implements IGankView,
        SwipeRefreshLayout.OnRefreshListener, GankAdapter.OnItemClickListener {

    @BindView(R.id.swipe_refresh_widget)
    SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.recycle_view)
    RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private GankAdapter mAdapter;

    private IGankPresenter mPresenter;
    private List<GankEntity> mData = new ArrayList<>();
    private int mCount = 10;
    private int mPage = 1;
    private String mType = GankFragment.GANK_ANDROID;

    public GankListFragment() {
        // Required empty public constructor
    }

    public static GankListFragment newInstance(String type) {
        Bundle args = new Bundle();
        args.putString("type", type);
        GankListFragment fragment = new GankListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new GankPresenterImpl(this);
        mType = getArguments().getString("type");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_gank_list, container, false);
        ButterKnife.bind(this, rootView);

        mRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark);
        mRefreshLayout.setOnRefreshListener(this);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addOnScrollListener(mOnScrollListener);

        mAdapter = new GankAdapter();
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setAdapter(mAdapter);

        onRefresh();
        return rootView;
    }

    @Override
    public void showLoading() {
        mRefreshLayout.setRefreshing(true);
    }

    @Override
    public void addGank(List<GankEntity> gankList) {
        mAdapter.setData(gankList);
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
        if (mAdapter != null) {
            mAdapter.clear();
        }
        mPresenter.loadGankList(mType, mCount, mPage);
    }

    @Override
    public void onItemClick(View view, int position) {
//        if (mData.size() <= 0) {
//            return;
//        }
        GankEntity gankEntity = mAdapter.getItem(position);
        View transitionView = view.findViewById(R.id.gank_item_layout);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                getActivity(), transitionView, "share");
        ActivityCompat.startActivity(getActivity(), GankDetailActivity.newIntent(getActivity(),
                gankEntity),
                options.toBundle());
    }

    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {

        private int lastVisibleItem;

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == RecyclerView.SCROLL_STATE_IDLE
                    && lastVisibleItem + 1 == mAdapter.getItemCount()) {
                //load more
                mAdapter.setShowFooter(true);
                mPresenter.loadGankList(mType, mCount, ++mPage);
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
        }
    };

}
