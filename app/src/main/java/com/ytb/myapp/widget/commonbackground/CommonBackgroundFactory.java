package com.ytb.myapp.widget.commonbackground;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;

import com.ytb.myapp.R;


/**
 * 通用背景工厂类
 *
 * @author yintaibing
 * @date 2016/10/29
 */
public class CommonBackgroundFactory {

    /**
     * 从XML解析通用背景
     *
     * @param view         View
     * @param attributeSet AttributeSet
     */
    public static void fromXml(View view, AttributeSet attributeSet) {
        CommonBackgroundAttrs attrs = obtainAttrs(view.getContext(), attributeSet);
        fromAttrSet(view, attrs);
    }

    /**
     * 从AttrSet解析通用背景
     *
     * @param view View
     * @param attrSet AttrSet
     */
    public static void fromAttrSet(View view, CommonBackgroundAttrs attrSet) {
        if (attrSet != null) {
            if (attrSet.stateful) {
                CommonBackgroundSet set = stateful(attrSet);
                set.showOn(view);
            } else {
                ICommonBackground drawable = stateless(attrSet);
                drawable.showOn(view);
            }

            attrSet.recycle();
        }
    }

    /**
     * 创建不区分状态（disabled, normal, pressed）的通用背景
     *
     * @return 单独一个CommonBackground
     */
    public static CommonBackground createStateless() {
        return new CommonBackground();
    }

    /**
     * 创建区分状态（disabled, normal, pressed）的通用背景
     *
     * @return CommonBackgroundSet
     */
    public static CommonBackgroundSet createClickable() {
        return new CommonBackgroundSet(CommonBackgroundSet.STATE_MODE_CLICK);
    }

    /**
     * 创建区分状态（disabled, unchecked, checked）的通用背景
     *
     * @return CommonBackgroundSet
     */
    public static CommonBackgroundSet createCheckable() {
        return new CommonBackgroundSet(CommonBackgroundSet.STATE_MODE_CHECK);
    }

    /**
     * 提取自定义的属性集
     *
     * @param context      Context
     * @param attributeSet AttributeSet
     * @return AttrSet
     */
    public static CommonBackgroundAttrs obtainAttrs(Context context, AttributeSet attributeSet) {
        if (context != null && attributeSet != null) {
            CommonBackgroundAttrs attrs = new CommonBackgroundAttrs();
            TypedArray a = context.obtainStyledAttributes(attributeSet,
                    R.styleable.CommonBackground);
            attrs.stateful = a.getBoolean(R.styleable.CommonBackground_stateful, false);
            attrs.stateMode = a.getInt(R.styleable.CommonBackground_stateMode,
                    CommonBackgroundSet.STATE_MODE_CLICK);
            attrs.shape = a.getInt(R.styleable.CommonBackground_shape,
                    CommonBackground.SHAPE_RECT); // 默认直角矩形
            attrs.fillMode = a.getInt(R.styleable.CommonBackground_fillMode,
                    CommonBackground.FILL_MODE_SOLID); // 默认颜色填充
            attrs.scaleType = a.getInt(R.styleable.CommonBackground_scaleType,
                    CommonBackground.SCALE_TYPE_CENTER); // 默认无缩放
            attrs.strokeMode = a.getInt(R.styleable.CommonBackground_strokeMode,
                    CommonBackground.STROKE_MODE_NONE); // 默认无描边
            if (attrs.strokeMode != CommonBackground.STROKE_MODE_NONE) {
                attrs.strokeWidth = a.getDimensionPixelSize(
                        R.styleable.CommonBackground_strokeWidth, 0);
            }
            attrs.radius = a.getDimensionPixelSize(R.styleable.CommonBackground_radius, 0);
            attrs.strokeDashSolid = a.getDimensionPixelSize(
                    R.styleable.CommonBackground_strokeDashSolid, 0);
            attrs.strokeDashSpace = a.getDimensionPixelSize(
                    R.styleable.CommonBackground_strokeDashSpace, 0);
            attrs.colorDisabled = a.getColor(R.styleable.CommonBackground_colorDisabled,
                    Color.LTGRAY); // disabled状态默认使用浅灰色
            if (attrs.stateMode == CommonBackgroundSet.STATE_MODE_CLICK) {
                attrs.colorNormal = a.getColor(R.styleable.CommonBackground_colorNormal,
                        Color.WHITE); // normal状态默认使用白色
                if (attrs.stateful) {
                    attrs.colorPressed = a.getColor(R.styleable.CommonBackground_colorPressed,
                            attrs.colorNormal); // pressed状态默认与normal状态相同
                }
            } else if (attrs.stateMode == CommonBackgroundSet.STATE_MODE_CHECK) {
                attrs.colorUnchecked = a.getColor(R.styleable.CommonBackground_colorUnchecked,
                        Color.WHITE);
                if (attrs.stateful) {
                    attrs.colorChecked = a.getColor(R.styleable.CommonBackground_colorChecked,
                            Color.WHITE);
                }
            }
            attrs.colorStroke = a.getColor(R.styleable.CommonBackground_colorStroke,
                    Color.TRANSPARENT); // 描边默认使用透明
            int bitmapResId = a.getResourceId(R.styleable.CommonBackground_bitmap,
                    android.R.drawable.ic_delete);
            attrs.bitmap = BitmapFactory.decodeResource(context.getResources(), bitmapResId);

            a.recycle();
            return attrs;
        }
        return null;
    }

    private static ICommonBackground stateless(CommonBackgroundAttrs attrs) {
        return new CommonBackground()
                .shape(attrs.shape)
                .fillMode(attrs.fillMode)
                .scaleType(attrs.scaleType)
                .strokeMode(attrs.strokeMode)
                .strokeWidth(attrs.strokeWidth)
                .strokeDashSolid(attrs.strokeDashSolid)
                .strokeDashSpace(attrs.strokeDashSpace)
                .radius(attrs.radius)
                .colorStroke(attrs.colorStroke)
                .colorFill(attrs.colorNormal)
                .bitmap(attrs.bitmap);
    }

    private static CommonBackgroundSet stateful(CommonBackgroundAttrs attrs) {
        CommonBackgroundSet set = new CommonBackgroundSet(attrs.stateMode);
        set.forEach()
                .shape(attrs.shape)
                .fillMode(attrs.fillMode)
                .scaleType(attrs.scaleType)
                .strokeMode(attrs.strokeMode)
                .strokeWidth(attrs.strokeWidth)
                .strokeDashSolid(attrs.strokeDashSolid)
                .strokeDashSpace(attrs.strokeDashSpace)
                .colorStroke(attrs.colorStroke)
                .radius(attrs.radius)
                .bitmap(attrs.bitmap);
        set.theDisabled().colorFill(attrs.colorDisabled);
        if (attrs.stateMode == CommonBackgroundSet.STATE_MODE_CLICK) {
            set.theNormal().colorFill(attrs.colorNormal);
            set.thePressed().colorFill(attrs.colorPressed);
        } else if (attrs.stateMode == CommonBackgroundSet.STATE_MODE_CHECK) {
            set.theUnchecked().colorFill(attrs.colorUnchecked);
            set.theChecked().colorFill(attrs.colorChecked);
        }
        return set;
    }

}