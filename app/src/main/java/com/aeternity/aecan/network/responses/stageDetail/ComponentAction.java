package com.aeternity.aecan.network.responses.stageDetail;

import com.aeternity.aecan.models.dynamic.ItemAction;

import java.io.Serializable;

public class ComponentAction implements Serializable {
    /**
     * action_type : open_input_modal
     * input_modal : {"title":"Ubicación en el invernadero","prefix":"","suffix":"","input_type":"number","button_text":"Agregar","url_path":"/lot_stages/1/properties/greenhouse_location"}
     */

    public void startAction() {
        switch (actionType) {
            case "open_input_modal":

                if (inputModal != null) {
                }
                break;
        }
    }

    private String actionType;
    private ItemAction inputModal;

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public ItemAction getInputModal() {
        return inputModal;
    }

    public void setInputModal(ItemAction inputModal) {
        this.inputModal = inputModal;
    }
}
