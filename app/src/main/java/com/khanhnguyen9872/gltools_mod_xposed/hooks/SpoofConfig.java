package com.xrevival.gltools26.core;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

public final class SpoofConfig {
    private static final String PREFS_NAME = "xrevival_prefs";

    private static final String KEY_DEVICE_ID = "device_id";
    private static final String KEY_FORCE_DESKTOP = "force_desktop_viewport";
    private static final String KEY_UA_ENABLED = "custom_ua_enabled";
    private static final String KEY_UA_CUSTOM = "custom_ua";
    private static final String KEY_AUTO_START = "auto_start";
    private static final String KEY_CLEAR_CACHE = "clear_webview_cache";
    private static final String KEY_INJECT_CSS = "inject_css";
    private static final String KEY_CUSTOM_CSS = "custom_css";
    private static final String KEY_INJECT_JS = "inject_js";
    private static final String KEY_CUSTOM_JS = "custom_js";
    private static final String KEY_THERMAL_BYPASS = "thermal_bypass";
    private static final String KEY_FPS_UNLOCK = "fps_unlock";
    private static final String KEY_MAX_FPS = "max_fps";
    private static final String KEY_PERF_PROFILE = "perf_profile";
    private static final String KEY_SPOOF_MODE = "spoof_mode";
    private static final String KEY_DARK_MODE = "dark_mode";
    private static final String KEY_TARGET_PACKAGES = "target_packages";

    public enum PerfProfile { BALANCED, GAMING, APEX, POWERSAVE }
    public enum SpoofMode { XPOSED, MAGISK, SHIZUKU, BOTH }

    public interface OnConfigChangedListener {
        void onConfigChanged(String key);
    }

    private static volatile SpoofConfig INSTANCE;
    private final SharedPreferences prefs;
    private final List<OnConfigChangedListener> listeners = new CopyOnWriteArrayList<>();

