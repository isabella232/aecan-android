package com.aeternity.aecan.views.base;

import androidx.annotation.LayoutRes;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.View;
import android.view.ViewGroup;

import com.aeternity.aecan.R;
import com.aeternity.aecan.databinding.ToolbarActivityBinding;
import com.aeternity.aecan.helpers.DialogHelper;
import com.aeternity.aecan.network.RestClient;
import com.aeternity.aecan.network.responses.errors.ErrorResponse;

public class ToolbarActivity extends BaseActivity {
    private ToolbarActivityBinding binding;
    private String toolbarTitle = "";

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
    }

    protected <T extends ViewDataBinding> T putContentView(@LayoutRes int resId) {
        binding = DataBindingUtil.setContentView(this, R.layout.toolbar_activity);
        ViewGroup mainContainer = binding.getRoot().findViewById(R.id.activityContainer);

        return DataBindingUtil.inflate(getLayoutInflater(), resId, mainContainer, true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupToolbar(toolbarTitle);
    }

    private void setupToolbar(String name) {
        binding.statusBar.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, getStatusBarHeight()));
        binding.toolbarTitle.setText(name);
        binding.toolbarArrowBack.setOnClickListener(view -> onBackPressed());
        binding.toolbarButtonLogout.setOnClickListener(view -> RestClient.logout(this));
    }

    public void hideBackArrow() {
        binding.toolbarArrowBack.setFocusable(false);
        binding.toolbarArrowBack.setClickable(false);
        binding.toolbarArrowBack.setVisibility(View.INVISIBLE);
    }

    public void setLogoutVisible() {
        binding.toolbarButtonLogout.setVisibility(View.VISIBLE);
    }

    public void setToolbarTitle(String name) {
        toolbarTitle = name;
        binding.toolbarTitle.setText(name);
    }


    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public void putFragment(Fragment fragment, int container) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(container, fragment)
                .commit();
    }


    public void onGenericError(ErrorResponse genericError) {
        hideActivityOverlay();
        String dialogMessage;

        if (genericError.getMessage() != null && !genericError.getMessage().isEmpty()) {
            dialogMessage = genericError.getMessage();
        } else {
            dialogMessage = getString(R.string.error_has_ocurred);
        }

        DialogHelper.showStaticErrorDialog(this, dialogMessage, getString(R.string.ok), new DialogHelper.DialogHelperListener() {
            @Override
            public void onBackPressed() {
                ToolbarActivity.super.onBackPressed();
            }
        });

    }
}
