// Generated code from Butter Knife. Do not modify!
package com.example.taxidriverapp.activity;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.view.ViewStub;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.example.taxidriverapp.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class MainFragment_ViewBinding<T extends MainFragment> implements Unbinder {
  protected T target;

  @UiThread
  public MainFragment_ViewBinding(T target, View source) {
    this.target = target;

    target.stub = Utils.findRequiredViewAsType(source, R.id.layout_stub, "field 'stub'", ViewStub.class);
    target.stubButtons = Utils.findRequiredViewAsType(source, R.id.buttons_stub, "field 'stubButtons'", ViewStub.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.stub = null;
    target.stubButtons = null;

    this.target = null;
  }
}
