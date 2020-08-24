package com.aeternity.aecan.util;

import com.aeternity.aecan.BuildConfig;

import okhttp3.MediaType;

public class Constants {
    public static final String LOGIN_URL = "users/token";
    public static final String REGISTER_URL = "users";
    public static final String GET_VARIETIES = "varieties";
    public static final String INTENT_STRING = "string-data";
    public static final String CREATE_LOT = "lots";
    public static final String GET_LOT_INFORMATION = "lots/default_info";
    public static final String RECOVERY_PASSWORD_URL = "recover_user_password_instructions";
    public static final String GET_BEACONS_URL = "beacons";
    public static final String GET_INITIALIZED_LOTS_URL = "lots";
    public static final String SEARCH_LOT_URL = "lots/search_by_identifier";
    public static final String GET_FINISHED_LOTS_URL = "lots/index_finished";
    public static final String GET_ASSIGNED_LOTS_URL = "lots/index_assigned";
    public static final String GET_LOT_DETAIL = "lots/{id}";
    public static final String GET_STAGE_DETAIL = "lot_stages/{id}";
    public static final String FINISH_STAGE = "lot_stages/{id}/finish";
    public static final String ASSIGN_BEACONS = "lot_stages/{id}/assign_beacons";
    public static final String CHECK_FINISH_STAGE = "lot_stages/{id}/ask_finish";
    public static final String JSON_TYPE = "application/json; charset=utf-8";
    public static final MediaType JSON = MediaType.parse(JSON_TYPE);
    public static final String STAGE_KEY = "stage";
    public static final String LOT_KEY = "lot";
    public static final String END_DATE_KEY = "end_date";
    public static final String IS_FINISHED_KEY = "is_finished";
    public static final String ADMITS_BEACONS_KEY = "admits_beacons";

    private static final String OPERATOR = "operator";

    @SuppressWarnings("ConstantConditions")
    public static boolean isOperator() {
        return BuildConfig.app_mode.equals(OPERATOR);
    }

    public static String JsonPostFormatter = "{\"value\": \"%s\"}";

    public enum GrantType {
        REFRESH_TOKEN("refresh_token"),
        PASSWORD("password");

        public final String text;

        public String getText() {
            return text;
        }

        GrantType(String text) {
            this.text = text;
        }
    }

}

