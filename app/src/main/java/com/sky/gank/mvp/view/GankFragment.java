package com.sky.gank.mvp.view;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sky.gank.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class GankFragment extends Fragment {

    public static final int GANK_ANDROID = 0;
    public static final int GANK_IOS = 1;
    public static final int GANK_FRONT_END = 2;

    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.viewpager)
    ViewPager mViewPager;

    public GankFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_gank, null);
        ButterKnife.bind(this, rootView);

        mViewPager.setOffscreenPageLimit(3);
        setupViewPager(mViewPager);

        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.category_android));
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.category_ios));
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.category_front_end));

        mTabLayout.setupWithViewPager(mViewPager);
        return rootView;
    }

    private void setupViewPager(ViewPager viewPager) {
        PagerAdapter adapter = new PagerAdapter(getChildFragmentManager());
        adapter.addFragment(GankListFragment.newInstance(GANK_ANDROID), getString(R.string.category_android));
        adapter.addFragment(GankListFragment.newInstance(GANK_IOS), getString(R.string.category_ios));
        adapter.addFragment(GankListFragment.newInstance(GANK_FRONT_END), getString(R.string.category_front_end));
        mViewPager.setAdapter(adapter);
    }

    public static class PagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }


        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }

}
