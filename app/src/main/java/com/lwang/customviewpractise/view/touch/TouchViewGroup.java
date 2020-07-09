package com.lwang.customviewpractise.view.touch;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.Nullable;

/**
 * description：
 *
 * @author: Lwang
 * @createTime: 2020-07-07 15:38
 */
public class TouchViewGroup extends LinearLayout {
  private static final String TAG = "TouchViewGroup";
  public TouchViewGroup(Context context) {
    super(context);
  }

  public TouchViewGroup(Context context,
      @Nullable AttributeSet attrs) {
    super(context, attrs);
  }

  public TouchViewGroup(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override public boolean dispatchTouchEvent(MotionEvent ev) {
    Log.d(TAG,"ViewGroup dispatchTouchEvent action : " + ev.getAction());
    return super.dispatchTouchEvent(ev);
  }

  @Override public boolean onInterceptTouchEvent(MotionEvent ev) {
    Log.d(TAG,"ViewGroup onInterceptTouchEvent action : " + ev.getAction());
    return super.onInterceptTouchEvent(ev);
  }

  @Override public boolean onTouchEvent(MotionEvent event) {
    Log.d(TAG,"ViewGroup onTouchEvent action : " + event.getAction());
    return super.onTouchEvent(event);
  }
}
