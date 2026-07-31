# Estrategias de despliegue

> ⚠️ **Advertencia:** la instalación de APKs modificados conlleva riesgo de inestabilidad. Realizar backup completo antes de continuar.

## Opción A: APK standalone parcheado

**Alcance:** UI completa de GLTools, gestión de perfiles, plugins offline.  
**Limitación:** no intercepta OpenGL ES de otras apps (requiere componente de sistema que necesita root).

### Requisitos

- `apktool` instalado
- JDK (para `keytool` y `jarsigner`)
- Android SDK (para `zipalign`)

### Pasos

```bash
# 1. Decompilar APK original
apktool d GLTools_1.0_NG.apk -o gltools_decompiled

# 2. Añadir clases Java del servidor mock y servicio
#    Copiar los archivos generados en android/src/main/java/com/gltools/offline/
#    a la estructura decompilada correspondiente.

# 3. Modificar AndroidManifest.xml para declarar el servicio
#    Ver plantilla en android/AndroidManifest.xml de este repositorio.

# 4. Modificar URLs de red en el código decompilado (smali)
#    Localizar la constante "https://gltools.app" y sustituir por "http://127.0.0.1:8765"
#    Nota: la constante exacta depende de la versión del APK.

# 5. Recompilar
apktool b gltools_decompiled -o GLTools_offline.apk

# 6. Firmar
keytool -genkey -v -keystore gltools.keystore -alias offline -keyalg RSA -validity 10000
jarsigner -verbose -sigalg SHA1withRSA -digestalg SHA1 -keystore gltools.keystore GLTools_offline.apk offline
zipalign -v 4 GLTools_offline.apk GLTools_offline_aligned.apk
```

## Opción B: LSPatch (inyección sin root)

**Alcance:** hooks de red en el proceso de GLTools.  
**Limitación:** LSPatch fue archivado en diciembre de 2023; soporte para Android 14 es teórico, no garantizado.

### Requisitos

- LSPatch Manager instalado en el dispositivo.
- Módulo Xposed que haga hook de `ApiClient` y `VersionChecker` (no de `CoinsManager`).

### Pasos

1. Instalar LSPatch Manager.
2. Crear un módulo Xposed con hooks de red únicamente.
3. En LSPatch Manager, seleccionar APK original de GLTools y el módulo.
4. Generar APK parcheado. No se actualiza vía Google Play.

## Opción C: Shizuku + ADB

**Alcance:** ajustes de sistema limitados (activar capas GLES para apps debuggables).  
**Limitación:** no permite escritura en `/system` ni instalación de drivers gráficos de sistema.

### Activación de capas GLES

```bash
adb shell settings put global enable_gpu_debug_layers 1
adb shell settings put global gpu_debug_app com.ejemplo.juego
adb shell settings put global gpu_debug_layers_gles libGLToolsLayer.so
adb shell settings put global gpu_debug_layer_app com.superpaninbros.glng
```

## Matriz de decisión

| Escenario | Root | ADB | Alcance técnico | Complejidad |
|---|---|---|---|---|
| APK standalone | No | No | UI, perfiles, plugins offline | Baja |
| LSPatch + módulo | No | No | UI + hooks de red | Media |
| Shizuku + ADB | No | Sí (1ª vez) | UI + capas GLES limitadas | Media |
| Root + sistema | Sí | No | Funcionalidad completa original | Alta |

## Nota sobre la firma

El APK recompilado debe firmarse con un keystore propio. No es posible usar la firma original del desarrollador sin su clave privada. Esto implica que el APK modificado no se actualizará por Google Play y debe tratarse como una instalación independiente.
