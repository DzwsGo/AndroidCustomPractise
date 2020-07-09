package com.lwang.customviewpractise.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.lwang.customviewpractise.R;

/**
 * description：
 *
 * @author: Lwang
 * @createTime: 2020-07-07 14:04
 */
public class TouchActivity extends AppCompatActivity {
  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_touch);
    ImageView ivTouch = findViewById(R.id.iv_touch);
    ivTouch.setOnTouchListener(new View.OnTouchListener() {
      @Override public boolean onTouch(View v, MotionEvent event) {
        Log.d("TouchView","view onTouch action : " + event.getAction());
        return false;
      }
    });
    ivTouch.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        Log.d("TouchView","view onClick ");
      }
    });
  }
}
