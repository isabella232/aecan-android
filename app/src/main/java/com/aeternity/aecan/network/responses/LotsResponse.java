package com.aeternity.aecan.network.responses;

import com.aeternity.aecan.models.Lot;
import com.aeternity.aecan.network.responses.base.BaseResponse;

import java.util.ArrayList;

public class LotsResponse extends BaseResponse {
    private ArrayList<Lot> lots;

    public ArrayList<Lot> getLots() {
        return lots;
    }
}
