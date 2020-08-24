package com.aeternity.aecan.models.dynamic;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.aeternity.aecan.views.modal.ConfirmDialogFragment;

import java.util.ArrayList;

public class ConfirmModal {

    private String title;
    private String buttonText;
    private String body;
    private String urlPath;
    private ArrayList<SelectionOptions> options;

    public DialogFragment getFragment(Fragment parent) {
        return ConfirmDialogFragment.newInstance(title, body, urlPath, buttonText, parent);
    }


}
