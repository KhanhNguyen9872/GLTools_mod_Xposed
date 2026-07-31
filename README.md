# Rama A — OpenSource/Legal

**Propósito:** Eliminar la dependencia de los servidores `gltools.app` para un dispositivo que ya cuenta con GLTools legítimamente adquirido.

**Alcance:**
- Mock local de endpoints de red no relacionados con licencia (`version.js`, `companion`).
- Emulación de capas GLES para apps debuggables mediante el sistema oficial de Android.
- Gestión offline de perfiles y plugins previamente descargados.
- Diagnóstico del estado de licencia local sin modificación.

**Qué NO incluye:**
- Parches a `CoinsManager.isPremium()` o cualquier método de verificación de compra.
- Falsificación de recibos de Google Play Billing.
- Redistribución del APK original de GLTools.

## Fases de implementación

| Fase | Contenido | Ubicación |
|---|---|---|
| 1 | Diagnóstico de estado de licencia local | `docs/DIAGNOSTICO.md` |
| 2 | Servidor mock embebido (NanoHTTPD) | `android/.../server/` |
| 3 | Estrategias de despliegue sin root | `docs/DESPLIEGUE.md` |
| 4 | Capa GLES para apps debuggables | `android/src/main/cpp/` |
| 5 | Gestión offline de plugins y perfiles | `android/.../plugin/`, `android/.../db/` |
| 6 | Reversión y troubleshooting | `docs/REVERSION.md` |

## Requisitos previos

- APK original de GLTools (no se proporciona en este repositorio).
- `apktool`, `jadx`, `adb` configurados en el entorno de build.
- Para capas GLES: Android NDK y CMake.
- Para diagnóstico: acceso `adb` al dispositivo.

## Nota sobre la capa GLES

Android 10+ introduce el sistema oficial de GLES Layers para debugging. Estas capas pueden cargarse en cualquier aplicación debuggable, o en cualquier aplicación si se dispone de acceso root. Sin root, no es posible interceptar OpenGL ES en apps arbitrarias de tiendas oficiales. La implementación en este repositorio respeta esa limitación de plataforma.
