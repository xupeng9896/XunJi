package com.example.xupeng.xunji.utils;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

import com.example.xupeng.xunji.imp.OnInputCompleteListener;

public class MyEditText extends AppCompatEditText {

    private OnInputCompleteListener mOnInputCompleteListener;

    public MyEditText(Context context) {
        super(context);
    }

    public MyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction,
                                  Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);

        if (!focused) {
            mOnInputCompleteListener.onInputComplete();
        }
    }

    public void setOnInputCompleteListener(OnInputCompleteListener listener) {
        this.mOnInputCompleteListener = listener;
    }

}
