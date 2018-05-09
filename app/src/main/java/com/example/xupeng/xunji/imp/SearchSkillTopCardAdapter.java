package com.example.xupeng.xunji.imp;

import android.support.v7.widget.CardView;

/**
 * Created by xupeng on 2018/3/14.
 */

public interface SearchSkillTopCardAdapter {

    int MAX_ELEVATION_FACTOR = 8;

    float getBaseElevation();
    //获取cardView位置
    CardView getCardViewAt(int position);
    //获取个数
    int getCount();
}
