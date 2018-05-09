package com.example.xupeng.xunji.utils;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by xupeng on 2018/3/16.
 */

public class SearchTrackRecyclerItemDecoration extends RecyclerView.ItemDecoration {

    private int space;

    public SearchTrackRecyclerItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.bottom = space;
    }
}
