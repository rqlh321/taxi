// Generated code from Butter Knife. Do not modify!
package com.example.taxidriverapp.activity;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.GridView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.example.taxidriverapp.R;
import com.example.taxidriverapp.fragments.AllAreasFragment;

import java.lang.IllegalStateException;
import java.lang.Override;

public class AllAreasFragment_ViewBinding<T extends AllAreasFragment> implements Unbinder {
  protected T target;

  @UiThread
  public AllAreasFragment_ViewBinding(T target, View source) {
    this.target = target;

    target.areasButtonsView = Utils.findRequiredViewAsType(source, R.id.areas_buttons, "field 'areasButtonsView'", GridView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.areasButtonsView = null;

    this.target = null;
  }
}
