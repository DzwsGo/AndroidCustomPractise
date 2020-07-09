package com.lwang.customviewpractise.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import com.blankj.utilcode.util.LogUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * description：
 *
 * @author: Lwang
 * @createTime: 2020-07-03 11:04
 */
public class FlowLayout extends ViewGroup {
  private List<List<View>> mAllLineViews = new ArrayList<>();
  private List<Integer> mLineHeight = new ArrayList<>();
  private int widthSpace = dp2px(10);
  private int heightSpace = dp2px(10);
  public FlowLayout(Context context) {
    this(context, null);
  }

  public FlowLayout(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    mLineHeight.clear();
    mAllLineViews.clear();

    LogUtils.d("onMeasure lineCount");


    int selfWidth = MeasureSpec.getSize(widthMeasureSpec);
    int selfHeight = MeasureSpec.getSize(heightMeasureSpec);
    int selfWidthMode = MeasureSpec.getMode(widthMeasureSpec);
    int selfHeightMode = MeasureSpec.getMode(heightMeasureSpec);

    int lineWidthUsed = 0;
    int lineHeight = 0;
    int parentNeedWidth = 0;
    int parentNeedHeight = 0;
    List<View> lineViews = new ArrayList<>();

    //测量孩子
    int childCount = getChildCount();
    for (int i = 0; i < childCount; i++) {
      View child = getChildAt(i);
      LayoutParams childLp = child.getLayoutParams();
      if (child.getVisibility() != View.GONE) {
        int childWidthMeasureSpec =
            getChildMeasureSpec(widthMeasureSpec, getPaddingLeft() + getPaddingRight(), childLp.width);
        int childHeightMeasureSpec =
            getChildMeasureSpec(heightMeasureSpec, getPaddingTop() + getPaddingBottom(),
                childLp.height);

        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);

        int childMeasuredWidth = child.getMeasuredWidth();
        int childMeasuredHeight = child.getMeasuredHeight();

        LogUtils.d("onMeasure i : " + i + " childMeasuredWidth : " + childMeasuredWidth + " childMeasuredHeight : " + childMeasuredHeight);

        //判断是否需要换行
        if (lineWidthUsed + childMeasuredWidth + widthSpace > selfWidth) {
          mAllLineViews.add(lineViews);
          mLineHeight.add(lineHeight);
          parentNeedHeight = lineHeight + parentNeedHeight + heightSpace;
          parentNeedWidth = Math.max(parentNeedWidth, lineWidthUsed + widthSpace);
          lineViews = new ArrayList<>();
          lineWidthUsed = 0;
          lineHeight = 0;
        }

        lineViews.add(child);
        lineWidthUsed = lineWidthUsed + childMeasuredWidth + widthSpace;
        lineHeight = Math.max(lineHeight, childMeasuredHeight);

        //最后一行
        if (i == childCount - 1) {
          mAllLineViews.add(lineViews);
          mLineHeight.add(lineHeight);
          parentNeedHeight = lineHeight + parentNeedHeight + heightSpace;
          parentNeedWidth = Math.max(parentNeedWidth, lineWidthUsed + widthSpace);
        }
      }
    }
    //设置自己
    int realWidth = selfWidthMode == MeasureSpec.EXACTLY ? selfWidth : parentNeedWidth;
    int realHeight = selfHeightMode == MeasureSpec.EXACTLY ? selfHeight : parentNeedWidth;
    setMeasuredDimension(realWidth, realHeight);
  }

  @Override protected void onLayout(boolean changed, int l, int t, int r, int b) {
    int lineCount = mAllLineViews.size();

    LogUtils.d("onLayout lineCount : " + lineCount);

    int curT = getPaddingTop();
    int curL = getPaddingLeft();

    for (int i = 0; i < lineCount; i++) {
      List<View> views = mAllLineViews.get(i);
      Integer lineHeight = mLineHeight.get(i);
      LogUtils.d("views : " + views.size());
      for (int j = 0;j < views.size(); j++) {
        View view = views.get(j);

        int left = curL;
        int top = curT;
        int right = curL + view.getMeasuredWidth();
        int bottom = curT + view.getMeasuredHeight();

        view.layout(left,top,right,bottom);
        LogUtils.d("layout left : " + left + " top : " + top + " right : " + right + " bottom : " + bottom);
        curL = right + widthSpace;

      }
      curT = curT + lineHeight + heightSpace;
      curL = getPaddingLeft();
    }
  }

  public int dp2px(int dp) {
    return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
        getResources().getDisplayMetrics());
  }
}
