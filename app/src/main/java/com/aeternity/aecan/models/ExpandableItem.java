package com.aeternity.aecan.models;

import com.aeternity.aecan.R;
import com.aeternity.aecan.models.dynamic.DynamicItem;
import com.aeternity.aecan.models.dynamic.ItemAction;

import java.util.ArrayList;

public class ExpandableItem {

    private String title;
    private ArrayList<String> subTitles;
    private String dates;
    private String state;
    private String buttonText;
    private boolean editable;
    private ItemAction itemAction;
    private static final String NOT_STARTED = "not_started";
    private static final String STARTED = "started";
    private static final String FINISHED = "finished";
    private static final String EXPIRED = "expired";
    private static final String CANCELLED = "cancelled";

    public ExpandableItem(DynamicItem item) {
        this.title = item.getTitle();
        this.subTitles = item.getDescription();
        this.state = item.getState();
        this.itemAction = item.getItemAction();
        this.dates = item.getGreenMessage();
        this.buttonText = item.getButtonText();
        this.editable = item.getEditable();
    }

    public ExpandableItem(String title, ArrayList<String> subTitles, String dates, String state, ItemAction itemAction) {
        this.title = title;
        this.subTitles = subTitles;
        this.dates = dates;
        this.state = state;
        this.itemAction = itemAction;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public String getDates() {
        return dates;
    }

    public boolean isPending() {
        return state.equals(NOT_STARTED);
    }

    public boolean stateSubtitle;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<String> getSubTitles() {
        return subTitles;
    }

    public void setSubTitles(ArrayList<String> subTitles) {
        this.subTitles = subTitles;
    }

    public boolean isStateSubtitle() {
        return stateSubtitle;
    }

    public boolean showButton() {
        return (state.equals(NOT_STARTED) || state.equals(STARTED))&& stateSubtitle && editable && getItemAction()!=null;
    }

    public void setStateSubtitle(boolean stateSubtitle) {
        this.stateSubtitle = stateSubtitle;
    }

    public ItemAction getItemAction() {
        return itemAction;
    }

    public int getPointResource() {
        switch (state) {
            case FINISHED:
                return R.drawable.ic_greencheck;
            case NOT_STARTED:
                return R.drawable.ic_gray_check;
            case STARTED:
                return R.drawable.ic_gray_check;
            case CANCELLED:
                return R.drawable.ic_gray_cancel;
            case EXPIRED:
                return R.drawable.ic_red_cancel;
        }
        return R.drawable.stage_circle_gray;
    }

    public int getTextColorResource() {
        switch (state) {
            case FINISHED:
                return R.color.darkGray;
            case NOT_STARTED:
                return R.color.lighterGray;
            case STARTED:
                return R.color.lighterGray;
            case CANCELLED:
                return R.color.lighterGray;
            case EXPIRED:
                return R.color.red;
        }
        return R.color.darkGray;
    }

    public int getArrowColorResource() {
        switch (state) {
            case EXPIRED:
                return R.color.red;
        }
        return R.color.colorPrimaryDark;
    }

    public int getRowDirection() {
        return isStateSubtitle() ? R.drawable.ic_abovearrow : R.drawable.ic_underarrow;
    }

    public String getButtonText() {
        return buttonText;
    }
}
