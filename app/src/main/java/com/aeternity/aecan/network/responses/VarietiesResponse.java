package com.aeternity.aecan.network.responses;

import com.aeternity.aecan.models.Variety;
import com.aeternity.aecan.network.responses.base.BaseResponse;

import java.util.ArrayList;

public class VarietiesResponse extends BaseResponse {

    private ArrayList<Variety> varieties;

    public ArrayList<Variety> getVarieties() {

        return varieties;
    }
}
