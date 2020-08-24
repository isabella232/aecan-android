package com.aeternity.aecan.network.responses;

import com.aeternity.aecan.network.responses.base.BaseResponse;

public class AskFinishedStageResponse extends BaseResponse {
    private boolean canFinished;

    public boolean isCanFinished() {
        return canFinished;
    }

    public void setCanFinished(boolean canFinished) {
        this.canFinished = canFinished;
    }
}
