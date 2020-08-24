package com.aeternity.aecan.views.modal;

import android.graphics.Point;
import android.view.Display;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import androidx.fragment.app.DialogFragment;

public class CustomDialogFragment extends DialogFragment {

    private boolean fullscreen = false;

    void setFullscreen(boolean fullscreen) {
        this.fullscreen = fullscreen;
    }

    public void onResume() {
        // Store access variables for window and blank point
        if (getDialog() != null && getDialog().getWindow() != null) {
            Window window = getDialog().getWindow();
            Point size = new Point();
            // Store dimensions of the screen in `size`
            Display display = window.getWindowManager().getDefaultDisplay();
            display.getSize(size);
            // Set the width of the dialog proportional to 90% of the screen width
            if(fullscreen){
                window.setLayout((int) (size.x), WindowManager.LayoutParams.MATCH_PARENT);

            }else {
                window.setLayout((int) (size.x * 0.90), WindowManager.LayoutParams.WRAP_CONTENT);

            }
            window.setGravity(Gravity.CENTER);
            // Call super onResume after sizing
        }
        super.onResume();

    }

}
