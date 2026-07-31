# Vías de recuperación y troubleshooting

> ⚠️ **Advertencia:** si el sistema gráfico se vuelve inestable, actuar de inmediato. No reiniciar repetidamente si hay indicios de bootloop.

## Si la app modificada no instala

1. Desinstalar GLTools original:
   ```bash
   adb uninstall com.superpaninbros.glng
   ```
2. Verificar firma del APK modificado:
   ```bash
   apksigner verify -v GLTools_offline_aligned.apk
   ```
3. Reinstalar.

## Si la app crashea al iniciar

1. Obtener stack trace:
   ```bash
   adb logcat | grep AndroidRuntime
   ```
2. Verificar que el servicio mock está declarado en `AndroidManifest.xml`.
3. Si el crash es nativo (`SIGSEGV`), revisar arquitectura de las bibliotecas nativas incluidas (`arm64-v8a` vs `armeabi-v7a`).

## Si las capas GLES no cargan

1. Verificar que la app objetivo tiene `android:debuggable="true"`.
2. Confirmar que `libGLToolsLayer.so` está en la ruta correcta dentro del APK objetivo.
3. Revisar logcat por mensajes de `GLES LayerLoader`.
4. Desactivar capas globalmente:
   ```bash
   adb shell settings delete global enable_gpu_debug_layers
   adb shell settings delete global gpu_debug_app
   adb shell settings delete global gpu_debug_layers_gles
   adb shell settings delete global gpu_debug_layer_app
   ```

## Reversión completa

1. Desinstalar GLTools modificado.
2. Ejecutar limpieza de settings de capas GLES (comandos arriba).
3. Si se instaló un módulo de sistema (solo con root), flashear `uninstall_gltools.zip` desde recovery.
4. Reiniciar el dispositivo.

## Métricas de validación post-instalación

| Métrica | Criterio | Método de verificación |
|---|---|---|
| Tiempo de lanzamiento | < 3 s desde tap hasta UI principal | Cronómetro o systrace |
| Fallos de red | 0 requests fallidos a gltools.app | Proxy local / logcat |
| Estabilidad de capa GLES | 0 crashes en 10 min de uso | Logcat + monkey test |
