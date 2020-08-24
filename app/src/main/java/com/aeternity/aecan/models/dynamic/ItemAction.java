package com.aeternity.aecan.models.dynamic;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ItemAction implements Serializable {

    private InputModal inputModal;
    private DateModal dateModal;
    private ConfirmModal confirmModal;
    private String actionType;
    private SelectionModal selectionModal;


    private static final String INPUT_MODAL = "open_input_modal";
    private static final String SELECT_MODAL = "open_selection_modal";
    private static final String DATE_MODAL = "open_date_modal";
    private static final String CONFIRM_MODAL = "open_confirm_modal";


    public InputModal getInputModal() {
        return inputModal;
    }

    public void setInputModal(InputModal inputModal) {
        this.inputModal = inputModal;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }


    public DialogFragment getFragment(Fragment parent) {
        switch (actionType) {
            case INPUT_MODAL:
                if (inputModal != null) return inputModal.getFragment(parent);
            case SELECT_MODAL:
                if (selectionModal != null) return selectionModal.getFragment();
            case DATE_MODAL:
                if (dateModal != null) return dateModal.getFragment(parent);
            case CONFIRM_MODAL:
                if (confirmModal != null) return confirmModal.getFragment(parent);
        }
        return null;
    }

    public ItemAction(String actionType, InputModal inputModal, SelectionModal selectionModal) {
        this.actionType = actionType;
        this.inputModal = inputModal;
        this.selectionModal = selectionModal;
    }
}
