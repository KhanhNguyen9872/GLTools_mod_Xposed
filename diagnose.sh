#!/bin/bash
# Script de diagnóstico remoto vía ADB.
# No modifica el dispositivo; solo extrae y muestra información.

PACKAGE="com.superpaninbros.glng"

echo "=== Diagnóstico GLTools Offline ==="
echo ""

echo "[1] Estado de instalación:"
adb shell pm list packages | grep glng || echo "No instalado"

echo ""
echo "[2] Procesos en ejecución:"
adb shell ps | grep glng || echo "No en ejecución"

echo ""
echo "[3] Logcat reciente (GLToolsOffline):"
adb logcat -d -s GLToolsOffline:D *:S | tail -n 20

echo ""
echo "[4] Puerto del servidor mock:"
adb shell netstat -tlnp 2>/dev/null | grep 8765 || echo "Puerto 8765 no detectado"

echo ""
echo "[5] Capas GLES activas:"
adb shell settings get global enable_gpu_debug_layers
adb shell settings get global gpu_debug_app
adb shell settings get global gpu_debug_layers_gles

echo ""
echo "[6] Conectividad (no debe mostrar conexiones a gltools.app):"
adb shell dumpsys connectivity | grep -A 5 gltools || echo "Sin tráfico detectado a gltools.app"

echo ""
echo "Diagnóstico completado."
