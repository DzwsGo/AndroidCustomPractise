package com.lwang.customviewpractise;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.blankj.utilcode.util.ActivityUtils;
import com.lwang.customviewpractise.activity.FlowLayoutActivity;
import com.lwang.customviewpractise.activity.SlideMenuActivity;
import com.lwang.customviewpractise.activity.TouchActivity;
import com.lwang.customviewpractise.activity.VerticalDragActivity;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  }

  public void flowLayoutClick(View view) {
    ActivityUtils.startActivity(FlowLayoutActivity.class);
  }

  public void touchClick(View view) {
    ActivityUtils.startActivity(TouchActivity.class);
  }

  public void slideMenuClick(View view) {
    ActivityUtils.startActivity(SlideMenuActivity.class);
  }

  public void verticalDragClick(View view) {
    ActivityUtils.startActivity(VerticalDragActivity.class);
  }
}
