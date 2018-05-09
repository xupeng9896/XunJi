package com.example.xupeng.xunji.imp;

import android.view.View;

/**
 * Created by xupeng on 2018/3/20.
 */

public interface OnRecyclerViewItemClickListener {
    void onItemClickListener(View view,int position);
    //void onWidgetClickListener(int position);
    void onLoveClickListener(int position,boolean state);
}
