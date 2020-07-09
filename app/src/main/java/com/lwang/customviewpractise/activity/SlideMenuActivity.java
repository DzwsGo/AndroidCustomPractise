package com.lwang.customviewpractise.activity;

import android.os.Bundle;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.blankj.utilcode.util.ToastUtils;
import com.lwang.customviewpractise.R;

/**
 * description：
 *
 * @author: Lwang
 * @createTime: 2020-07-08 14:01
 */
public class SlideMenuActivity extends AppCompatActivity {
  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_slide_menu);
  }

  public void buttonClick(View view) {
    ToastUtils.showShort("buttonClick");
  }
}
