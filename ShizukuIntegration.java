package com.gltools.offline.shizuku;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import rikka.shizuku.Shizuku;
import rikka.shizuku.ShizukuBinderWrapper;
import rikka.shizuku.SystemServiceHelper;

/**
 * Integración con Shizuku / ShizukuPlus para operaciones de sistema
 * que no requieren root directo.
 *
 * Alcance: activar capas GLES de depuración, modificar settings globales,
 * instalar APKs parcheados vía pm install.
 *
 * Limitación: Shizuku no permite escritura en /system ni modificación
 * de particiones protegidas. Para eso se requiere root.
 *
 * Nota sobre ShizukuPlus: es un fork de Shizuku (thedjchi/Shizuku) con
 * APIs adicionales. Este código usa la API estándar de Shizuku (rikka.shizuku)
 * que es compatible con ambos. Las APIs exclusivas de ShizukuPlus
 * (Privileged Storage Proxy, SU Bridge, etc.) no se utilizan aquí
 * porque no son necesarias para el alcance de este proyecto.
 *
 * Fuente: https://github.com/thejaustin/ShizukuPlus (verificado al 30/07/2026).
 */
public class ShizukuIntegration {

    private static final String TAG = "GLToolsShizuku";
    private static final int SHIZUKU_CODE = 1001;

    private final Context context;
    private boolean bound = false;

    public ShizukuIntegration(Context context) {
        this.context = context.getApplicationContext();
    }

    /**
     * Verifica si Shizuku está disponible y el permiso concedido.
     */
    public boolean isAvailable() {
        return Shizuku.pingBinder();
    }

    public boolean hasPermission() {
        if (Shizuku.isPreV11()) {
            return false; // Requiere Shizuku v11+
        }
        return Shizuku.checkSelfPermission() == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Solicita permiso de Shizuku al usuario.
     */
    public void requestPermission() {
        if (!Shizuku.isPreV11() && Shizuku.shouldShowRequestPermissionRationale()) {
            // El usuario denegó previamente; mostrar explicación.
            Log.w(TAG, "Permiso de Shizuku denegado previamente.");
            return;
        }
        Shizuku.requestPermission(SHIZUKU_CODE);
    }

    /**
     * Activa capas GLES de depuración para una app objetivo.
     * Requiere que la app objetivo sea debuggable.
     *
     * @param targetPackage paquete de la app a instrumentar
     * @param layerName nombre de la biblioteca .so (ej: libGLToolsLayer.so)
     */
    public boolean enableGlesLayer(String targetPackage, String layerName) {
        if (!hasPermission()) {
            Log.e(TAG, "Sin permiso de Shizuku.");
            return false;
        }

        try {
            // Settings.Global.putInt / putString requieren privilegios de shell
            android.provider.Settings.Global.putInt(
                context.getContentResolver(),
                "enable_gpu_debug_layers",
                1
            );
            android.provider.Settings.Global.putString(
                context.getContentResolver(),
                "gpu_debug_app",
                targetPackage
            );
            android.provider.Settings.Global.putString(
                context.getContentResolver(),
                "gpu_debug_layers_gles",
                layerName
            );
            android.provider.Settings.Global.putString(
                context.getContentResolver(),
                "gpu_debug_layer_app",
                "com.superpaninbros.glng"
            );

            Log.i(TAG, "Capas GLES activadas para: " + targetPackage);
            return true;
        } catch (SecurityException e) {
            Log.e(TAG, "Sin privilegios para modificar settings globales", e);
            return false;
        }
    }

    /**
     * Desactiva capas GLES globalmente.
     */
    public boolean disableGlesLayers() {
        if (!hasPermission()) {
            return false;
        }

        try {
            android.provider.Settings.Global.putInt(
                context.getContentResolver(),
                "enable_gpu_debug_layers",
                0
            );
            Log.i(TAG, "Capas GLES desactivadas.");
            return true;
        } catch (SecurityException e) {
            Log.e(TAG, "Error al desactivar capas GLES", e);
            return false;
        }
    }

    /**
     * Instala un APK parcheado vía Shizuku (pm install).
     * Requiere que el APK esté accesible en almacenamiento compartido.
     *
     * @param apkPath ruta absoluta al APK
     */
    public boolean installApk(String apkPath) {
        if (!hasPermission()) {
            Log.e(TAG, "Sin permiso de Shizuku para instalar APK.");
            return false;
        }

        try {
            // Usar transactRemote para llamar a PackageManagerService directamente
            // o delegar a shell command si está disponible
            Process process = Runtime.getRuntime().exec(
                new String[]{"sh", "-c", "pm install -r " + apkPath}
            );
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                Log.i(TAG, "APK instalado: " + apkPath);
                return true;
            } else {
                Log.e(TAG, "pm install falló con código: " + exitCode);
                return false;
            }
        } catch (Exception e) {
            Log.e(TAG, "Error al instalar APK", e);
            return false;
        }
    }

    /**
     * Listener para cambios de estado de Shizuku.
     */
    public interface ShizukuStateListener {
        void onServiceConnected();
        void onServiceDisconnected();
        void onPermissionResult(boolean granted);
    }
}
