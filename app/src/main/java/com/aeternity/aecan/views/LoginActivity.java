package com.aeternity.aecan.views;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.aeternity.aecan.BR;
import com.aeternity.aecan.R;
import com.aeternity.aecan.databinding.ActivityLoginBinding;
import com.aeternity.aecan.util.Constants;
import com.aeternity.aecan.util.EditTextValidationUtils;
import com.aeternity.aecan.viewModels.LoginActivityViewModel;
import com.aeternity.aecan.views.base.BaseActivity;

public class LoginActivity extends BaseActivity {

    private ActivityLoginBinding binding;
    private LoginActivityViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        viewModel = ViewModelProviders.of(this).get(LoginActivityViewModel.class);
        binding.setVariable(BR.loginActivityViewModel, viewModel);

        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey(Constants.INTENT_STRING)) {
            binding.editTextEmail.setText(getIntent().getExtras().getString(Constants.INTENT_STRING, ""));
        }

        observeLoginViewModel();
    }

    private void goToForgotPassword() {
        startActivity(new Intent(this, ForgotPasswordActivity.class));
    }

    private void observeLoginViewModel() {
        viewModel.getButtonAction().observe(this, buttonAction -> {
            switch (buttonAction) {
                case LOGIN_PRESSED:
                    validateFields();
                    break;
                case SIGN_UP_PRESSED:
                    goToRegisterActivity();
                    break;
                case FORGOT_PASSWORD_PRESSED:
                    goToForgotPassword();
                    break;
            }
        });
        viewModel.getOnLoginSuccessData().observe(this, onLoginRepositorySuccess -> {
            hideActivityOverlay();
            goToHomeActivity();
        });
        viewModel.getOnLoginFailData().observe(this, onLoginRepositoryFail -> {
            hideActivityOverlay();
            makeMessage(onLoginRepositoryFail.getErrorDescription());

        });
    }

    private void validateFields() {
        if (isFieldsValid()) {
            showActivityOverlay();
            viewModel.makeLogin(
                    EditTextValidationUtils.getEditTextTextSafe(binding.editTextEmail),
                    EditTextValidationUtils.getEditTextTextSafe(binding.editTextPassword));
            hideKeyboard();
        }
    }

    private boolean isFieldsValid() {

        if (!EditTextValidationUtils.validateTextNonEmpty(binding.editTextEmail) ||
                !EditTextValidationUtils.checkIfIsEmail(EditTextValidationUtils.getEditTextTextSafe(binding.editTextEmail))) {

            makeMessage(R.string.login_email_invalid_field);

            binding.editTextEmail.requestFocus();
            binding.editTextEmail.setError(getString(R.string.login_email_invalid_field));
            return false;
        }
        if (!EditTextValidationUtils.validateTextNonEmpty(binding.editTextPassword)) {
            makeMessage(R.string.login_password_invalid_field);
            binding.editTextPassword.requestFocus();
            binding.editTextPassword.setError(getString(R.string.login_password_invalid_field));
            return false;
        }
        return true;
    }

    private void goToRegisterActivity() {
        startActivity(new Intent(this, RegisterActivity.class));
    }

    private void goToHomeActivity() {
        startActivity(new Intent(this, HomeActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
    }

    @Override
    public void onBackPressed() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            binding.logoAECan.setTransitionName("");
        }

        super.onBackPressed();
        System.exit(0);

    }


}

