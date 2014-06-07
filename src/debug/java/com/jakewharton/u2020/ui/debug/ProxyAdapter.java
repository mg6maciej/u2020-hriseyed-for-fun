package com.jakewharton.u2020.ui.debug;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.jakewharton.u2020.data.prefs.DebugU2020Prefs;
import com.jakewharton.u2020.ui.misc.BindableAdapter;

import static butterknife.ButterKnife.findById;

class ProxyAdapter extends BindableAdapter<String> {
  public static final int NONE = 0;
  public static final int PROXY = 1;

  private final DebugU2020Prefs prefs;

  ProxyAdapter(Context context, DebugU2020Prefs prefs) {
    super(context);
    if (prefs == null) {
      throw new IllegalStateException("prefs == null");
    }
    this.prefs = prefs;
  }

  @Override public int getCount() {
    return 2 /* "None" and "Set" */ + (prefs.containsNetworkProxy() ? 1 : 0);
  }

  @Override public String getItem(int position) {
    if (position == 0) {
      return "None";
    }
    if (position == getCount() - 1) {
      return "Set…";
    }
    return prefs.getNetworkProxy();
  }

  @Override public long getItemId(int position) {
    return position;
  }

  @Override public View newView(LayoutInflater inflater, int position, ViewGroup container) {
    return inflater.inflate(android.R.layout.simple_spinner_item, container, false);
  }

  @Override public void bindView(String item, int position, View view) {
    TextView tv = findById(view, android.R.id.text1);
    tv.setText(item);
  }

  @Override
  public View newDropDownView(LayoutInflater inflater, int position, ViewGroup container) {
    return inflater.inflate(android.R.layout.simple_spinner_dropdown_item, container, false);
  }
}
