package com.stephen.astro.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.stephen.astro.R;
import com.stephen.astro.modelhandlers.LoginModelHandler;
import com.stephen.astro.util.ViewUtils;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import org.json.JSONException;

import java.util.Arrays;

/**
 * Created by stephenadipradhana on 1/3/17.
 */

public class LoginActivity extends RxAppCompatActivity {
    private CallbackManager callbackManager;
    private LoginModelHandler mModelHandler;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mModelHandler = new LoginModelHandler(this);

        if (mModelHandler.isLogin()) {
            TaskStackBuilder.create(LoginActivity.this)
                    .addNextIntent(new Intent(this, MainActivity.class))
                    .startActivities();
        } else {
            callbackManager = CallbackManager.Factory.create();
            LoginManager.getInstance().logOut();
            LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);

            loginButton.setReadPermissions(Arrays.asList("public_profile", "email"));

            loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    progressDialog = ViewUtils.showProgressDialog(LoginActivity.this, getString(R.string.loading));
                    GraphRequest request = GraphRequest.newMeRequest(
                            loginResult.getAccessToken(),
                            (object, response) -> {
                                Log.v("LoginActivity", response.toString());

                                try {
                                    String email = object.getString("email");
                                    login(email);
                                } catch (JSONException e) {
                                    progressDialog.dismiss();
                                    e.printStackTrace();
                                    Toast.makeText(LoginActivity.this, R.string.fail_login, Toast.LENGTH_SHORT).show();
                                }
                            });
                    Bundle parameters = new Bundle();
                    parameters.putString("fields", "id,name,email");
                    request.setParameters(parameters);
                    request.executeAsync();
                }

                @Override
                public void onCancel() {
                }

                @Override
                public void onError(FacebookException error) {
                    error.printStackTrace();
                    Toast.makeText(LoginActivity.this, R.string.fail_login, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void login(String email) {
        mModelHandler.login(email)
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(aVoid -> {
                    progressDialog.dismiss();
                    TaskStackBuilder.create(LoginActivity.this)
                            .addNextIntent(new Intent(LoginActivity.this, MainActivity.class))
                            .startActivities();
                }, throwable -> {
                    throwable.printStackTrace();
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, R.string.fail_login, Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
