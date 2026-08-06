package com.xrevival.gltools26.core;

import java.io.File;

public final class ModuleStatus {
    public static final String MODULE_VERSION = "2026.1.0-xrevival";
    public static final String HOST_PACKAGE = "com.xrevival.gltools26";

    public static boolean isActive() { return false; }

    public static boolean isRootAvailable() {
        String[] paths = {"/system/bin/su", "/system/xbin/su", "/sbin/su", "/apex/com.android.runtime/bin/su", "/data/adb/magisk/su"};
        for (String p : paths) if (new File(p).exists()) return true;
        return false;
    }

    public static String describe() {
        if (isActive()) return "Modulo ACTIVO . " + MODULE_VERSION;
        return "Modulo INACTIVO . marca la app en LSPosed" + (isRootAvailable() ? " (root detectado: modo Magisk disponible)" : "");
    }

    private ModuleStatus() {}
}
