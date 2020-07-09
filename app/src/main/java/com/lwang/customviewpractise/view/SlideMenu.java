package com.lwang.customviewpractise.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.RelativeLayout;
import com.blankj.utilcode.util.ScreenUtils;
import com.lwang.customviewpractise.R;

/**
 * description：
 *
 * @author: Lwang
 * @createTime: 2020-07-08 13:46
 */
public class SlideMenu extends HorizontalScrollView
    implements View.OnScrollChangeListener {

  private static final String TAG = "SlideMenu";

  private int mMenuRightMargin;
  private int mAppScreenWidth;
  private View mMenuView;
  private View mContentView;
  private GestureDetector mGestureDetector;
  private View mShadowView;

  public SlideMenu(Context context) {
    this(context, null);
  }

  public SlideMenu(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public SlideMenu(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);

    TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SlideMenu);

    mMenuRightMargin = typedArray.getDimensionPixelSize(R.styleable.SlideMenu_menuRightMargin, 50);

    typedArray.recycle();

    mAppScreenWidth = ScreenUtils.getAppScreenWidth();
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      setOnScrollChangeListener(this);
    } else {
    }

    mGestureDetector = new GestureDetector(mSimpleOnGestureListener);
  }

  @Override protected void onFinishInflate() {
    super.onFinishInflate();

    ViewGroup viewGroup = (ViewGroup) getChildAt(0);
    mMenuView = viewGroup.getChildAt(0);
    mContentView = viewGroup.getChildAt(1);

    ViewGroup.LayoutParams layoutParams = mMenuView.getLayoutParams();
    layoutParams.width = mAppScreenWidth - mMenuRightMargin;

    ViewGroup.LayoutParams contentViewLayoutParams = mContentView.getLayoutParams();
    contentViewLayoutParams.width = mAppScreenWidth;
    //删除原有关系
    viewGroup.removeView(mContentView);

    RelativeLayout relativeLayout = new RelativeLayout(getContext());
    viewGroup.addView(relativeLayout);
    ViewGroup.LayoutParams relativeLayoutLayoutParams = relativeLayout.getLayoutParams();
    relativeLayoutLayoutParams.width = ScreenUtils.getScreenWidth();
    relativeLayout.setLayoutParams(relativeLayoutLayoutParams);

    relativeLayout.addView(mContentView);

    mShadowView = new View(getContext());
    relativeLayout.addView(mShadowView);
    ViewGroup.LayoutParams viewLayoutParams = mShadowView.getLayoutParams();
    viewLayoutParams.width = ScreenUtils.getScreenWidth();
    mShadowView.setLayoutParams(viewLayoutParams);

    mShadowView.setBackgroundColor(Color.parseColor("#505050"));


    mShadowView.setAlpha(0.0f);
  }

  @Override public boolean onTouchEvent(MotionEvent ev) {
    if (isIntercept) {
      return true;
    }
    if (mGestureDetector.onTouchEvent(ev)) {
      return true;
    }
    if (ev.getAction() == MotionEvent.ACTION_UP) {
      int scrollX = getScrollX();
      if (scrollX > mMenuView.getMeasuredWidth() / 2) {
        closeMenu();
      } else {
        openMenu();
      }
      return true;
    }
    return super.onTouchEvent(ev);
  }

  private boolean isMenuOpen = false;

  private boolean isIntercept = false;

  private void openMenu() {
    smoothScrollTo(0, 0);
    isMenuOpen = true;
    isIntercept = false;
  }

  private void closeMenu() {
    smoothScrollTo(mMenuView.getMeasuredWidth(), 0);
    isMenuOpen = false;
    isIntercept = false;
  }

  @Override protected void onLayout(boolean changed, int l, int t, int r, int b) {
    super.onLayout(changed, l, t, r, b);
    scrollTo(mMenuView.getMeasuredWidth(), 0);
  }

  @Override
  public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
    float scale = (1f * scrollX) / mMenuView.getMeasuredWidth();

    mShadowView.setAlpha(0.7f - scale);

    //float contentScale = 0.6f + 0.4f * scale;
    //mContentView.setPivotX(0);
    //mContentView.setPivotY(mContentView.getMeasuredHeight() / 2);
    //mContentView.setScaleX(contentScale);
    //mContentView.setScaleY(contentScale);
    //
    //float alpha = 0.6f + 0.4f * (1 - scale);
    //mMenuView.setAlpha(alpha);
    //mMenuView.setScaleX(alpha);
    //mMenuView.setScaleY(alpha);

    mMenuView.setTranslationX(0.7f * scrollX);
  }

  GestureDetector.SimpleOnGestureListener mSimpleOnGestureListener =
      new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
          Log.d(TAG, "velocityX : " + velocityX + " velocityY : " + velocityY);

          if (isMenuOpen) {
            if (velocityX < 0 && velocityX < velocityY) {
              closeMenu();
              return true;
            }
          } else {
            if (velocityX > 0 && velocityX > velocityY) {
              openMenu();
              return true;
            }
          }
          return super.onFling(e1, e2, velocityX, velocityY);
        }
      };

  @Override public boolean onInterceptTouchEvent(MotionEvent ev) {

    if (ev.getX() > mMenuView.getMeasuredWidth()) {
      closeMenu();
      isIntercept = true;
      return true;
    }

    isIntercept = false;

    return super.onInterceptTouchEvent(ev);
  }
}
