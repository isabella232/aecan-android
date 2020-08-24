package com.aeternity.aecan.views.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.aeternity.aecan.R;


@SuppressLint("Registered")
public class BaseActivity extends AppCompatActivity {

    private OverlayHelper overlayHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prepareView();
    }


    protected void showActivityOverlay() {
        runOnUiThread(() -> getOverlayHelper().showActivityOverlay());
    }

    protected void hideActivityOverlay() {
        runOnUiThread(() -> getOverlayHelper().hideActivityOverlay());
    }


    public void prepareView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        } else {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.white));
    }


    public OverlayHelper getOverlayHelper() {
        if (overlayHelper == null)
            overlayHelper = createOverlayHelper();
        return overlayHelper;
    }

    private OverlayHelper createOverlayHelper() {
        return new OverlayHelper(this, getLayoutInflater(), getWindow().getDecorView());
    }

    public void makeMessage(int resourceId) {
        makeMessage(getResources().getString(resourceId));
    }

    public void makeMessage(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = this.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(this);
        }
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


}
