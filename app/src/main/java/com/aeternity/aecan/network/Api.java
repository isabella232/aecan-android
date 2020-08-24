package com.aeternity.aecan.network;


import com.aeternity.aecan.network.requests.AssignBeaconsIdsRequest;
import com.aeternity.aecan.network.requests.CreateLotRequest;
import com.aeternity.aecan.network.requests.LoginRequest;
import com.aeternity.aecan.network.requests.RegisterRequest;
import com.aeternity.aecan.network.responses.AskFinishedStageResponse;
import com.aeternity.aecan.network.responses.BeaconsResponse;
import com.aeternity.aecan.network.responses.LotResponse;
import com.aeternity.aecan.network.responses.LoginResponse;
import com.aeternity.aecan.network.responses.LotsResponse;
import com.aeternity.aecan.network.responses.RegisterResponse;
import com.aeternity.aecan.network.responses.StageDetailResponse;
import com.aeternity.aecan.network.responses.VarietiesResponse;
import com.aeternity.aecan.network.requests.RecoveryPasswordRequest;
import com.aeternity.aecan.network.responses.base.BaseResponse;
import com.aeternity.aecan.util.Constants;

import java.util.ArrayList;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * API que contiene los endpoints con los que se comunica la app
 */
public interface Api {

    @POST(Constants.LOGIN_URL)
    Single<LoginResponse> makeLogin(@Body LoginRequest loginRequest);

    @POST(Constants.REGISTER_URL)
    Single<RegisterResponse> makeRegister(@Body RegisterRequest RegisterRequest);

    @GET(Constants.GET_VARIETIES)
    Single<VarietiesResponse> getVarieties();

    @GET(Constants.GET_BEACONS_URL)
    Single<BeaconsResponse> getBeacons();

    @POST(Constants.RECOVERY_PASSWORD_URL)
    Single<BaseResponse> recoveryPassword(@Body RecoveryPasswordRequest recoveryPasswordRequest);

    @POST(Constants.CREATE_LOT)
    Single<LotResponse> createLot(@Body CreateLotRequest createLotRequest);

    @GET(Constants.GET_LOT_INFORMATION)
    Single<LotResponse> getLotInformation();

    @GET(Constants.GET_INITIALIZED_LOTS_URL)
    Single<LotsResponse> getInitializedLots();

    @GET(Constants.GET_FINISHED_LOTS_URL)
    Single<LotsResponse> getFinishedLots();

    @GET(Constants.GET_ASSIGNED_LOTS_URL)
    Single<LotsResponse> getAssignedLots();

    @GET(Constants.SEARCH_LOT_URL)
    Single<LotResponse> searchLot(@Query("identifier") String identifier);

    @GET(Constants.GET_LOT_DETAIL)
    Single<LotResponse> getLotDetail(
            @Path("id") int  id
    );

    @GET(Constants.GET_STAGE_DETAIL)
    Single<StageDetailResponse> getStageDetail(
            @Path("id") String  id
    );

    @POST(Constants.FINISH_STAGE)
    Single<BaseResponse> finishStage(
            @Path("id") String id
    );


    @POST(Constants.ASSIGN_BEACONS)
    Single<BaseResponse> assignBeacons(
            @Path("id") String id,
            @Body AssignBeaconsIdsRequest beaconIds
    );


    @GET(Constants.CHECK_FINISH_STAGE)
    Single<AskFinishedStageResponse> checkFinishStage(
            @Path("id") String id
    );

}

