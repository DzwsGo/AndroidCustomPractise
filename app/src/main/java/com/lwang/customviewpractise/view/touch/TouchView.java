package com.lwang.customviewpractise.view.touch;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import androidx.annotation.Nullable;

/**
 * description：
 *
 * @author: Lwang
 * @createTime: 2020-07-07 14:02
 */
public class TouchView extends ImageView {

  public static final String TAG = "TouchView";

  public TouchView(Context context) {
    super(context);
  }

  public TouchView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
  }

  public TouchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override public boolean dispatchTouchEvent(MotionEvent event) {
    Log.d(TAG,"view dispatchTouchEvent action : " + event.getAction());
    getParent().requestDisallowInterceptTouchEvent(true);
    return super.dispatchTouchEvent(event);
  }

  @Override public boolean onTouchEvent(MotionEvent event) {
    Log.d(TAG,"view onTouchEvent action : " + event.getAction());
    //return super.onTouchEvent(event);
    return true;
  }
}
