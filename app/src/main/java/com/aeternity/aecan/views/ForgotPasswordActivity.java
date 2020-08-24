package com.aeternity.aecan.views;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.aeternity.aecan.BR;
import com.aeternity.aecan.R;
import com.aeternity.aecan.databinding.ActivityForgotPasswordBinding;
import com.aeternity.aecan.helpers.DialogHelper;
import com.aeternity.aecan.util.Constants;
import com.aeternity.aecan.util.EditTextValidationUtils;
import com.aeternity.aecan.viewModels.RecoveryPasswordViewModel;
import com.aeternity.aecan.views.base.BaseActivity;

public class ForgotPasswordActivity extends BaseActivity {
    private ActivityForgotPasswordBinding binding;
    private RecoveryPasswordViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_forgot_password);
        viewModel = ViewModelProviders.of(this).get(RecoveryPasswordViewModel.class);
        binding.setVariable(BR.recoveryPasswordViewModel, viewModel);
        observerRecoveryViewModel();
    }

    private void observerRecoveryViewModel() {
        viewModel.getAction().observe(this, aBoolean -> validateFields());
        viewModel.getOnRecoveryPasswordSuccessData().observe(this, onRecoveryPasswordSuccess -> {
            hideActivityOverlay();
            goToLogin(binding.editTextEmailForgotPassword.getText().toString());

        });
        viewModel.getOnRecoveryPasswordFailData().observe(this, onRecoveryPasswordFailData -> {
            hideActivityOverlay();
            showToast(onRecoveryPasswordFailData.getMessage());
        });
    }


    public void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }


    private void validateFields() {
        if (isFieldsValid()) {
            showActivityOverlay();
            viewModel.makeRecoveryPassword(binding.editTextEmailForgotPassword.getText().toString());
            hideKeyboard();
        }
    }


    private boolean isFieldsValid() {
        if (!EditTextValidationUtils.validateTextNonEmpty(binding.editTextEmailForgotPassword) ||
                !EditTextValidationUtils.checkIfIsEmail(binding.editTextEmailForgotPassword.getText().toString())) {
            makeMessage(R.string.login__email_invalid_field);
            binding.editTextEmailForgotPassword.requestFocus();
            return false;
        }

        return true;
    }


    public AlertDialog.Builder showSuccessDialog() {
        return new AlertDialog.Builder(
                this)
                .setTitle(getString(R.string.recovery_password))
                .setMessage("Email enviado exitosamente!")
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
    }

    private void goToLogin(String email) {
        startActivity(new Intent(this, LoginActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                .putExtra(Constants.INTENT_STRING, email));
        finish();
    }
}
