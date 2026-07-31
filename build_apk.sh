#!/bin/bash
# Script de build para APK standalone parcheado (Rama A).
# Uso: ./build_apk.sh <ruta_al_apk_original>

set -e

APK_ORIGINAL=$1
if [ -z "$APK_ORIGINAL" ]; then
    echo "Uso: $0 <GLTools_1.0_NG.apk>"
    exit 1
fi

# Configuración
APKTOOL=$(which apktool)
KEYSTORE="gltools.keystore"
ALIAS="offline"
OUT_APK="GLTools_offline.apk"
ALIGNED_APK="GLTools_offline_aligned.apk"

echo "[1/5] Decompilando APK original..."
$APKTOOL d "$APK_ORIGINAL" -o gltools_decompiled

echo "[2/5] Inyectando código fuente..."
# Copiar clases Java a la estructura smali correspondiente
# Nota: este paso requiere que las clases estén compiladas a .dex y luego
# convertidas a smali, o inyectadas directamente como .smali.
# Para simplificar, este script asume que el usuario ha preparado los archivos smali.

echo "[3/5] Modificando AndroidManifest.xml..."
cp android/AndroidManifest.xml gltools_decompiled/AndroidManifest.xml

echo "[4/5] Recompilando..."
$APKTOOL b gltools_decompiled -o "$OUT_APK"

echo "[5/5] Firmando y alineando..."
if [ ! -f "$KEYSTORE" ]; then
    keytool -genkey -v -keystore "$KEYSTORE" -alias "$ALIAS" -keyalg RSA -validity 10000
fi
jarsigner -verbose -sigalg SHA1withRSA -digestalg SHA1 -keystore "$KEYSTORE" "$OUT_APK" "$ALIAS"
zipalign -v 4 "$OUT_APK" "$ALIGNED_APK"

echo "Build completado: $ALIGNED_APK"
