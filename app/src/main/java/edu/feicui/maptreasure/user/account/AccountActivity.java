package edu.feicui.maptreasure.user.account;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.hybridsquad.android.library.CropHandler;
import org.hybridsquad.android.library.CropHelper;
import org.hybridsquad.android.library.CropParams;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import edu.feicui.maptreasure.R;
import edu.feicui.maptreasure.commons.ActivityUtils;
import edu.feicui.maptreasure.components.IconSelectWindow;
import edu.feicui.maptreasure.user.UserPref;

public class AccountActivity extends MvpActivity<AccountView,AccountPresenter> implements AccountView{

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.iv_userIcon)
    ImageView imageView;
    private ActivityUtils activityUtils;
    private IconSelectWindow iconSelectWindow; // 按下icon，弹出的POPUOWINDOW
    private AccountPresenter accountPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityUtils=new ActivityUtils(this);
        setContentView(R.layout.activity_account);

        String keyPhoto = UserPref.getInstance().getKeyPhoto();
        if (keyPhoto != null) {
            ImageLoader.getInstance().displayImage(keyPhoto, imageView);
        }

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

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getTitle());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        imageView= (ImageView) findViewById(R.id.iv_userIcon);

    }

    @NonNull
    @Override
    public AccountPresenter createPresenter() {
        accountPresenter=new AccountPresenter();
        return accountPresenter;
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

    private CropHandler cropHandler=new CropHandler() {
        @Override
        public void onPhotoCropped(Uri uri) {
            File file = new File(uri.getPath());
            accountPresenter.upLoadPhoto(file);
        }

        @Override
        public void onCropCancel() {
            activityUtils.showToast("onCancel");
        }

        @Override
        public void onCropFailed(String message) {
            activityUtils.showToast("onfailed");
        }

        @Override
        public CropParams getCropParams() {
           CropParams cropParams=new CropParams();
            cropParams.aspectX=300;
            cropParams.aspectY=300;
            return cropParams;
        }

        @Override
        public Activity getContext() {
            return AccountActivity.this;
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        CropHelper.handleResult(cropHandler,requestCode, resultCode, data);
    }

    private IconSelectWindow.Listener listener=new IconSelectWindow.Listener() {
        @Override
        public void toGallery() {
            CropHelper.clearCachedCropFile(cropHandler.getCropParams().uri);
            Intent intent=CropHelper.buildCropFromGalleryIntent(cropHandler.getCropParams());
            startActivityForResult(intent,CropHelper.REQUEST_CROP);
        }

        @Override
        public void toCamera() {
            CropHelper.clearCachedCropFile(cropHandler.getCropParams().uri);
            Intent intent=CropHelper.buildCaptureIntent(cropHandler.getCropParams().uri);
            startActivityForResult(intent,CropHelper.REQUEST_CAMERA);
        }
    };

    private ProgressDialog progressDialog;
    @Override
    public void showProgress() {
        progressDialog=ProgressDialog.show(this,"","头像上传中....");
    }

    @Override
    public void hideProgress() {
        if(progressDialog.isShowing())progressDialog.dismiss();
    }

    @Override
    public void showMessage(String msg) {
        activityUtils.showToast(msg);
    }

    @Override
    public void upLoadIcon(String url) {
        ImageLoader.getInstance().displayImage(url,imageView);
    }
}
