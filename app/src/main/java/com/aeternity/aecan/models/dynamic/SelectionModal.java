package com.aeternity.aecan.models.dynamic;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.aeternity.aecan.views.modal.SelectDialogFragment;

import java.util.ArrayList;

public class SelectionModal {

    private String title;
    private String buttonText;
    private String value;
    private String urlPath;
    private ArrayList<SelectionOptions> options;

    public DialogFragment getFragment() {
        return SelectDialogFragment.newInstance(title, buttonText, getSelectedValue(), options, urlPath);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }



    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getButtonText() {
        return buttonText;
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }

    public String getUrlPath() {
        return urlPath;
    }

    public void setUrlPath(String urlPath) {
        this.urlPath = urlPath;
    }

    public ArrayList<SelectionOptions> getOptions() {
        return options;
    }

    public void setOptions(ArrayList<SelectionOptions> options) {
        this.options = options;
    }

    private ArrayList<Integer> getSelectedValue() {
        ArrayList<Integer> selected = new ArrayList<>();
        for (SelectionOptions option : options) {
            if (option.getId().equals(value)) {
                selected.add(options.indexOf(option));
            }
        }
        return selected;
    }

    public SelectionModal(String title, String button_text, String value, String url_path, ArrayList<SelectionOptions> options) {
        this.title = title;
        this.buttonText = button_text;
        this.value = value;
        this.urlPath = url_path;
        this.options = options;
    }
}
