package com.example.xupeng.xunji.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.youth.banner.loader.ImageLoader;

/**
 * Created by xupeng on 2018/3/17.
 */

public class GlideImageLoader extends ImageLoader {

    @Override
    public void displayImage(Context context, Object o, ImageView imageView) {
        //Glide 加载图片简单用法
        Glide.with(context).load(o).into(imageView);
    }
}
