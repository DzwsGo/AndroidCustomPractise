package com.lwang.customviewpractise.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.customview.widget.ViewDragHelper;
import com.blankj.utilcode.util.LogUtils;

/**
 * description：
 *
 * @author: Lwang
 * @createTime: 2020-07-09 10:42
 */
public class VerticalDragView extends FrameLayout {

  private ViewDragHelper mViewDragHelper;
  private View mAfterView, mListView;
  private int mAfterViewHeight;
  private boolean isMenuOpen = false;

  public VerticalDragView(@NonNull Context context) {
    this(context, null);
  }

  public VerticalDragView(@NonNull Context context,
      @Nullable AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public VerticalDragView(@NonNull Context context, @Nullable AttributeSet attrs,
      int defStyleAttr) {
    super(context, attrs, defStyleAttr);

    mViewDragHelper = ViewDragHelper.create(this, mDragHelperCallback);
  }

  private ViewDragHelper.Callback mDragHelperCallback = new ViewDragHelper.Callback() {
    @Override public boolean tryCaptureView(@NonNull View child, int pointerId) {
      return mListView == child;
    }

    @Override public int clampViewPositionVertical(@NonNull View child, int top, int dy) {
      if (top < 0) {
        top = 0;
      } else if (top > mAfterViewHeight) {
        top = mAfterViewHeight;
      }
      return top;
    }

    @Override public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
      super.onViewReleased(releasedChild, xvel, yvel);
      if (releasedChild == mAfterView) {
        return;
      }
      if (yvel > mAfterViewHeight / 2) {
        //打开
        mViewDragHelper.settleCapturedViewAt(0, mAfterViewHeight);
        isMenuOpen = true;
      } else {
        mViewDragHelper.settleCapturedViewAt(0, 0);
        isMenuOpen = false;
      }
      invalidate();
    }
  };

  @Override public boolean onTouchEvent(MotionEvent event) {
    mViewDragHelper.processTouchEvent(event);
    return true;
  }

  @Override protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
    super.onLayout(changed, left, top, right, bottom);
    mAfterViewHeight = mAfterView.getMeasuredHeight();
  }

  @Override protected void onFinishInflate() {
    super.onFinishInflate();
    mAfterView = getChildAt(0);
    mListView = getChildAt(1);
  }

  @Override public void computeScroll() {
    super.computeScroll();
    if (mViewDragHelper.continueSettling(true)) {
      invalidate();
    }
  }

  float mDownY = 0;

  @Override public boolean onInterceptTouchEvent(MotionEvent ev) {

    int action = ev.getAction();
    LogUtils.d("onInterceptTouchEvent ev : " + action);

    if(isMenuOpen) {
      return true;
    }

    switch (ev.getAction()) {
      case MotionEvent.ACTION_DOWN:
        mDownY = ev.getY();
        // 让 DragHelper 拿一个完整的事件
        mViewDragHelper.processTouchEvent(ev);
        break;
      case MotionEvent.ACTION_MOVE:
        float moveY = ev.getY();
        if ((moveY - mDownY) > 0 & !canChildScrollUp()) {
          // 向下滑动 && 滚动到了顶部，拦截不让ListView做处理
          return true;
        }
        break;
    }

    return super.onInterceptTouchEvent(ev);
  }

  /**
   * @return Whether it is possible for the child view of this layout to
   * scroll up. Override this if the child view is a custom view.
   * 判断View是否滚动到了最顶部,还能不能向上滚
   */
  public boolean canChildScrollUp() {
    if (mListView instanceof AbsListView) {
      final AbsListView absListView = (AbsListView) mListView;
      return absListView.getChildCount() > 0
          && (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0)
          .getTop() < absListView.getPaddingTop());
    } else {
      return ViewCompat.canScrollVertically(mListView, -1) || mListView.getScrollY() > 0;
    }
  }
}
