# Diagnóstico del estado de licencia local

> ⚠️ **Advertencia:** este diagnóstico solo lee datos existentes en el dispositivo. No modifica, restaura ni falsifica ningún estado de compra.

## Propósito

Determinar, antes de aplicar cualquier modificación, si el dispositivo conserva el estado de compra de GLTools en almacenamiento local. Esto distingue dos escenarios:

1. **Estado local presente:** el problema es solo de red (servidor caído). La solución es el mock de endpoints (Fase 3).
2. **Estado local ausente:** la compra se perdió (cambio de ROM, borrado de datos). Ninguna técnica de este repositorio la restaura de forma legítima sin contactar al desarrollador original.

## Métodos de extracción

### Opción A: ADB backup (no requiere root)

```bash
adb backup -f gltools_backup.ab -noapk com.superpaninbros.glng
```

Extraer el contenido con `abe` (Android Backup Extractor) y examinar `shared_prefs/*.xml`.

### Opción B: run-as (si la app es debuggable)

```bash
adb shell run-as com.superpaninbros.glng ls shared_prefs/
adb shell run-as com.superpaninbros.glng cat shared_prefs/*.xml
```

### Opción C: acceso root (si disponible)

```bash
adb shell su -c "cat /data/data/com.superpaninbros.glng/shared_prefs/*.xml"
```

## Claves a buscar

| Clave | Significado si está presente |
|---|---|
| `premium` | Estado premium activo (booleano) |
| `purchased` | Indicador de compra completada |
| `coins` | Saldo de monedas (sistema legacy) |
| `license` | Hash o token de licencia |
| `sku` | Identificador de producto comprado |

## Interpretación

- Si alguna clave indica estado activo (`true`, valor numérico > 0, o cadena no vacía): **proceder a Fase 3** (servidor mock).
- Si todas las claves están ausentes o en estado inactivo: **no proceder con este repositorio** para recuperar premium. Contactar al desarrollador original (n0n3m4) si es localizable, o aceptar la pérdida de funciones premium.

## Limitaciones

- El diagnóstico no puede distinguir entre "nunca se compró" y "se compró pero se perdió el estado local". Ambos se ven como ausencia de claves.
- Si el dispositivo fue reinstalado desde cero sin backup, el estado de compra de Google Play Billing puede re-validarse contra los servidores de Google (no de GLTools), pero eso está fuera del alcance de este documento.
