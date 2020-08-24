package com.aeternity.aecan;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.aeternity.aecan.databinding.SplashBinding;
import com.aeternity.aecan.network.RestClient;
import com.aeternity.aecan.network.responses.LoginResponse;
import com.aeternity.aecan.persistence.SessionPersistence;
import com.aeternity.aecan.views.HomeActivity;
import com.aeternity.aecan.views.LoginActivity;
import com.aeternity.aecan.views.NewLotDetailActivity;
import com.aeternity.aecan.views.base.BaseActivity;
import com.aeternity.aecan.views.fragments.StageListFragment;

import io.paperdb.Paper;

public class SplashActivity extends BaseActivity {

    SplashBinding binding;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.splash);

        LoginResponse loginResponse = SessionPersistence.newOrReadFromPaper();

        if (!loginResponse.checkIfIsSignedIn()) {
            binding.logoAECan.postDelayed(this::startLogin, 1500);
        } else {
            binding.logoAECan.postDelayed(this::startView, 500);

        }
    }


    private void startLogin() {

        new Handler().postDelayed(super::finish, 500);

        //Intent mLoginIntent = new Intent(this, LoginActivity.class);

        Intent mLoginIntent = new Intent(this, LoginActivity.class);

        //String sharedResource = "imageLogo";
        //ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, binding.logoAECan, sharedResource);
        //startActivity(mLoginIntent, options.toBundle());
        startActivity(mLoginIntent);

    }

    private void startView() {

        new Handler().postDelayed(super::finish, 500);

        Intent mLoginIntent = new Intent(this, HomeActivity.class);

        startActivity(mLoginIntent);

    }

    @Override
    public void onBackPressed() {

    }
}
