package com.lwang.customviewpractise.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.lwang.customviewpractise.utils.MathUtil;
import java.util.ArrayList;
import java.util.List;

/**
 * description：
 *
 * @author: Lwang
 * @createTime: 2020-07-10 10:11
 */
public class LockPatternView extends View {

  private Paint mNormalPaint;
  private Paint mLinePaint;
  private Paint mPressPaint;
  private Paint mErrorPaint;
  private Paint mArrowPaint;

  // 颜色
  private int mOuterPressedColor = 0xff8cbad8;
  private int mInnerPressedColor = 0xff0596f6;
  private int mOuterNormalColor = 0xffd9d9d9;
  private int mInnerNormalColor = 0xff929292;
  private int mOuterErrorColor = 0xff901032;
  private int mInnerErrorColor = 0xffea0945;

  private List<Integer> password = new ArrayList<Integer>() {
    {
      add(0);
      add(1);
      add(2);
    }
  };

  private Point[][] mPoints = new Point[3][3];
  private List<Point> mSelectPoints = new ArrayList<>();

  private int mInnerRadius, mOuterRadius;

  private boolean isInit = false;
  private boolean mIsTouchPoint;

  public LockPatternView(Context context) {
    this(context, null);
  }

  public LockPatternView(Context context,
      @Nullable AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public LockPatternView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initPaint();
  }

  float mMovingX = 0f, mMovingY = 0f;

  @Override public boolean onTouchEvent(MotionEvent event) {
    mMovingX = event.getX();
    mMovingY = event.getY();
    Point downPoint;
    switch (event.getAction()) {
      case MotionEvent.ACTION_DOWN:
        downPoint = getDownPoint();
        if (downPoint != null) {
          mIsTouchPoint = true;
          mSelectPoints.add(downPoint);
          downPoint.setStatusPress();
        }
        break;
      case MotionEvent.ACTION_MOVE:
        if (mIsTouchPoint) {
          downPoint = getDownPoint();
          if (downPoint != null) {
            if (!mSelectPoints.contains(downPoint)) {
              mSelectPoints.add(downPoint);
            }
            downPoint.setStatusPress();
          }
        }
        break;
      case MotionEvent.ACTION_UP:
        mIsTouchPoint = false;

        List<Integer> selectArray = getSelectArray();

        ToastUtils.showShort(selectArray.toString());
        LogUtils.d("password : " + password.equals(selectArray));
        if (!password.equals(selectArray)) {
          //循环置为error
          setSelectPointStatus(Point.ERROR);
          mHandler.sendEmptyMessageDelayed(1000, 1000);
        } else {
          setSelectPointStatus(Point.NORMAL);
          mHandler.sendEmptyMessage(1001);
        }
        break;
      default:
    }
    invalidate();
    return true;
  }

  @SuppressLint("HandlerLeak") private Handler mHandler = new Handler() {
    @Override public void handleMessage(@NonNull Message msg) {
      super.handleMessage(msg);
      switch (msg.what) {
        case 1000:
          setSelectPointStatus(Point.NORMAL);
        case 1001:
          mSelectPoints = new ArrayList<>();
          invalidate();
          break;
        default:
      }
    }
  };

  @Override protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    if (!isInit) {
      isInit = true;
      initPoint();
    }

