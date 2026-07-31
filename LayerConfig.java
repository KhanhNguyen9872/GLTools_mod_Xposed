package com.gltools.offline.config;

import android.content.Context;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Gestor de configuración para la capa GLES de depuración.
 * Lee/escribe un archivo INI en el almacenamiento privado de la app.
 */
public class LayerConfig {

    private static final String CONFIG_FILE = "layer_config.ini";
    private final File configFile;
    private final Properties props;

    public LayerConfig(Context context) {
        File dir = new File(context.getExternalFilesDir(null), "gltools_layer");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        this.configFile = new File(dir, CONFIG_FILE);
        this.props = new Properties();
        load();
    }

    private void load() {
        if (configFile.exists()) {
            try (FileInputStream fis = new FileInputStream(configFile)) {
                props.load(fis);
            } catch (IOException e) {
                // Configuración por defecto si el archivo está corrupto.
            }
        }
    }

    public void save() {
        try (FileOutputStream fos = new FileOutputStream(configFile)) {
            props.store(fos, "GLTools Layer Configuration");
        } catch (IOException e) {
            // Fallo silencioso; la capa usará valores por defecto.
        }
    }

    public void setFakeGpu(String gpu) {
        props.setProperty("fake_gpu", gpu);
    }

    public String getFakeGpu() {
        return props.getProperty("fake_gpu", "");
    }

    public void setEnabled(boolean enabled) {
        props.setProperty("enabled", String.valueOf(enabled));
    }

    public boolean isEnabled() {
        return Boolean.parseBoolean(props.getProperty("enabled", "false"));
    }
}
