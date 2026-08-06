package com.xrevival.gltools26.hook;

import android.os.Build;
import com.xrevival.gltools26.core.DeviceProfile;
import com.xrevival.gltools26.core.UserAgentFactory;

import java.util.HashMap;
import java.util.Map;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public final class BuildPropHooks {
    private static final String SP_CLASS = "android.os.SystemProperties";
    private static final String SECURITY_PATCH = "2026-07-05";
    private static boolean installed = false;

    public static synchronized void install(LoadPackageParam lpparam, DeviceProfile profile) {
        if (installed) return;
        installed = true;
        spoofBuildFields(profile);
        hookSystemProperties(lpparam, expandProps(profile));
    }

    private static void spoofBuildFields(DeviceProfile p) {
        try {
            XposedHelpers.setStaticObjectField(Build.class, "BRAND", p.getBrand());
            XposedHelpers.setStaticObjectField(Build.class, "MANUFACTURER", p.getManufacturer());
            XposedHelpers.setStaticObjectField(Build.class, "MODEL", p.getModel());
            XposedHelpers.setStaticObjectField(Build.class, "DEVICE", p.getDevice());
            XposedHelpers.setStaticObjectField(Build.class, "PRODUCT", p.getProduct());
            XposedHelpers.setStaticObjectField(Build.class, "HARDWARE", p.getHardware());
            XposedHelpers.setStaticObjectField(Build.class, "FINGERPRINT", p.getFingerprint());
            String buildId = UserAgentFactory.extractBuildId(p);
            XposedHelpers.setStaticObjectField(Build.class, "ID", buildId);
            XposedHelpers.setStaticObjectField(Build.class, "DISPLAY", buildId);
            trySetVersionField("RELEASE", UserAgentFactory.extractAndroidVersion(p));
            trySetVersionField("SECURITY_PATCH", SECURITY_PATCH);
            MainModule.log("Build.* spoof -> " + p.getBrand() + "/" + p.getModel());
        } catch (Throwable t) { MainModule.log("spoofBuildFields fallo: " + t); }
    }

    private static void trySetVersionField(String field, String value) {
        try { XposedHelpers.setStaticObjectField(Build.VERSION.class, field, value); }
        catch (Throwable ignored) {}
    }

    private static void hookSystemProperties(LoadPackageParam lpparam, final Map<String, String> props) {
        try {
            Class<?> sp = XposedHelpers.findClass(SP_CLASS, lpparam.classLoader);
            XposedBridge.hookAllMethods(sp, "get", new XC_MethodHook() {
                @Override protected void beforeHookedMethod(MethodHookParam param) {
                    if (!(param.args[0] instanceof String)) return;
                    String v = props.get((String) param.args[0]);
                    if (v != null) param.setResult(v);
                }
            });
            XposedBridge.hookAllMethods(sp, "getInt", new XC_MethodHook() {
                @Override protected void beforeHookedMethod(MethodHookParam param) {
                    if (!(param.args[0] instanceof String)) return;
                    String v = props.get((String) param.args[0]);
                    if (v != null) try { param.setResult(Integer.parseInt(v.trim())); } catch (NumberFormatException ignored) {}
                }
            });
            XposedBridge.hookAllMethods(sp, "getLong", new XC_MethodHook() {
                @Override protected void beforeHookedMethod(MethodHookParam param) {
                    if (!(param.args[0] instanceof String)) return;
                    String v = props.get((String) param.args[0]);
                    if (v != null) try { param.setResult(Long.parseLong(v.trim())); } catch (NumberFormatException ignored) {}
                }
            });
            XposedBridge.hookAllMethods(sp, "getBoolean", new XC_MethodHook() {
                @Override protected void beforeHookedMethod(MethodHookParam param) {
                    if (!(param.args[0] instanceof String)) return;
                    String v = props.get((String) param.args[0]);
                    if (v != null) { String s = v.trim().toLowerCase(); param.setResult("1".equals(s) || "true".equals(s)); }
                }
            });
            MainModule.log("SystemProperties hook OK (" + props.size() + " props)");
        } catch (Throwable t) { MainModule.log("hookSystemProperties fallo: " + t); }
    }

    private static Map<String, String> expandProps(DeviceProfile p) {
        Map<String, String> m = new HashMap<>(p.toBuildProps());
        String buildId = UserAgentFactory.extractBuildId(p);
        String ver = UserAgentFactory.extractAndroidVersion(p);
        m.put("ro.build.id", buildId);
        m.put("ro.build.display.id", buildId);
        m.put("ro.build.version.release_or_codename", ver);
        m.put("ro.build.version.security_patch", SECURITY_PATCH);
        m.put("ro.soc.manufacturer", p.getGpuVendor().equals("MediaTek") ? "MediaTek" : "Qualcomm");
        String[] partitions = {"system", "vendor", "odm", "product"};
        String[] fields = {"brand", "manufacturer", "model", "device", "name"};
        for (String part : partitions) {
            for (String f : fields) {
                String base = m.get("ro.product." + f);
                if (base != null) m.put("ro.product." + part + "." + f, base);
            }
        }
        return m;
    }

    private BuildPropHooks() {}
}
