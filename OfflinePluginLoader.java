package com.gltools.offline.plugin;

import android.content.Context;
import android.content.res.AssetManager;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Gestor de plugins offline. Copia assets embebidos al directorio
 * de trabajo de la aplicación en lugar de descargarlos de servidores
 * de terceros potencialmente inaccesibles o de licencia no clara.
 *
 * Nota: asume que los plugins fueron previamente adquiridos/descargados
 * por el usuario y se incluyen como assets con permiso correspondiente.
 */
public class OfflinePluginLoader {

    private final Context context;
    private final File pluginDir;

    public OfflinePluginLoader(Context context) {
        this.context = context;
        this.pluginDir = new File(context.getFilesDir(), "plugins");
        if (!pluginDir.exists()) {
            pluginDir.mkdirs();
        }
    }

    public boolean ensurePluginsAvailable() {
        String[] pluginAssets = {"default.etcpack", "etc2plugin.so", "astc_decoder.so"};
        for (String asset : pluginAssets) {
            File outFile = new File(pluginDir, asset);
            if (!outFile.exists()) {
                try {
                    copyAsset(asset, outFile);
                } catch (IOException e) {
                    return false;
                }
            }
        }
        return true;
    }

    private void copyAsset(String assetName, File outFile) throws IOException {
        try (InputStream is = context.getAssets().open("plugins/" + assetName);
             FileOutputStream fos = new FileOutputStream(outFile)) {
            byte[] buffer = new byte[8192];
            int read;
            while ((read = is.read(buffer)) != -1) {
                fos.write(buffer, 0, read);
            }
        }
    }
}
