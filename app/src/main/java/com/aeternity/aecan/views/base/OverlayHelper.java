package com.aeternity.aecan.views.base;

import android.content.Context;
import android.content.ContextWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.aeternity.aecan.R;

public class OverlayHelper extends ContextWrapper {
    private ViewGroup overlayContainerView;
    private LayoutInflater layoutInflater;
    private View rootView;

    public OverlayHelper(Context context, LayoutInflater layoutInflater, View rootView) {
        super(context);
        this.layoutInflater = layoutInflater;
        this.rootView = rootView;
    }

    private View createNetworkOverlay() {
        View overlay = layoutInflater.inflate(R.layout.network_indicator_overlay, overlayContainerView, false);
        getOverlayContainerView().addView(overlay, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return overlay;
    }

    public ViewGroup getOverlayContainerView() {
        if (overlayContainerView == null)
            overlayContainerView = (ViewGroup) rootView;
        return overlayContainerView;
    }

    public void showActivityOverlay() {
        View overlay = rootView.findViewById(R.id.lytNetworkIndicatorOverlay);
        if (overlay == null) {
            overlay = createNetworkOverlay();
        }
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        overlay.startAnimation(anim);
        overlay.setVisibility(View.VISIBLE);
    }

    public void hideActivityOverlay() {
        final View overlay = rootView.findViewById(R.id.lytNetworkIndicatorOverlay);
        overlay.setVisibility(View.GONE);
    }
}
