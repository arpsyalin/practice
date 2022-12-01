package com.lyl.baseutil.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

/**
 * * @Description
 * * @Author 刘亚林
 * * @CreateDate 2021/6/21
 * * @Version 1.0
 * * @Remark TODO
 **/
public class BaseViewPagerTabFragmentAdapter extends FragmentPagerAdapter {
    final List<Fragment> fragments;
    final List<String> tabTitles;

    public BaseViewPagerTabFragmentAdapter(FragmentManager fm, List<Fragment> fragments, List<String> tabTitles) {
        super(fm);
        this.fragments = fragments;
        this.tabTitles = tabTitles;
    }

    /**
     * Return the Fragment associated with a specified position.
     *
     * @param position
     */
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    /**
     * Return the number of views available.
     */
    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles.get(position);
    }
}
