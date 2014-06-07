package com.jakewharton.u2020.data.prefs;

import com.jakewharton.u2020.data.ApiEndpoints;

import hrisey.Preferences;

@Preferences
public final class DebugU2020Prefs {

  private String apiEndpoint = ApiEndpoints.MOCK_MODE.url;
  private String networkProxy = null;

  private int animationSpeed = 1;
  private boolean seenDebugDrawer = false;
  private boolean pixelGridEnabled = false;
  private boolean pixelRatioEnabled = false;
  private boolean scalpelEnabled = false;
  private boolean scalpelWireframeEnabled = false;
  private boolean picassoDebugging = false;
}
