package com.lwang.customviewpractise;

import android.app.Application;
import com.blankj.utilcode.util.Utils;

/**
 * description：
 *
 * @author: Lwang
 * @createTime: 2020-07-03 11:25
 */
public class MainApplication extends Application {
  @Override public void onCreate() {
    super.onCreate();
    Utils.init(this);
  }
}
