package com.aeternity.aecan.network;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.aeternity.aecan.BuildConfig;
import com.aeternity.aecan.network.responses.LoginResponse;
import com.aeternity.aecan.persistence.SessionPersistence;
import com.aeternity.aecan.util.Constants;
import com.aeternity.aecan.util.DateUtils;
import com.aeternity.aecan.util.gson.AnnotationExclusionStrategy;
import com.aeternity.aecan.views.LoginActivity;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.aeternity.aecan.util.Constants.*;

public class RestClient {
    private static Api API_SERVICE;
    private static Retrofit RETROFIT;
    private static OkHttpClient CLIENT;
    private static OkHttpClient.Builder BUILDER = new OkHttpClient.Builder()
            .readTimeout(20, TimeUnit.SECONDS)
            .connectTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(1, TimeUnit.MINUTES);
    private static final String TAG = RestClient.class.getSimpleName();

    private RestClient() {
    }

    public static void init(final Context context) {
        if (RETROFIT != null) {
            Log.w("RestClient", "Singleton, call only one time!");
            return;
        }

        Gson gson = buildGson();
        CLIENT = buildOkHttp(context);

        RETROFIT = new Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .client(CLIENT)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        API_SERVICE = RETROFIT.create(Api.class);
    }

    public static OkHttpClient getClient() {
        return CLIENT;
    }

    public static OkHttpClient.Builder getBUILDER() {
        return BUILDER;
    }

    public static OkHttpClient buildOkHttp(Context context) {
        addTokensToRequests(getBUILDER());
        refreshIf401(getBUILDER(), context);
        if (BuildConfig.DEBUG)
            addBetterLogsToRequests(getBUILDER());
        return getBUILDER().build();
    }


    public static Gson buildGson() {
        return new GsonBuilder()
                .addSerializationExclusionStrategy(new AnnotationExclusionStrategy())
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
    }

    //Interceptor para añadir tokens de app y de sesion a todas las requests
    private static void addTokensToRequests(OkHttpClient.Builder okhttpBuilder) {
        okhttpBuilder.addInterceptor(chain -> {
            Request original = chain.request();
            Request.Builder requestBuilder = original.newBuilder()
                    .header("Authorization", SessionPersistence.newOrReadFromPaper().getAccesTokenWBearer())
                    .header("platform", "android")
                    .header("Content-Type", JSON_TYPE)
                    .header("time-zone", DateUtils.getUTCOffset())
                    .header("app-mode", BuildConfig.app_mode)
                    .header("Accept", JSON_TYPE)
                    .method(original.method(), original.body());
            return chain.proceed(requestBuilder.build());
        });
    }


    // Interceptor intentar refrescar el token si devuelve 401
    private static void refreshIf401(OkHttpClient.Builder okhttpBuilder, final Context context) {
        okhttpBuilder.addInterceptor(chain -> {
            Response response = chain.proceed(chain.request());
            if (response.code() == 401) {
                if (SessionPersistence.newOrReadFromPaper().checkIfIsSignedIn()) {
                    if (response.request().url().toString().equals(BuildConfig.BASE_URL + LOGIN_URL)) {
                        //logout
                        logout(context);
                    } else {
                        return makeTokenRefreshCall(chain, context);
                    }
                } else
                    goToLoginActivity(context);
            }
            return response;
        });
    }

    public static void logout(Context context) {
        SessionPersistence.deleteSessionFromPersistence();
        goToLoginActivity(context);
    }


    private static void goToLoginActivity(Context context) {
        Intent i = new Intent(context, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(i);
    }

    //Interceptar request para mejores logs en debug
    private static void addBetterLogsToRequests(OkHttpClient.Builder okhttpBuilder) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        okhttpBuilder.addInterceptor(interceptor);
    }

    public static Api getInstance() {
        return API_SERVICE;
    }

    public static Retrofit getRetrofit() {
        return RETROFIT;
    }

    private static Response makeTokenRefreshCall(Interceptor.Chain chain, final Context context) throws IOException {
        /* Fetch refreshed token*/
        Response tokenResponse = fetchToken();
        LoginResponse loginResponse = buildGson().fromJson(tokenResponse.body().string(), LoginResponse.class);

        if (loginResponse.getAccessToken() != null) {
            SessionPersistence.saveSessionInPersistence(loginResponse);
        } else {
            logout(context);
            return null;
        }
        /* Make a new request which is same as the original one, except that its headers now contain a refreshed token */
        Request newRequest = chain.request().newBuilder()
                .header("Content-Type", JSON_TYPE)
                .header("Accept", JSON_TYPE)
                .header("platform", "android")
                .header("Authorization", SessionPersistence.newOrReadFromPaper().getAccesTokenWBearer())
                .header("app-mode", BuildConfig.app_mode)
                .method(chain.request().method(), chain.request().body()).build();
        return chain.proceed(newRequest);
    }

    private static Response fetchToken() {
        OkHttpClient client = RestClient.getClient();
        Gson gson = RestClient.buildGson();
        RequestBody body = RequestBody.create(JSON, gson.toJson(SessionPersistence.getRefreshRequest()));

        Request request = new Request.Builder()
                .url(BuildConfig.BASE_URL + Constants.LOGIN_URL)
                .post(body)
                .build();
        try {
            return client.newCall(request).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    public static void genericPost(String url, String value, Callback callback) {
        OkHttpClient client = RestClient.getClient();
        RequestBody body = RequestBody.create(String.format(JsonPostFormatter, value), JSON);

        Request request = new Request.Builder()
                .url(BuildConfig.BASE_URL+ url)
                .post(body)
                .build();
        try {
            client.newCall(request).enqueue(callback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
