package edu.feicui.maptreasure.components;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DecimalFormat;

import butterknife.Bind;
import butterknife.ButterKnife;
import edu.feicui.maptreasure.R;
import edu.feicui.maptreasure.treasure.Treasure;

/**
 * Created by Administrator on 2016/7/19 0019.
 */
public class TreasureView extends RelativeLayout {
    public TreasureView(Context context) {
        super(context);
        init();
    }

    public TreasureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TreasureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    // 用来显示宝藏title
    @Bind(R.id.tv_treasureTitle)TextView tvTitle;
    // 用来显示宝藏位置描述
    @Bind(R.id.tv_treasureLocation)TextView tvLocation;
    // 用来显示宝藏距离
    @Bind(R.id.tv_distance)TextView tv_Distance;

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_treasure,this,true);
        ButterKnife.bind(this);
    }
    public void bindTreasure(@NonNull Treasure treasure){
        tvTitle.setText(treasure.getTitle());
        tvLocation.setText(treasure.getLocation());

        double distance = treasure.distanceToMyLocation();
        DecimalFormat format = new DecimalFormat("0.00");
        String str = format.format(distance/1000)+"km";
        tv_Distance.setText(str);
    }
}
