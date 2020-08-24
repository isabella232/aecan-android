package com.aeternity.aecan.helpers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class DialogHelper {
    private DialogHelperListener listener;

    public interface DialogHelperListener {
        void onBackPressed();
    }

    public static AlertDialog.Builder showStaticErrorDialog(Context context, String message, String positiveButton, DialogHelperListener listener) {
        return new AlertDialog.Builder(context)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(positiveButton, (dialog, which) -> {
                    if (listener != null)
                        listener.onBackPressed();
                });
    }
}
