package com.aeternity.aecan.models.dynamic;

import androidx.fragment.app.Fragment;

import com.aeternity.aecan.views.modal.DatePickerDialogFragment;

public class DateModal {

    private String title;
    private String value;
    private String buttonText;
    private String urlPath;

    public DatePickerDialogFragment getFragment(Fragment parent) {
        return DatePickerDialogFragment.newInstance(title, buttonText, value, urlPath,parent);
    }


    public String getTitle() {
        return title;
    }

    public String getButtonText() {
        return buttonText;
    }

    public String getUrlPath() {
        return urlPath;
    }

    public String getValue() {
        return value;
    }
}
