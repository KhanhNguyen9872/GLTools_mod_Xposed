package com.xrevival.gltools26.hook;

import com.xrevival.gltools26.core.DeviceCatalog;
import com.xrevival.gltools26.core.DeviceProfile;
import com.xrevival.gltools26.core.SpoofConfig;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public final class MainModule implements IXposedHookLoadPackage, IXposedHookZygoteInit {
    public static final String TAG = "XRevival";
    public static final String HOST_PACKAGE = "com.xrevival.gltools26";
    private static final String STATUS_CLASS = "com.xrevival.gltools26.core.ModuleStatus";
    private static long zygoteInitTimeMs = -1L;

    @Override
    public void initZygote(StartupParam startupParam) {
        zygoteInitTimeMs = System.currentTimeMillis();
        log("initZygote OK");
    }

    @Override
    public void handleLoadPackage(LoadPackageParam lpparam) {
        if (lpparam == null || lpparam.packageName == null) return;
        final String pkg = lpparam.packageName;
        if ("android".equals(pkg)) return;

        if (HOST_PACKAGE.equals(pkg)) {
            hookModuleStatus(lpparam); return;
        }

        SpoofConfig cfg = safeConfig(lpparam);
        if (cfg != null && !isTargetPackage(pkg, cfg)) return;

        DeviceProfile profile = (cfg != null) ? cfg.getActiveProfile() : DeviceCatalog.defaultProfile();
        log("Spoof activo en [" + pkg + "] -> " + profile.getMarketName());

        if (profile.supportsNativeSpoof()) {
            BuildPropHooks.install(lpparam, profile);
            GLHooks.install(lpparam, profile);
            DisplayHooks.install(lpparam, profile, cfg);
            RamHooks.install(lpparam, profile); // P6
        }
        PerfHooks.install(lpparam, profile, cfg);
    }

    private void hookModuleStatus(LoadPackageParam lpparam) {
        try {
            XposedHelpers.findAndHookMethod(STATUS_CLASS, lpparam.classLoader, "isActive",
                new XC_MethodHook() {
                    @Override protected void afterHookedMethod(MethodHookParam param) { param.setResult(Boolean.TRUE); }
                });
            log("Hook estado OK");
        } catch (Throwable t) { log("ModuleStatus no disponible: " + t.getMessage()); }
    }

    private static SpoofConfig safeConfig(LoadPackageParam lpparam) {
        try {
            android.content.Context ctx = de.robv.android.xposed.AndroidAppHelper.currentApplication();
            if (ctx == null) return null;
            return SpoofConfig.initForHost(ctx, HOST_PACKAGE);
        } catch (Throwable t) { log("Config fallo: " + t.getMessage()); return null; }
    }

    static boolean isTargetPackage(String pkg, SpoofConfig cfg) {
        java.util.Set<String> targets = cfg.getTargetPackages();
        return targets.isEmpty() || targets.contains(pkg);
    }

    public static void log(String msg) { XposedBridge.log("[" + TAG + "] " + msg); }
    public static long getZygoteInitTimeMs() { return zygoteInitTimeMs; }
}