    for (int i = 0; i < mPoints.length; i++) {
      for (int j = 0; j < mPoints[i].length; j++) {
        switch (mPoints[i][j].status) {
          case Point.NORMAL:
            mNormalPaint.setColor(mInnerNormalColor);
            mNormalPaint.setStrokeWidth(SizeUtils.dp2px(2));
            drawCircle(canvas, mInnerRadius, mNormalPaint, mPoints[i][j]);
            mNormalPaint.setColor(mOuterNormalColor);
            mNormalPaint.setStrokeWidth(SizeUtils.dp2px(5));
            drawCircle(canvas, mOuterRadius, mNormalPaint, mPoints[i][j]);
            break;
          case Point.ERROR:
            mNormalPaint.setColor(mInnerErrorColor);
            mNormalPaint.setStrokeWidth(SizeUtils.dp2px(2));
            drawCircle(canvas, mInnerRadius, mNormalPaint, mPoints[i][j]);
            mNormalPaint.setColor(mOuterErrorColor);
            mNormalPaint.setStrokeWidth(SizeUtils.dp2px(5));
            drawCircle(canvas, mOuterRadius, mNormalPaint, mPoints[i][j]);
            break;
          case Point.PRESS:
            mNormalPaint.setColor(mInnerPressedColor);
            mNormalPaint.setStrokeWidth(SizeUtils.dp2px(2));
            drawCircle(canvas, mInnerRadius, mNormalPaint, mPoints[i][j]);
            mNormalPaint.setColor(mOuterPressedColor);
            mNormalPaint.setStrokeWidth(SizeUtils.dp2px(5));
            drawCircle(canvas, mOuterRadius, mNormalPaint, mPoints[i][j]);
            break;
          default:
        }
      }
    }
    drawLine(canvas);
  }

  private void drawCircle(Canvas canvas, int radius, Paint paint, Point point) {
    canvas.drawCircle(point.centerX, point.centerY, radius, paint);
  }

  private void initPoint() {

    int width = getWidth();
    int height = getHeight();

    int offsetX = 0, offsetY = 0, squareWidth;
    if (width > height) {
      offsetX = (width - height) / 2;
      squareWidth = height / 3;
    } else {
      squareWidth = width / 3;
      offsetY = (height - width) / 2;
    }

    mInnerRadius = squareWidth / 24;
    mOuterRadius = squareWidth / 4;

    mPoints[0][0] = new Point(offsetX + squareWidth / 2, offsetY + squareWidth / 2, 0);
    mPoints[0][1] = new Point(offsetX + squareWidth * 3 / 2, offsetY + squareWidth / 2, 1);
    mPoints[0][2] = new Point(offsetX + squareWidth * 5 / 2, offsetY + squareWidth / 2, 2);
    mPoints[1][0] = new Point(offsetX + squareWidth / 2, offsetY + squareWidth * 3 / 2, 3);
    mPoints[1][1] = new Point(offsetX + squareWidth * 3 / 2, offsetY + squareWidth * 3 / 2, 4);
    mPoints[1][2] = new Point(offsetX + squareWidth * 5 / 2, offsetY + squareWidth * 3 / 2, 5);
    mPoints[2][0] = new Point(offsetX + squareWidth / 2, offsetY + squareWidth * 5 / 2, 6);
    mPoints[2][1] = new Point(offsetX + squareWidth * 3 / 2, offsetY + squareWidth * 5 / 2, 7);
    mPoints[2][2] = new Point(offsetX + squareWidth * 5 / 2, offsetY + squareWidth * 5 / 2, 8);
  }

  private void initPaint() {
    //圆的默认状态
    mNormalPaint = getPaint();
    //圆的选中状态
    mPressPaint = getPaint();
    //线的绘制
    mLinePaint = getPaint();
    mLinePaint.setColor(mInnerPressedColor);

    //错误状态
    mErrorPaint = getPaint();
    //箭头
    mArrowPaint = getPaint();
    mArrowPaint.setColor(mInnerPressedColor);
  }

  private Paint getPaint() {
    Paint paint = new Paint();
    paint.setAntiAlias(true);
    paint.setStyle(Paint.Style.STROKE);
    paint.setDither(true);
    paint.setStrokeWidth(SizeUtils.dp2px(5));
    return paint;
  }

  class Point {
    static final int NORMAL = 0;
    static final int PRESS = 1;
    static final int ERROR = 2;
    int centerX, centerY, index;
    int status = NORMAL;

    Point(int centerX, int centerY, int index) {
      this.centerX = centerX;
      this.centerY = centerY;
      this.index = index;
    }

    void setStatusNormal() {
      status = NORMAL;
    }

    void setStatusPress() {
      status = PRESS;
    }

    void setStatusError() {
      status = ERROR;
    }

    @Override public String toString() {
      return "Point{" +
          "centerX=" + centerX +
          ", centerY=" + centerY +
          ", index=" + index +
          ", status=" + status +
          '}';
    }
  }

  /**
   * 绘制两个点之间的连线以及箭头
   */
  private void drawLine(Canvas canvas) {
    LogUtils.d("drawline : " + mSelectPoints + " : " + (mSelectPoints.size() - 1));
    if (mSelectPoints.size() >= 1) {
      // 两个点之间需要绘制一条线和箭头
      Point lastPoint = mSelectPoints.get(0);

      for (int index = 1; index <= mSelectPoints.size() - 1; index++) {
        // 两个点之间绘制一条线
        // 贝塞尔曲线 - 讲一次数学课
        drawLine(lastPoint, mSelectPoints.get(index), canvas, mLinePaint);
        // 两个点之间绘制一个箭头
        drawArrow(canvas, mArrowPaint, lastPoint, mSelectPoints.get(index),
            (float) (mOuterRadius / 5), 38);
        lastPoint = mSelectPoints.get(index);
      }

      // 绘制最后一个点到手指当前位置的连线
      // 如果手指在内圆里面就不要绘制
      boolean isInnerPoint = MathUtil.checkInRound(lastPoint.centerX, lastPoint.centerY,
          mInnerRadius, mMovingX, mMovingY);
      if (!isInnerPoint && mIsTouchPoint) {
        drawLine(lastPoint, new Point((int) mMovingX, (int) mMovingY, -1), canvas, mLinePaint);
      }
    }
  }

  /**
   * 画线
   */
  private void drawLine(Point start, Point end, Canvas canvas, Paint paint) {
    double pointDistance = MathUtil.distance(start.centerX, start.centerY,
        end.centerX, end.centerY);

    int dx = end.centerX - start.centerX;
    int dy = end.centerY - start.centerY;

    double rx = (dx / pointDistance * (mInnerRadius));
    double ry = (dy / pointDistance * (mInnerRadius));
    canvas.drawLine(start.centerX + (float) rx, start.centerY + (float) ry,
        end.centerX - (float) rx, end.centerY - (float) ry, paint);
  }

  /**
   * 画箭头
   */
  private void drawArrow(Canvas canvas, Paint paint, Point start, Point end, Float arrowHeight,
      int angle) {
    double d = MathUtil.distance(start.centerX, start.centerY, end.centerX, end.centerY);
    double sin_B = ((end.centerX - start.centerX) / d);
    double cos_B = ((end.centerY - start.centerY) / d);
    double tan_A = Math.tan(Math.toRadians(angle));
    double h = (d - arrowHeight - mOuterRadius * 1.1);
    double l = arrowHeight * tan_A;
    double a = l * sin_B;
    double b = l * cos_B;
    double x0 = h * sin_B;
    double y0 = h * cos_B;
    double x1 = start.centerX + (h + arrowHeight) * sin_B;
    double y1 = start.centerY + (h + arrowHeight) * cos_B;
    double x2 = start.centerX + x0 - b;
    double y2 = start.centerY + y0 + a;
    double x3 = start.centerX + x0 + b;
    double y3 = start.centerY + y0 - a;
    Path path = new Path();
    path.moveTo((float) x1, (float) y1);
    path.lineTo((float) x2, (float) y2);
    path.lineTo((float) x3, (float) y3);
    path.close();
    canvas.drawPath(path, paint);
  }

  private Point getDownPoint() {
    for (int i = 0; i < mPoints.length; i++) {
      for (int j = 0; j < mPoints[i].length; j++) {
        Point point = mPoints[i][j];
        boolean isPointBounds =
            MathUtil.checkInRound(point.centerX, point.centerY, mOuterRadius, mMovingX, mMovingY);
        if (isPointBounds) {
          return point;
        }
      }
    }
    return null;
  }

  public List<Integer> getSelectArray() {
    List<Integer> ints = new ArrayList<>();
    for (int i = 0; i < mSelectPoints.size(); i++) {
      Point point = mSelectPoints.get(i);
      ints.add(point.index);
    }
    return ints;
  }

  private void setSelectPointStatus(int status) {

    for (int i = 0; i < mSelectPoints.size(); i++) {
      Point point = mSelectPoints.get(i);
      switch (status) {
        case Point.PRESS:
          point.setStatusPress();
          mLinePaint.setColor(mInnerPressedColor);
          mArrowPaint.setColor(mInnerPressedColor);
          break;
        case Point.ERROR:
          point.setStatusError();
          mLinePaint.setColor(mInnerErrorColor);
          mArrowPaint.setColor(mInnerErrorColor);
          break;
        case Point.NORMAL:
          point.setStatusNormal();
          mLinePaint.setColor(mInnerPressedColor);
          mArrowPaint.setColor(mInnerPressedColor);
          break;
        default:
      }
    }
  }
}
