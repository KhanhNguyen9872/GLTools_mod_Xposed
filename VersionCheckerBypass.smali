.class public Lcom/gltools/offline/smali/VersionCheckerBypass;
.super Ljava/lang/Object;
.source "VersionCheckerBypass.smali"

# Ilustra la modificación de com.superpaninbros.glng.update.VersionChecker
# para evitar crash cuando gltools.app no responde.
#
# Estrategia: interceptar el método que lanza la petición de red y
# devolver una respuesta embebida en lugar de contactar el servidor.

.method public static getEmbeddedVersion()Ljava/lang/String;
    .locals 1

    # Versión embebida que evita force-update
    const-string v0, "{\"version\":\"1.99\",\"force_update\":false,\"min_android\":16,\"max_android\":34}"

    return-object v0
.end method
