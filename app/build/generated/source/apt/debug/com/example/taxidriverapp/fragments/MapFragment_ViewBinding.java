// Generated code from Butter Knife. Do not modify!
package com.example.taxidriverapp.fragments;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.example.taxidriverapp.R;
import java.lang.IllegalStateException;
import java.lang.Override;
import ru.yandex.yandexmapkit.MapView;

public class MapFragment_ViewBinding<T extends MapFragment> implements Unbinder {
  protected T target;

  @UiThread
  public MapFragment_ViewBinding(T target, View source) {
    this.target = target;

    target.mapView = Utils.findRequiredViewAsType(source, R.id.map, "field 'mapView'", MapView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.mapView = null;

    this.target = null;
  }
}
