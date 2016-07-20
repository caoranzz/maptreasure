package edu.feicui.maptreasure;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import edu.feicui.maptreasure.commons.ActivityUtils;
import edu.feicui.maptreasure.user.login.LoginActivity;
import edu.feicui.maptreasure.user.register.RegisterActivity;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.btn_Register)
    Button btnRegister;
    @Bind(R.id.btn_Login)
    Button btnLogin;
    private ActivityUtils activityUtils;

    public static final String ENTER_HOME="action.enter.home";
    private BroadcastReceiver reciever=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityUtils=new ActivityUtils(this);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        IntentFilter intentFilter=new IntentFilter(ENTER_HOME);
        LocalBroadcastManager.getInstance(this).registerReceiver(reciever,intentFilter);
    }


    @OnClick({R.id.btn_Register, R.id.btn_Login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_Register:
            activityUtils.startActivity(RegisterActivity.class);
                break;
            case R.id.btn_Login:
                activityUtils.startActivity(LoginActivity.class);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(reciever);
    }
}
