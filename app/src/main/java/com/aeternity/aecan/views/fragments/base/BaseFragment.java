package com.aeternity.aecan.views.fragments.base;

import androidx.fragment.app.Fragment;

import com.aeternity.aecan.views.base.OverlayHelper;

import java.util.Objects;

public class BaseFragment  extends Fragment {
    private OverlayHelper overlayHelper;

    protected void showActivityOverlay() {
        getOverlayHelper().showActivityOverlay();
    }

    protected void hideActivityOverlay() {
        getOverlayHelper().hideActivityOverlay();
    }

    public OverlayHelper getOverlayHelper() {
        if (overlayHelper == null)
            overlayHelper = createOverlayHelper();
        return overlayHelper;
    }

    public OverlayHelper createOverlayHelper() {
        return new OverlayHelper(getContext(), getLayoutInflater(), Objects.requireNonNull(getActivity()).getWindow().getDecorView());
    }

    public BaseFragment() {
    }
}
