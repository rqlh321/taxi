// Generated code from Butter Knife. Do not modify!
package com.example.taxidriverapp.activity;

import android.support.annotation.UiThread;
import android.view.View;
import butterknife.internal.Utils;
import com.example.taxidriverapp.R;
import com.example.taxidriverapp.fragments.MapFragment;

import java.lang.Override;
import ru.yandex.yandexmapkit.MapView;

public class MapFragment_ViewBinding<T extends MapFragment> extends AllAreasFragment_ViewBinding<T> {
  @UiThread
  public MapFragment_ViewBinding(T target, View source) {
    super(target, source);

    target.mapView = Utils.findRequiredViewAsType(source, R.id.map, "field 'mapView'", MapView.class);
  }

  @Override
  public void unbind() {
    T target = this.target;
    super.unbind();

    target.mapView = null;
  }
}
