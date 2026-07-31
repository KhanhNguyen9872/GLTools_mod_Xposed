package com.gltools.offline.diagnostic;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import java.io.File;
import java.util.Map;

/**
 * Utilidad de diagnóstico para verificar el estado de licencia local
 * antes de aplicar cualquier modificación.
 *
 * Resultado: si las preferencias locales indican compra previa,
 * el problema probable es solo de red (servidor caído).
 * Si no hay rastro local, este diagnóstico no puede restaurarlo.
 */
public class LicenseDiagnostics {

    private static final String TAG = "GLToolsDiag";
    private static final String[] PREFS_FILES = {
        "com.superpaninbros.glng_preferences",
        "prefs",
        "settings"
    };
    private static final String[] KEYS_TO_CHECK = {
        "premium", "purchased", "coins", "license", "sku", "is_pro"
    };

    private final Context context;

    public LicenseDiagnostics(Context context) {
        this.context = context;
    }

    public DiagnosticResult run() {
        DiagnosticResult result = new DiagnosticResult();

        // Escaneo de SharedPreferences conocidas
        for (String prefsName : PREFS_FILES) {
            try {
                SharedPreferences prefs = context.getSharedPreferences(
                    prefsName, Context.MODE_PRIVATE
                );
                for (String key : KEYS_TO_CHECK) {
                    if (prefs.contains(key)) {
                        Object value = prefs.getAll().get(key);
                        result.addFinding(prefsName, key, value);
                    }
                }
            } catch (Exception e) {
                Log.w(TAG, "No se pudo leer: " + prefsName);
            }
        }

        // Escaneo de archivos XML en shared_prefs (si run-as o root disponible)
        File sharedPrefsDir = new File(
            context.getApplicationInfo().dataDir, "shared_prefs"
        );
        if (sharedPrefsDir.exists() && sharedPrefsDir.isDirectory()) {
            File[] files = sharedPrefsDir.listFiles();
            if (files != null) {
                for (File f : files) {
                    if (f.getName().endsWith(".xml")) {
                        result.addFile(f.getAbsolutePath());
                    }
                }
            }
        }

        return result;
    }

    public static class DiagnosticResult {
        private final StringBuilder findings = new StringBuilder();
        private boolean licenseFound = false;

        void addFinding(String file, String key, Object value) {
            findings.append(file).append(" -> ")
                    .append(key).append(" = ").append(value).append("\n");
            licenseFound = true;
        }

        void addFile(String path) {
            findings.append("Archivo encontrado: ").append(path).append("\n");
        }

        public boolean isLicenseFound() {
            return licenseFound;
        }

        @Override
        public String toString() {
            return findings.toString();
        }
    }
}
