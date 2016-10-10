// Generated code from Butter Knife. Do not modify!
package com.example.taxidriverapp.fragments;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.example.taxidriverapp.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class HistoryFragment_ViewBinding<T extends HistoryFragment> implements Unbinder {
  protected T target;

  @UiThread
  public HistoryFragment_ViewBinding(T target, View source) {
    this.target = target;

    target.recyclerView = Utils.findRequiredViewAsType(source, R.id.history_list, "field 'recyclerView'", RecyclerView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.recyclerView = null;

    this.target = null;
  }
}
