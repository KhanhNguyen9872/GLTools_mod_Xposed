package com.xrevival.gltools26.hook;

import android.app.ActivityManager;
import com.xrevival.gltools26.core.DeviceProfile;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public final class RamHooks {
    private static boolean installed = false;

    public static synchronized void install(LoadPackageParam lpparam, DeviceProfile profile) {
        if (installed) return;
        installed = true;
        hookMemoryInfo(profile);
        hookRuntimeMemory(profile);
        MainModule.log("RAM spoof -> " + profile.getRamMb() + " MB");
    }

    private static void hookMemoryInfo(final DeviceProfile p) {
        try {
            XposedBridge.hookAllMethods(ActivityManager.class, "getMemoryInfo", new XC_MethodHook() {
                @Override protected void afterHookedMethod(MethodHookParam param) {
                    if (param.args[0] instanceof ActivityManager.MemoryInfo) {
                        ActivityManager.MemoryInfo mi = (ActivityManager.MemoryInfo) param.args[0];
                        long totalBytes = (long) p.getRamMb() * 1024L * 1024L;
                        long availBytes = (long) (totalBytes * 0.70);
                        mi.totalMem = totalBytes;
                        mi.availMem = availBytes;
                        mi.lowMemory = false;
                        mi.threshold = 512L * 1024L * 1024L;
                    }
                }
            });
            MainModule.log("getMemoryInfo hook OK -> " + p.getRamMb() + "MB total");
        } catch (Throwable t) { MainModule.log("hookMemoryInfo fallo: " + t); }
    }

    private static void hookRuntimeMemory(final DeviceProfile p) {
        try {
            XposedBridge.hookAllMethods(Runtime.class, "totalMemory", new XC_MethodHook() {
                @Override protected void afterHookedMethod(MethodHookParam param) {
                    param.setResult(((long) p.getRamMb() * 1024L * 1024L) / 4);
                }
            });
            XposedBridge.hookAllMethods(Runtime.class, "freeMemory", new XC_MethodHook() {
                @Override protected void afterHookedMethod(MethodHookParam param) {
                    param.setResult(((long) p.getRamMb() * 1024L * 1024L) / 8);
                }
            });
            XposedBridge.hookAllMethods(Runtime.class, "maxMemory", new XC_MethodHook() {
                @Override protected void afterHookedMethod(MethodHookParam param) {
                    param.setResult(((long) p.getRamMb() * 1024L * 1024L) / 4);
                }
            });
            MainModule.log("Runtime memory hooks OK");
        } catch (Throwable t) { MainModule.log("hookRuntimeMemory fallo: " + t); }
    }

    private RamHooks() {}
}
