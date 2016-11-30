package com.sky.gank.mvp.view;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
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
import com.sky.gank.utils.DividerItemDecoration;

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

    //RecyclerView settings
    private LinearLayoutManager mLayoutManager;
    private List<GankEntity> mData;
    private GankAdapter mAdapter;

    //Gank data
    private IGankPresenter mPresenter;
    private int mCount = 10;
    private int mPage = 1;
    private String mType;

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

        // init RefreshLayout
        mRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark);
        mRefreshLayout.setOnRefreshListener(this);

        //init RecyclerView
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addOnScrollListener(mOnScrollListener);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));


        //init Adapter
        mAdapter = new GankAdapter(getActivity());
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
    public void addGank(List<GankEntity> gankList) {
        mAdapter.setShowFooter(true);
        if (mData == null) {
            mData = new ArrayList<>();
        }
        mData.addAll(gankList);
        if (mPage == 1) {
            mAdapter.setData(mData);
        } else {
            if (gankList == null || gankList.size() == 0) {
                mAdapter.setShowFooter(false);
            }
            mAdapter.notifyDataSetChanged();
        }
        mPage += 1;
    }

    @Override
    public void hideLoading() {
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showLoadFailMsg() {
        mRefreshLayout.setRefreshing(false);
        View view = mRecyclerView.getRootView();
        Snackbar.make(view, R.string.load_data_error, Snackbar.LENGTH_SHORT).show();
    }


    @Override
    public void onRefresh() {
        mPage = 1;
        if (mData != null) {
            mData.clear();
        }
        mPresenter.loadGankList(mType, mCount, mPage);
    }

    @Override
    public void onItemClick(View view, int position) {
        if (mData.size() <= 0) {
            return;
        }
        View transitionView = view.findViewById(R.id.gank_item_layout);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                getActivity(), transitionView, "share");
        ActivityCompat.startActivity(getActivity(), GankDetailActivity.newIntent(getActivity(),
                mData.get(position).getUrl(), mData.get(position).getDesc()),
                options.toBundle());
    }

    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {

        private int lastVisibleItem;

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == RecyclerView.SCROLL_STATE_IDLE
                    && lastVisibleItem + 1 == mAdapter.getItemCount()
                    && mAdapter.isShowFooter()) {
                //load more
                mPresenter.loadGankList(mType, mCount, mPage);
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
        }
    };

}
