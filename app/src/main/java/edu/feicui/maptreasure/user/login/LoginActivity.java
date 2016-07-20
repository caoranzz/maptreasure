package edu.feicui.maptreasure.user.login;

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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hannesdorfmann.mosby.mvp.MvpActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import edu.feicui.maptreasure.R;
import edu.feicui.maptreasure.commons.ActivityUtils;
import edu.feicui.maptreasure.commons.RegexUtils;
import edu.feicui.maptreasure.components.AlertDialogFragment;
import edu.feicui.maptreasure.MainActivity;
import edu.feicui.maptreasure.user.UserInfo;
import edu.feicui.maptreasure.treasure.home.HomeActivity;
import edu.feicui.maptreasure.user.UserPref;

public class LoginActivity extends MvpActivity<LoginView,LoginPresenter> implements LoginView{

    @Bind(R.id.et_Username)
    EditText etUsername;
    @Bind(R.id.et_Password)
    EditText etPassword;
    @Bind(R.id.tv_forgetPassword)
    TextView tvForgetPassword;
    @Bind(R.id.btn_Login)
    Button btnLogin;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    private ActivityUtils activityUtils;
    private String userName;
    private String userPwd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityUtils=new ActivityUtils(this);
        setContentView(R.layout.activity_login);
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("登录");
        }
        String userName1 = UserPref.getInstance().getUserName();
        String userPwd1 = UserPref.getInstance().getUserPwd();
        if(userName1!=null){
            etUsername.setText(userName1);
            etPassword.setText(userPwd1);
            userName =etUsername.getText().toString();
            userPwd =etPassword.getText().toString();
            boolean canLogin = (!TextUtils.isEmpty(userName))&&(!TextUtils.isEmpty(userPwd));
            btnLogin.setEnabled(canLogin);
        }
        etUsername.addTextChangedListener(mTextWatcher);
        etPassword.addTextChangedListener(mTextWatcher);
    }

    @NonNull
    @Override
    public LoginPresenter createPresenter() {
        return new LoginPresenter();
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
            userName =etUsername.getText().toString();
            userPwd =etPassword.getText().toString();
            UserPref.getInstance().setUserName(userName);
            UserPref.getInstance().setUserPwd(userPwd);
            boolean canLogin = (!TextUtils.isEmpty(userName))&&(!TextUtils.isEmpty(userPwd));
            btnLogin.setEnabled(canLogin);

        }
    };

    @OnClick({R.id.tv_forgetPassword, R.id.btn_Login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_forgetPassword:
                break;
            case R.id.btn_Login:
                if(RegexUtils.verifyUsername(userName)!=RegexUtils.VERIFY_SUCCESS){
                    AlertDialogFragment fragment = AlertDialogFragment.newInstance(R.string.username_error, getString(R.string.username_rules));
                    fragment.show(getFragmentManager(),"showUsernameError");
                    etUsername.setText(null);
                    etPassword.setText(null);
                    return;
                }
                if(RegexUtils.verifyPassword(userPwd)!=RegexUtils.VERIFY_SUCCESS){
                    AlertDialogFragment fragment = AlertDialogFragment.newInstance(R.string.password_error, getString(R.string.password_rules));
                    fragment.show(getFragmentManager(), "showPasswordError");
                    etPassword.setText(null);
                    return;
                }
                getPresenter().login(new UserInfo(userName,userPwd));
                break;
        }
    }



    ProgressDialog progressDialog;
    @Override
    public void showProgress() {
        progressDialog=ProgressDialog.show(this,null,"正在登录，请稍后。。。");
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
