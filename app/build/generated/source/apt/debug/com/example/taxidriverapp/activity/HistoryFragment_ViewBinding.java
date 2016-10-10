// Generated code from Butter Knife. Do not modify!
package com.example.taxidriverapp.activity;

import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import butterknife.internal.Utils;
import com.example.taxidriverapp.R;
import com.example.taxidriverapp.fragments.HistoryFragment;

import java.lang.Override;

public class HistoryFragment_ViewBinding<T extends HistoryFragment> extends AllAreasFragment_ViewBinding<T> {
  @UiThread
  public HistoryFragment_ViewBinding(T target, View source) {
    super(target, source);

    target.recyclerView = Utils.findRequiredViewAsType(source, R.id.history_list, "field 'recyclerView'", RecyclerView.class);
  }

  @Override
  public void unbind() {
    T target = this.target;
    super.unbind();

    target.recyclerView = null;
  }
}
