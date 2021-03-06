package com.ytb.commonbackground;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 支持XML配置的通用背景TextView
 *
 * @author yintaibing
 */
public class CommonBackgroundTextView extends TextView {

    public CommonBackgroundTextView(Context context) {
        this(context, null);
    }

    public CommonBackgroundTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommonBackgroundTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        CommonBackgroundFactory.fromXml(this, attrs);
    }
}
