package edu.feicui.maptreasure.ui.user.account;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import edu.feicui.maptreasure.R;
import edu.feicui.maptreasure.commons.ActivityUtils;
import edu.feicui.maptreasure.components.IconSelectWindow;

public class AccountActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    private ActivityUtils activityUtils;
    private IconSelectWindow iconSelectWindow; // 按下icon，弹出的POPUOWINDOW
    private ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityUtils=new ActivityUtils(this);
        setContentView(R.layout.activity_account);
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getTitle());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        imageView= (ImageView) findViewById(R.id.iv_userIcon);

    }

    @OnClick(R.id.iv_userIcon)
    public void onClick(View view){
        if(iconSelectWindow==null){
            iconSelectWindow=new IconSelectWindow(this,listener);
        }
        if(iconSelectWindow.isShowing()){
            iconSelectWindow.dismiss();
        }
        iconSelectWindow.show();
    }

    private IconSelectWindow.Listener listener=new IconSelectWindow.Listener() {
        @Override
        public void toGallery() {
            activityUtils.showToast("toGallery");
        }

        @Override
        public void toCamera() {
            activityUtils.showToast("toCamera");
        }
    };
}
