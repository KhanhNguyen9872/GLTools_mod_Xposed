package com.gltools.offline.xposed;

import android.util.Log;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Módulo Xposed/LSPosed de red neutral para GLTools.
 *
 * Propósito: interceptar peticiones de red a gltools.app y redirigirlas
 * a un servidor mock local, evitando crashes por servidor caído.
 *
 * Alcance: redirección de URLs, inyección de respuestas de versión.
 * Fuera de alcance: modificación de CoinsManager, verificación de licencia,
 * o cualquier método relacionado con estado de compra.
 *
 * Advertencia: requiere LSPosed con scope en com.superpaninbros.glng.
 * No garantiza estabilidad en versiones futuras de Android o GLTools.
 */
public class GLToolsNetworkModule implements IXposedHookLoadPackage {

    private static final String TAG = "GLToolsNetModule";
    private static final String TARGET_PACKAGE = "com.superpaninbros.glng";
    private static final String MOCK_HOST = "http://127.0.0.1:8765";

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if (!TARGET_PACKAGE.equals(lpparam.packageName)) {
            return;
        }

        Log.i(TAG, "GLTools detectado. Inyectando hooks de red neutrales.");

        // Hook 1: Interceptar construcción de URLs de API
        try {
            XposedHelpers.findAndHookMethod(
                "java.net.URL",
                lpparam.classLoader,
                "<init>",
                String.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        String url = (String) param.args[0];
                        if (url != null && url.contains("gltools.app")) {
                            String redirected = url.replace("https://gltools.app", MOCK_HOST)
                                                   .replace("http://gltools.app", MOCK_HOST);
                            param.args[0] = redirected;
                            Log.d(TAG, "URL redirigida: " + url + " -> " + redirected);
                        }
                    }
                }
            );
        } catch (Exception e) {
            Log.w(TAG, "No se pudo hookar java.net.URL: " + e.getMessage());
        }

        // Hook 2: Interceptar respuestas de version.js para evitar force-update
        try {
            Class<?> versionChecker = XposedHelpers.findClass(
                "com.superpaninbros.glng.update.VersionChecker",
                lpparam.classLoader
            );
            XposedHelpers.findAndHookMethod(
                versionChecker,
                "checkVersion",
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        // Si el método devuelve un objeto JSON o String, inyectar versión segura
                        String safeVersion = "{\"version\":\"1.99\",\"force_update\":false," +
                                           "\"min_android\":16,\"max_android\":34}";
                        param.setResult(safeVersion);
                        Log.d(TAG, "Versión inyectada para evitar force-update.");
                    }
                }
            );
        } catch (Exception e) {
            Log.w(TAG, "No se pudo hookar VersionChecker: " + e.getMessage());
        }

        // Hook 3: Silenciar peticiones a companion (no altera estado de licencia)
        try {
            XposedHelpers.findAndHookMethod(
                "com.superpaninbros.glng.network.ApiClient",
                lpparam.classLoader,
                "fetchCompanionStatus",
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        param.setResult(null); // Evita crash por servidor muerto
                        Log.d(TAG, "Companion request silenciado.");
                    }
                }
            );
        } catch (Exception e) {
            Log.w(TAG, "No se pudo hookar ApiClient.fetchCompanionStatus: " + e.getMessage());
        }

        Log.i(TAG, "Hooks de red inyectados. Licencia NO modificada.");
    }
}
