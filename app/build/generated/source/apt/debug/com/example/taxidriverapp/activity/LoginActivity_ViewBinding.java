// Generated code from Butter Knife. Do not modify!
package com.example.taxidriverapp.activity;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.EditText;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.example.taxidriverapp.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class LoginActivity_ViewBinding<T extends LoginActivity> implements Unbinder {
  protected T target;

  private View view2131624046;

  @UiThread
  public LoginActivity_ViewBinding(final T target, View source) {
    this.target = target;

    View view;
    target.mUserNameView = Utils.findRequiredViewAsType(source, R.id.userName, "field 'mUserNameView'", EditText.class);
    target.mPasswordView = Utils.findRequiredViewAsType(source, R.id.password, "field 'mPasswordView'", EditText.class);
    target.mProgressView = Utils.findRequiredView(source, R.id.login_progress, "field 'mProgressView'");
    target.mLoginFormView = Utils.findRequiredView(source, R.id.login_form, "field 'mLoginFormView'");
    view = Utils.findRequiredView(source, R.id.sign_in_button, "method 'signIn'");
    view2131624046 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.signIn();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.mUserNameView = null;
    target.mPasswordView = null;
    target.mProgressView = null;
    target.mLoginFormView = null;

    view2131624046.setOnClickListener(null);
    view2131624046 = null;

    this.target = null;
  }
}
