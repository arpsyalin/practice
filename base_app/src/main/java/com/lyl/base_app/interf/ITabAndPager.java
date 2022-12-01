package com.lyl.base_app.interf;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

/**
 * * @Description
 * * @Author 刘亚林
 * * @CreateDate 2021/8/26
 * * @Version 1.0
 * * @Remark TODO
 **/
public interface ITabAndPager extends TabLayout.OnTabSelectedListener, ViewPager.OnPageChangeListener {
    int getTabLayoutId();

    String[] getTitleArr();

    int getViewPagerId();

    String addTabTitleByTabTitleArr(int i, String tabTitle);

    Fragment addFragmentByTabTitleArr(int i, String tabTitle);

}
