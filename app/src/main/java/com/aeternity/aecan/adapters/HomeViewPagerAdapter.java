package com.aeternity.aecan.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.aeternity.aecan.R;
import com.aeternity.aecan.views.fragments.BlankFragment;

public class HomeViewPagerAdapter extends FragmentStateAdapter {
    private static final int CARD_ITEM_SIZE = 2;
    private Fragment fragmentLeft;
    private Fragment fragmentRight;

    public HomeViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, Fragment fragmentLeft, Fragment fragmentRight) {
        super(fragmentActivity);
        this.fragmentLeft = fragmentLeft;
        this.fragmentRight = fragmentRight;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return fragmentLeft;
            case 1:
                return fragmentRight;
        }
        return new BlankFragment();

    }

    @Override
    public int getItemCount() {
        return CARD_ITEM_SIZE;
    }
}
