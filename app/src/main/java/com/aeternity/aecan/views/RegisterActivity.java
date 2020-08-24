package com.aeternity.aecan.views;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import com.aeternity.aecan.BR;
import com.aeternity.aecan.R;
import com.aeternity.aecan.databinding.ActivityRegisterBinding;
import com.aeternity.aecan.helpers.DialogHelper;
import com.aeternity.aecan.network.requests.RegisterRequest;
import com.aeternity.aecan.util.Constants;
import com.aeternity.aecan.util.EditTextValidationUtils;
import com.aeternity.aecan.viewModels.RegisterActivityViewModel;
import com.aeternity.aecan.views.base.BaseActivity;

public class RegisterActivity extends BaseActivity {

    ActivityRegisterBinding binding;
    private RegisterActivityViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register);
        viewModel = ViewModelProviders.of(this).get(RegisterActivityViewModel.class);
        binding.setVariable(BR.registerActivityViewModel, viewModel);
        observeRegisterViewModel();

    }


    private void observeRegisterViewModel() {
        viewModel.getAction().observe(this, action -> validateFields());
        viewModel.getOnRegisterSuccessData().observe(this, onLoginRepositorySuccess -> {
            hideActivityOverlay();
            String message = onLoginRepositorySuccess.getRegisterResponse().getMessage();
            if (message != null && !message.isEmpty()) {
                DialogHelper.showStaticErrorDialog(this, message, getResources().getString(R.string.ok),
                        new DialogHelper.DialogHelperListener() {
                            @Override
                            public void onBackPressed() {
                                goToLogin(onLoginRepositorySuccess.getRegisterResponse().getEmail());
                            }
                        }).show();
            } else {
                goToLogin(onLoginRepositorySuccess.getRegisterResponse().getEmail());
            }


        });
        viewModel.getOnRegisterFailData().observe(this, onLoginRepositoryFail -> {
            hideActivityOverlay();
            makeMessage(onLoginRepositoryFail.getFormattedMessage());
        });
    }

    private void validateFields() {
        if (isFieldsValid()) {
            showActivityOverlay();

            RegisterRequest request = new RegisterRequest(
                    EditTextValidationUtils.getText(binding.editTextEmail),
                    EditTextValidationUtils.getText(binding.editTextPhone),
                    EditTextValidationUtils.getText(binding.editTextName),
                    EditTextValidationUtils.getText(binding.editTextLastName),
                    EditTextValidationUtils.getText(binding.editTextPassword)
            );

            viewModel.makeRegister(request);
            hideKeyboard();
        }
    }


    private boolean isFieldsValid() {
        if (!EditTextValidationUtils.validateTextNonEmpty(binding.editTextEmail) ||
                !EditTextValidationUtils.checkIfIsEmail(EditTextValidationUtils.getText(binding.editTextEmail))) {
            binding.editTextEmail.setError(getString(R.string.login_email_invalid_field));

            binding.editTextEmail.requestFocus();
            return false;
        }
        if (!EditTextValidationUtils.validateTextNonEmpty(binding.editTextLastName)) {

            binding.editTextLastName.setError(getString(R.string.register_last_name_invalid_field));

            binding.editTextLastName.requestFocus();
            return false;
        }
        if (!EditTextValidationUtils.validateTextNonEmpty(binding.editTextName)) {

            binding.editTextName.setError(getString(R.string.register_name_invalid_field));

            binding.editTextName.requestFocus();
            return false;
        }
        if (!EditTextValidationUtils.validateTextNonEmpty(binding.editTextPhone)) {

            binding.editTextPhone.setError(getString(R.string.register_phone_invalid_field));

            binding.editTextPhone.requestFocus();
            return false;
        }

        if (!EditTextValidationUtils.validateTextNonEmpty(binding.editTextPassword)) {

            binding.editTextPassword.setError(getString(R.string.login_password_invalid_field));

            binding.editTextPassword.requestFocus();
            return false;
        }

        return true;
    }

    private void goToLogin(String email) {
        startActivity(new Intent(this, LoginActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                .putExtra(Constants.INTENT_STRING, email));
        finish();
    }

}
