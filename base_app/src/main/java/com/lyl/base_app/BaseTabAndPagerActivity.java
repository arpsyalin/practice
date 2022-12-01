package com.lyl.base_app;


import com.google.android.material.tabs.TabLayout;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.lyl.base_app.interf.ITabAndPager;
import com.lyl.baseutil.adapter.BaseViewPagerTabFragmentAdapter;
import com.lyl.baseutil.utils.TabViewUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * * @Description
 * * @Author 刘亚林
 * * @CreateDate 2021/8/25
 * * @Version 1.0
 * * @Remark TODO TabLayout+Viewpager的组合
 **/
public abstract class BaseTabAndPagerActivity extends BaseActivity implements ITabAndPager {
    protected List<Fragment> mFragments = new ArrayList<>();
    protected List<String> mTabTitles;
    protected TabLayout mTabLayout;
    protected ViewPager mViewPager;
    float mDefaultStrokeWidth = 0f;
    float mSelectStrokeWidth = 1f;

    protected void initView() {
        mTabLayout = findViewById(getTabLayoutId());
        mViewPager = findViewById(getViewPagerId());
        initTabFragmentData();
    }

    private void setTabFragmentData() {
        BaseViewPagerTabFragmentAdapter pagerAdapter =
                new BaseViewPagerTabFragmentAdapter(getSupportFragmentManager(), mFragments, mTabTitles);
        mViewPager.setAdapter(pagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.addOnPageChangeListener(this);
        mTabLayout.addOnTabSelectedListener(this);
        TabViewUtils.changeTabText(mTabLayout.getTabAt(0), mSelectStrokeWidth);
    }


    private void initTabFragmentData() {
        if (mFragments.size() > 0) return;
        if (mTabLayout != null) {
            String[] tabTitles = getTitleArr();
            this.mTabTitles = new ArrayList<>(tabTitles.length);
            int tabSize = tabTitles.length;
            for (int i = 0; i < tabSize; i++) {
                this.mTabTitles.add(addTabTitleByTabTitleArr(i, tabTitles[i]));
                mFragments.add(addFragmentByTabTitleArr(i, tabTitles[i]));
            }
        } else {
            mFragments.addAll(addFragments());
        }
        setTabFragmentData();
    }

    //没有mTabLayout请实现这个方法
    protected List<? extends Fragment> addFragments() {
        return null;
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {

    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        TabViewUtils.changeTabText(tab, mSelectStrokeWidth);
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        TabViewUtils.changeTabText(tab, mDefaultStrokeWidth);
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
