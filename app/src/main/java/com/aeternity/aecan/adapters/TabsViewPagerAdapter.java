package com.aeternity.aecan.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.aeternity.aecan.views.fragments.components.TabFragment;

import java.util.ArrayList;

public class TabsViewPagerAdapter extends FragmentStateAdapter {
    private ArrayList<TabFragment> tabs;

    public TabsViewPagerAdapter(ArrayList<TabFragment> tabs, @NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
        this.tabs = tabs;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return tabs.get(position);
    }

    @Override
    public int getItemCount() {
        return tabs.size();
    }
}