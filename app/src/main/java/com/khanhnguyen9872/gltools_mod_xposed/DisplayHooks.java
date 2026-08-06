package com.xrevival.gltools26.hook;

import android.util.DisplayMetrics;
import android.view.Display;
import com.xrevival.gltools26.core.DeviceProfile;
import com.xrevival.gltools26.core.SpoofConfig;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public final class DisplayHooks {
    private static boolean installed = false;

    public static synchronized void install(LoadPackageParam lpparam, DeviceProfile profile, SpoofConfig cfg) {
        if (installed) return;
        installed = true;
        hookDisplayMetrics(lpparam, profile, cfg);
        hookDisplay(lpparam, profile, cfg);
        MainModule.log("Display hooks OK -> " + profile.getWidthPx() + "x" + profile.getHeightPx() + " @" + profile.getRefreshRateHz() + "Hz");
    }

    private static void hookDisplayMetrics(LoadPackageParam lpparam, final DeviceProfile p, final SpoofConfig cfg) {
        try {
            XposedHelpers.findAndHookMethod("android.util.DisplayMetrics", lpparam.classLoader, "setToDefaults", new XC_MethodHook() {
                @Override protected void afterHookedMethod(MethodHookParam param) {
                    DisplayMetrics dm = (DisplayMetrics) param.thisObject;
                    dm.widthPixels = p.getWidthPx();
                    dm.heightPixels = p.getHeightPx();
                    dm.densityDpi = p.getDensityDpi();
                    dm.density = p.getDensityDpi() / 160f;
                    dm.scaledDensity = dm.density;
                    dm.xdpi = p.getDensityDpi();
                    dm.ydpi = p.getDensityDpi();
                }
            });
        } catch (Throwable t) { MainModule.log("hookDisplayMetrics fallo: " + t); }
    }

    private static void hookDisplay(LoadPackageParam lpparam, final DeviceProfile p, final SpoofConfig cfg) {
        try {
            XposedBridge.hookAllMethods(Display.class, "getMetrics", new XC_MethodHook() {
                @Override protected void afterHookedMethod(MethodHookParam param) {
                    if (param.args[0] instanceof DisplayMetrics) {
                        DisplayMetrics dm = (DisplayMetrics) param.args[0];
                        dm.widthPixels = p.getWidthPx();
                        dm.heightPixels = p.getHeightPx();
                        dm.densityDpi = p.getDensityDpi();
                        dm.density = p.getDensityDpi() / 160f;
                        dm.scaledDensity = dm.density;
                    }
                }
            });
            XposedBridge.hookAllMethods(Display.class, "getRealMetrics", new XC_MethodHook() {
                @Override protected void afterHookedMethod(MethodHookParam param) {
                    if (param.args[0] instanceof DisplayMetrics) {
                        DisplayMetrics dm = (DisplayMetrics) param.args[0];
                        dm.widthPixels = p.getWidthPx();
                        dm.heightPixels = p.getHeightPx();
                        dm.densityDpi = p.getDensityDpi();
                        dm.density = p.getDensityDpi() / 160f;
                        dm.scaledDensity = dm.density;
                    }
                }
            });
            XposedBridge.hookAllMethods(Display.class, "getRefreshRate", new XC_MethodHook() {
                @Override protected void afterHookedMethod(MethodHookParam param) {
                    param.setResult((float) p.getRefreshRateHz());
                }
            });
        } catch (Throwable t) { MainModule.log("hookDisplay fallo: " + t); }
    }

    private DisplayHooks() {}
}
