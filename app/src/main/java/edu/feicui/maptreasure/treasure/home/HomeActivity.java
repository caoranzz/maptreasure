package edu.feicui.maptreasure.treasure.home;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.Bind;
import butterknife.ButterKnife;
import edu.feicui.maptreasure.R;
import edu.feicui.maptreasure.commons.ActivityUtils;
import edu.feicui.maptreasure.treasure.TreasureRepo;
import edu.feicui.maptreasure.treasure.home.map.MapFragment;
import edu.feicui.maptreasure.user.UserPref;
import edu.feicui.maptreasure.user.account.AccountActivity;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{


    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.nav_view)
    NavigationView navView;
    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    private ActivityUtils activityUtils;
    private ImageView imageView;
    private MapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityUtils=new ActivityUtils(this);
        setContentView(R.layout.activity_home);
        mapFragment= (MapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
    }

    @Override
    protected void onStart() {
        super.onStart();
        TreasureRepo.getInstance().clear();
        String keyPhoto = UserPref.getInstance().getKeyPhoto();
        if (keyPhoto != null) {
            ImageLoader.getInstance().displayImage(keyPhoto, imageView);
        }
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        navView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        imageView= (ImageView) navView.getHeaderView(0).findViewById(R.id.iv_userIcon);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityUtils.startActivity(AccountActivity.class);
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_item_hide:
                drawerLayout.closeDrawer(GravityCompat.START);
                mapFragment.hideTreasure();
                break;
            case R.id.menu_item_my_list:
                break;
            case R.id.menu_item_help:
                break;
            case R.id.menu_item_logout:
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
            return;
        }else{
            if(mapFragment.onBackPressed()){
                super.onBackPressed();
            }
        }
    }
}
