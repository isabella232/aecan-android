package com.aeternity.aecan.network.responses;

import com.aeternity.aecan.models.Lot;
import com.aeternity.aecan.network.responses.base.BaseResponse;

public class LotResponse extends BaseResponse {
    private Lot lot;

    public Lot getLot() {
        return lot;
    }

    public void setLot(Lot lot) {
        this.lot = lot;
    }
}
