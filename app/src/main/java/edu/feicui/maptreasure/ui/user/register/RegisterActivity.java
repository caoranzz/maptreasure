package edu.feicui.maptreasure.ui.user.register;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.hannesdorfmann.mosby.mvp.MvpActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import edu.feicui.maptreasure.R;
import edu.feicui.maptreasure.commons.ActivityUtils;
import edu.feicui.maptreasure.commons.RegexUtils;
import edu.feicui.maptreasure.components.AlertDialogFragment;
import edu.feicui.maptreasure.ui.MainActivity;
import edu.feicui.maptreasure.ui.entity.UserInfo;
import edu.feicui.maptreasure.ui.home.HomeActivity;

public class RegisterActivity extends MvpActivity<RegisterView,RegisterPresenter> implements RegisterView{

    @Bind(R.id.et_Username)
    EditText etUsername;
    @Bind(R.id.et_Password)
    EditText etPassword;
    @Bind(R.id.et_Confirm)
    EditText etConfirm;
    @Bind(R.id.btn_Register)
    Button btnRegister;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    private ActivityUtils activityUtils;
    private String username;
    private String userpwd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityUtils=new ActivityUtils(this);
        setContentView(R.layout.activity_register);
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("注册");
        }
        etUsername.addTextChangedListener(mTextWatcher);
        etPassword.addTextChangedListener(mTextWatcher);
        etConfirm.addTextChangedListener(mTextWatcher);
    }

    @NonNull
    @Override
    public RegisterPresenter createPresenter() {
        return new RegisterPresenter();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
             finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private final TextWatcher mTextWatcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            username = etUsername.getText().toString();
            userpwd = etPassword.getText().toString();
            String confirm = etConfirm.getText().toString();
            boolean canRegister = (!TextUtils.isEmpty(username)) && (!TextUtils.isEmpty(userpwd))
                    && userpwd.equals(confirm);
            btnRegister.setEnabled(canRegister);
        }
    };


    @OnClick(R.id.btn_Register)
    public void onClick() {
        if (RegexUtils.verifyUsername(username) != RegexUtils.VERIFY_SUCCESS) {
            AlertDialogFragment.newInstance(R.string.username_error,getString(R.string.username_rules))
            .show(getFragmentManager(),"showUsernameError");
            etUsername.setText(null);
            etPassword.setText(null);
            etConfirm.setText(null);
            return;
        }
        if (RegexUtils.verifyPassword(userpwd) != RegexUtils.VERIFY_SUCCESS) {
            AlertDialogFragment.newInstance(R.string.password_error,getString(R.string.password_rules))
                    .show(getFragmentManager(), "showPasswordError");
            etPassword.setText(null);
            etConfirm.setText(null);
            return;
        }
        getPresenter().register(new UserInfo(username,userpwd));
    }

    ProgressDialog progressDialog;
    @Override
    public void showProgress() {
        progressDialog=ProgressDialog.show(this,null,"正在注册，请稍后...");
        activityUtils.hideSoftKeyboard();
    }

    @Override
    public void hideProgress() {
        progressDialog.dismiss();
    }

    @Override
    public void showMessage(String msg) {
        activityUtils.showToast(msg);
    }

    @Override
    public void navigateToHome() {
        activityUtils.startActivity(HomeActivity.class);
        finish();
        Intent intent=new Intent(MainActivity.ENTER_HOME);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

}
