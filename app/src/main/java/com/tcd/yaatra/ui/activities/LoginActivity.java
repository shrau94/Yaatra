package com.tcd.yaatra.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.tcd.yaatra.R;
import com.tcd.yaatra.databinding.ActivityLoginBinding;
import com.tcd.yaatra.utils.Executor;
import com.tcd.yaatra.repository.datasource.UserInfoRepository;
import com.tcd.yaatra.ui.viewmodels.LoginActivityViewModel;
import com.tcd.yaatra.utils.Constants;
import com.tcd.yaatra.utils.SharedPreferenceUtils;

import org.jetbrains.annotations.TestOnly;
import javax.inject.Inject;

public class LoginActivity extends BaseActivity<ActivityLoginBinding> {

    private static final String TAG = "LoginActivity.class";
    @Inject
    LoginActivityViewModel loginActivityViewModel;
    SharedPreferences loginPreferences;

    @Inject
    UserInfoRepository userInfoRepository;

    //private Observer<AsyncData<LoginResponse>> apiObserver;
    //private LoginActivity activity;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initEventHandlers() {
        super.initEventHandlers();
        layoutDataBinding.login.setOnClickListener(view -> handleOnLoginButtonClick());
        layoutDataBinding.showHide.setOnClickListener(view -> handleOnShowHideClick());
        layoutDataBinding.registerLink.setOnClickListener(view -> handleOnRegisterLinkClick());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.loginPreferences = SharedPreferenceUtils.createLoginSharedPreference();

        //createLiveDataObserver();
    }

    private void handleOnLoginButtonClick() {

        if(TextUtils.isEmpty(layoutDataBinding.username.getText().toString()))
        {
            layoutDataBinding.username.setError(Constants.USER_NAME_IS_REQUIRED);
            layoutDataBinding.username.setHint(Constants.PLEASE_ENTER_USER_NAME);
            layoutDataBinding.username.requestFocus();
        }

        else if(TextUtils.isEmpty(layoutDataBinding.password.getText().toString()))
        {
            layoutDataBinding.password.setError(Constants.PASSWORD_IS_REQUIRED);
            layoutDataBinding.password.setHint(Constants.PLEASE_ENTER_PASSWORD);
            layoutDataBinding.password.requestFocus();
        }

        else {
            String username = layoutDataBinding.username.getText().toString();
            String password = layoutDataBinding.password.getText().toString();

            loginActivityViewModel.authenticateUser(username, password)
                    .observe(this, loginResponse -> {
                        switch (loginResponse.getState()) {
                            case LOADING:
                                layoutDataBinding.progressBarOverlay.setVisibility(View.VISIBLE);
                                break;

                        case SUCCESS:
                            layoutDataBinding.progressBarOverlay.setVisibility(View.GONE);
                            SharedPreferenceUtils.setAuthToken(loginResponse.getData().getAuthToken());
                            SharedPreferenceUtils.setUserName(loginResponse.getData().getUserInfo().getUsername());
                            SharedPreferenceUtils.setUserId(String.valueOf(loginResponse.getData().getUserInfo().getId()));

                            userInfoRepository.getUserProfile(loginResponse.getData().getUserInfo().getUsername()).observe(this, response -> {
                                if(response==null) {
                                    Executor.IOThread(() -> userInfoRepository.insertUser(loginResponse.getData().getUserInfo()));
                                }
                                else {
                                    Executor.IOThread(() -> userInfoRepository.updateUser(loginResponse.getData().getUserInfo()));
                                }
                            });

                            Intent myIntent = new Intent(LoginActivity.this, MenuContainerActivity.class);
                            startActivity(myIntent);

                            // Permissions for offline maps
                            String sPermission = android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
                            if (!checkPermission(sPermission, this))
                            {
                                String sPermission2 = android.Manifest.permission.ACCESS_FINE_LOCATION;
                                requestPermission(new String[]{sPermission, sPermission2});
                            }

                            finish();
                            break;

                        case FAILURE:
                            layoutDataBinding.progressBarOverlay.setVisibility(View.GONE);
                            Toast.makeText(this, loginResponse.getData().getMessage(), Toast.LENGTH_SHORT).show();
                            break;
                        }
                    });
        }
    }

    /**
     * Permissions for offline maps
     */
    private void requestPermission(String[] sPermission){
        if(this != null)
            ActivityCompat.requestPermissions(this, sPermission, 1);
    }

    private static boolean checkPermission(String sPermission, Activity activity) {
        if (ActivityCompat.checkSelfPermission(activity, sPermission)
                == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(grantResults.length >= 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            finish();
        }
        else {
            Log.d(TAG, "onRequestPermissionsResult: No access");
        }
    }

    private void handleOnShowHideClick() {

        if( layoutDataBinding.password.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
            layoutDataBinding.showHide.setImageResource(R.drawable.ic_show_password);

            //Show Password
            layoutDataBinding.password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            layoutDataBinding.password.setSelection(layoutDataBinding.password.length());

        }
        else{
            layoutDataBinding.showHide.setImageResource(R.drawable.ic_hide_password);

            //Hide Password
            layoutDataBinding.password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            layoutDataBinding.password.setSelection(layoutDataBinding.password.length());

        }
    }

    private void handleOnRegisterLinkClick() {
        Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
        startActivity(intent);
    }

    @TestOnly
    public void setLoginActivityViewModel(LoginActivityViewModel viewModel){
        this.loginActivityViewModel = viewModel;
    }

    @TestOnly
    public void setUserInfoRepository(UserInfoRepository repository){
        this.userInfoRepository = repository;
    }

    /*@TestOnly
    public void setApiObserver(Observer<AsyncData<LoginResponse>> observer){
        this.apiObserver = observer;
    }*/

    /*private void createLiveDataObserver(){
        apiObserver = new Observer<AsyncData<LoginResponse>>() {
            @Override
            public void onChanged(AsyncData<LoginResponse> loginResponse) {

                switch (loginResponse.getState()) {
                    case LOADING:
                        layoutDataBinding.progressBarOverlay.setVisibility(View.VISIBLE);
                        break;

                    case SUCCESS:
                        layoutDataBinding.progressBarOverlay.setVisibility(View.GONE);
                        Intent myIntent = new Intent(LoginActivity.this, MenuContainerActivity.class);
                        SharedPreferenceUtils.setAuthToken(loginResponse.getData().getAuthToken());
                        SharedPreferenceUtils.setUserName(loginResponse.getData().getUserInfo().getUsername());
                        SharedPreferenceUtils.setUserId(String.valueOf(loginResponse.getData().getUserInfo().getId()));

                        userInfoRepository.getUserProfile(loginResponse.getData().getUserInfo().getUsername()).observe(activity, response -> {
                            if(response==null) {
                                Executor.IOThread(() -> userInfoRepository.insertUser(loginResponse.getData().getUserInfo()));
                            }
                            else {
                                Executor.IOThread(() -> userInfoRepository.updateUser(loginResponse.getData().getUserInfo()));
                            }
                        });

                        startActivity(myIntent);
                        finish();
                        break;

                    case FAILURE:
                        layoutDataBinding.progressBarOverlay.setVisibility(View.GONE);
                        Toast.makeText(activity, loginResponse.getData().getMessage(), Toast.LENGTH_SHORT).show();
                        break;
                }

            }
        };
    }*/
}
