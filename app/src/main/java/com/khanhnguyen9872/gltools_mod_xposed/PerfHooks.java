package com.xrevival.gltools26.hook;

import android.os.PowerManager;
import com.xrevival.gltools26.core.DeviceProfile;
import com.xrevival.gltools26.core.SpoofConfig;

import java.util.HashMap;
import java.util.Map;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public final class PerfHooks {
    private static final String SP_CLASS = "android.os.SystemProperties";
    private static final String DISPLAY_MODE = "android.view.DisplayMode";
    private static final int THERMAL_STATUS_NONE = 0;
    private static boolean installed = false;

    public static synchronized void install(LoadPackageParam lpparam, DeviceProfile profile, SpoofConfig cfg) {
        if (installed) return;
        installed = true;
        final int fps = effectiveFps(profile, cfg);
        hookThermalStatus();
        hookThermalProps(lpparam, fps);
        hookFpsCeiling(lpparam, fps);
        MainModule.log("Perf hooks -> thermal=NONE | techo FPS=" + fps);
    }

    private static void hookThermalStatus() {
        try {
            XposedBridge.hookAllMethods(PowerManager.class, "getCurrentThermalStatus", new XC_MethodHook() {
                @Override protected void afterHookedMethod(MethodHookParam param) { param.setResult(THERMAL_STATUS_NONE); }
            });
            XposedBridge.hookAllMethods(PowerManager.class, "getThermalStatus", new XC_MethodHook() {
                @Override protected void afterHookedMethod(MethodHookParam param) { param.setResult(THERMAL_STATUS_NONE); }
            });
        } catch (Throwable t) { MainModule.log("hookThermalStatus fallo: " + t); }
    }

    private static void hookThermalProps(LoadPackageParam lpparam, final int fps) {
        final Map<String, String> props = new HashMap<>();
        props.put("sys.thermal.status", "0");
        props.put("vendor.thermal.status", "0");
        props.put("persist.sys.thermal.status", "0");
        props.put("sys.game.fps.limit", "0");
        props.put("persist.vendor.max_fps", String.valueOf(fps));
        try {
            Class<?> sp = XposedHelpers.findClass(SP_CLASS, lpparam.classLoader);
            XposedBridge.hookAllMethods(sp, "get", new XC_MethodHook() {
                @Override protected void beforeHookedMethod(MethodHookParam param) {
                    if (!(param.args[0] instanceof String)) return;
                    String v = props.get((String) param.args[0]);
                    if (v != null) param.setResult(v);
                }
            });
        } catch (Throwable t) { MainModule.log("hookThermalProps fallo: " + t); }
    }

    private static void hookFpsCeiling(LoadPackageParam lpparam, final int fps) {
        try {
            XposedBridge.hookAllMethods(android.view.Display.class, "getRefreshRate", new XC_MethodHook() {
                @Override protected void afterHookedMethod(MethodHookParam param) { param.setResult((float) fps); }
            });
            Class<?> modeClass = XposedHelpers.findClass(DISPLAY_MODE, lpparam.classLoader);
            XposedBridge.hookAllMethods(modeClass, "getRefreshRate", new XC_MethodHook() {
                @Override protected void afterHookedMethod(MethodHookParam param) { param.setResult((float) fps); }
            });
        } catch (Throwable t) { MainModule.log("hookFpsCeiling fallo: " + t); }
    }

    static int effectiveFps(DeviceProfile p, SpoofConfig cfg) {
        if (cfg == null) return p.getRefreshRateHz();
        switch (cfg.getPerfProfile()) {
            case POWERSAVE: return 60;
            case BALANCED: return p.getRefreshRateHz();
            case GAMING: return cfg.isFpsUnlock() ? Math.max(p.getRefreshRateHz(), cfg.getMaxFps()) : p.getRefreshRateHz();
            case APEX: return cfg.isFpsUnlock() ? cfg.getMaxFps() : Math.max(p.getRefreshRateHz(), 144);
            default: return p.getRefreshRateHz();
        }
    }

    private PerfHooks() {}
}
