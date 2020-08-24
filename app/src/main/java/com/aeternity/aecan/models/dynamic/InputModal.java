package com.aeternity.aecan.models.dynamic;

import android.text.InputType;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.aeternity.aecan.views.modal.DatePickerDialogFragment;
import com.aeternity.aecan.views.modal.InputDialogFragment;

public class InputModal {
    /**
     * title : Ubicación en el invernadero
     * prefix :
     * suffix :
     * input_type : number
     * button_text : Agregar
     * url_path : /lot_stages/1/properties/greenhouse_location
     */
    private String title;
    private String prefix;
    private String suffix;
    private String inputType;
    private String value;
    private String buttonText;
    private String urlPath;

    public DialogFragment getFragment(Fragment parent) {

        if (inputType.equals("date")) {
            return DatePickerDialogFragment.newInstance(title, buttonText, value, urlPath, parent);
        }
        return InputDialogFragment.newInstance(title, prefix, suffix, buttonText, value, getInputTypeID(), urlPath, parent);
    }


    public int getInputTypeID() {
        switch (inputType) {
            case "number":
                return InputType.TYPE_CLASS_NUMBER;
            case "text":
                return InputType.TYPE_CLASS_TEXT;
        }
        return InputType.TYPE_CLASS_TEXT;
    }


    public String getTitle() {
        return title;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public String getInputType() {
        return inputType;
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