    private SpoofConfig(Context ctx) { this.prefs = ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE); }

    public static SpoofConfig init(Context appContext) {
        if (INSTANCE == null) {
            synchronized (SpoofConfig.class) {
                if (INSTANCE == null) INSTANCE = new SpoofConfig(appContext.getApplicationContext());
            }
        }
        return INSTANCE;
    }

    public static SpoofConfig initForHost(Context hookedCtx, String hostPackage) {
        try {
            Context host = hookedCtx.createPackageContext(hostPackage, Context.CONTEXT_IGNORE_SECURITY);
            return init(host);
        } catch (Exception e) { return init(hookedCtx); }
    }

    public static SpoofConfig get() {
        if (INSTANCE == null) throw new IllegalStateException("SpoofConfig.init() primero");
        return INSTANCE;
    }

    public String getDeviceId() { return prefs.getString(KEY_DEVICE_ID, DeviceCatalog.defaultProfile().getId()); }
    public void setDeviceId(String id) {
        if (id == null || DeviceCatalog.byId(id) == null) return;
        prefs.edit().putString(KEY_DEVICE_ID, id).apply(); notifyChanged(KEY_DEVICE_ID);
    }
    public DeviceProfile getActiveProfile() {
        DeviceProfile p = DeviceCatalog.byId(getDeviceId());
        return p != null ? p : DeviceCatalog.defaultProfile();
    }

    public boolean isForceDesktopViewport() { return prefs.getBoolean(KEY_FORCE_DESKTOP, false); }
    public void setForceDesktopViewport(boolean v) { prefs.edit().putBoolean(KEY_FORCE_DESKTOP, v).apply(); notifyChanged(KEY_FORCE_DESKTOP); }
    public boolean isCustomUserAgentEnabled() { return prefs.getBoolean(KEY_UA_ENABLED, false); }
    public void setCustomUserAgentEnabled(boolean v) { prefs.edit().putBoolean(KEY_UA_ENABLED, v).apply(); notifyChanged(KEY_UA_ENABLED); }
    public String getCustomUserAgent() { return prefs.getString(KEY_UA_CUSTOM, ""); }
    public void setCustomUserAgent(String ua) { prefs.edit().putString(KEY_UA_CUSTOM, ua == null ? "" : ua).apply(); notifyChanged(KEY_UA_CUSTOM); }
    public boolean isAutoStart() { return prefs.getBoolean(KEY_AUTO_START, false); }
    public void setAutoStart(boolean v) { prefs.edit().putBoolean(KEY_AUTO_START, v).apply(); notifyChanged(KEY_AUTO_START); }
    public boolean isClearWebViewCache() { return prefs.getBoolean(KEY_CLEAR_CACHE, false); }
    public void setClearWebViewCache(boolean v) { prefs.edit().putBoolean(KEY_CLEAR_CACHE, v).apply(); notifyChanged(KEY_CLEAR_CACHE); }
    public boolean isInjectCss() { return prefs.getBoolean(KEY_INJECT_CSS, false); }
    public void setInjectCss(boolean v) { prefs.edit().putBoolean(KEY_INJECT_CSS, v).apply(); notifyChanged(KEY_INJECT_CSS); }
    public String getCustomCss() { return prefs.getString(KEY_CUSTOM_CSS, ""); }
    public void setCustomCss(String css) { prefs.edit().putString(KEY_CUSTOM_CSS, css == null ? "" : css).apply(); notifyChanged(KEY_CUSTOM_CSS); }
    public boolean isInjectJs() { return prefs.getBoolean(KEY_INJECT_JS, false); }
    public void setInjectJs(boolean v) { prefs.edit().putBoolean(KEY_INJECT_JS, v).apply(); notifyChanged(KEY_INJECT_JS); }
    public String getCustomJs() { return prefs.getString(KEY_CUSTOM_JS, ""); }
    public void setCustomJs(String js) { prefs.edit().putString(KEY_CUSTOM_JS, js == null ? "" : js).apply(); notifyChanged(KEY_CUSTOM_JS); }
    public boolean isDarkMode() { return prefs.getBoolean(KEY_DARK_MODE, true); }
    public void setDarkMode(boolean v) { prefs.edit().putBoolean(KEY_DARK_MODE, v).apply(); notifyChanged(KEY_DARK_MODE); }

    public boolean isThermalBypass() { return prefs.getBoolean(KEY_THERMAL_BYPASS, false); }
    public void setThermalBypass(boolean v) { prefs.edit().putBoolean(KEY_THERMAL_BYPASS, v).apply(); notifyChanged(KEY_THERMAL_BYPASS); }
    public boolean isFpsUnlock() { return prefs.getBoolean(KEY_FPS_UNLOCK, false); }
    public void setFpsUnlock(boolean v) { prefs.edit().putBoolean(KEY_FPS_UNLOCK, v).apply(); notifyChanged(KEY_FPS_UNLOCK); }
    public int getMaxFps() { return prefs.getInt(KEY_MAX_FPS, 144); }
    public void setMaxFps(int fps) { prefs.edit().putInt(KEY_MAX_FPS, Math.max(30, Math.min(240, fps))).apply(); notifyChanged(KEY_MAX_FPS); }
    public PerfProfile getPerfProfile() {
        try { return PerfProfile.valueOf(prefs.getString(KEY_PERF_PROFILE, PerfProfile.BALANCED.name())); }
        catch (IllegalArgumentException e) { return PerfProfile.BALANCED; }
    }
    public void setPerfProfile(PerfProfile p) { prefs.edit().putString(KEY_PERF_PROFILE, (p == null ? PerfProfile.BALANCED : p).name()).apply(); notifyChanged(KEY_PERF_PROFILE); }

    public SpoofMode getSpoofMode() {
        try { return SpoofMode.valueOf(prefs.getString(KEY_SPOOF_MODE, SpoofMode.XPOSED.name())); }
        catch (IllegalArgumentException e) { return SpoofMode.XPOSED; }
    }
    public void setSpoofMode(SpoofMode m) { prefs.edit().putString(KEY_SPOOF_MODE, (m == null ? SpoofMode.XPOSED : m).name()).apply(); notifyChanged(KEY_SPOOF_MODE); }

    public Set<String> getTargetPackages() { return Collections.unmodifiableSet(new HashSet<>(prefs.getStringSet(KEY_TARGET_PACKAGES, Collections.<String>emptySet()))); }
    public void setTargetPackages(Set<String> packages) { prefs.edit().putStringSet(KEY_TARGET_PACKAGES, packages == null ? Collections.<String>emptySet() : packages).apply(); notifyChanged(KEY_TARGET_PACKAGES); }
    public void addTargetPackage(String pkg) {
        if (pkg == null || pkg.isEmpty()) return;
        Set<String> set = new HashSet<>(getTargetPackages()); if (set.add(pkg)) setTargetPackages(set);
    }
    public void removeTargetPackage(String pkg) {
        Set<String> set = new HashSet<>(getTargetPackages()); if (set.remove(pkg)) setTargetPackages(set);
    }

    public void reset() { prefs.edit().clear().apply(); notifyChanged(null); }

    public String toSummary() {
        DeviceProfile p = getActiveProfile();
        return "XRevival Config:
  perfil=" + p.getMarketName() + " (" + p.getId() + ")
  viewportEscritorio=" + isForceDesktopViewport() + "
  uaCustom=" + isCustomUserAgentEnabled() + "
  autoInicio=" + isAutoStart() + "
  limpiarCache=" + isClearWebViewCache() + "
  css=" + isInjectCss() + " js=" + isInjectJs() + "
  thermalBypass=" + isThermalBypass() + "
  fpsUnlock=" + isFpsUnlock() + " maxFps=" + getMaxFps() + "
  perfProfile=" + getPerfProfile() + "
  spoofMode=" + getSpoofMode() + "
  targets=" + getTargetPackages();
    }

    public void addListener(OnConfigChangedListener l) { if (l != null && !listeners.contains(l)) listeners.add(l); }
    public void removeListener(OnConfigChangedListener l) { listeners.remove(l); }
    private void notifyChanged(String key) { for (OnConfigChangedListener l : listeners) try { l.onConfigChanged(key); } catch (Exception ignored) {} }
}
