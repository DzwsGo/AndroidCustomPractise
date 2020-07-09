package com.lwang.customviewpractise.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.lwang.customviewpractise.R;
import java.util.ArrayList;

/**
 * description：
 *
 * @author: Lwang
 * @createTime: 2020-07-09 10:43
 */
public class VerticalDragActivity extends AppCompatActivity {

  private ListView mListView;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_vertical_drag);
    mListView = findViewById(R.id.listview);
    final ArrayList<String> strings = new ArrayList<String>();

    for (int i = 0; i < 20;i++) {
      strings.add(i + "");
    }

    mListView.setAdapter(new BaseAdapter() {
      @Override
      public int getCount() {
        return strings.size();
      }

      @Override
      public Object getItem(int position) {
        return null;
      }

      @Override
      public long getItemId(int position) {
        return 0;
      }

      @Override
      public View getView(int position, View convertView, ViewGroup parent) {
        TextView item = (TextView) LayoutInflater.from(VerticalDragActivity.this)
            .inflate(R.layout.item_lv, parent, false);
        item.setText(strings.get(position));
        return item;
      }
    });
  }

}
