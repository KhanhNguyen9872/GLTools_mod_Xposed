.class public Lcom/gltools/offline/smali/ApiClientRedirect;
.super Ljava/lang/Object;
.source "ApiClientRedirect.smali"

# Este archivo smali ilustra la modificación necesaria en la clase
# com.superpaninbros.glng.network.ApiClient para redirigir tráfico
# de gltools.app a localhost:8765.
#
# Alcance: interceptación de red neutral, sin modificación de licencia.
# Limitación: la constante exacta depende de la versión específica del APK.
# Advertencia: modificar smali requiere recompilación y firma propia.

.method public static redirectUrl(Ljava/lang/String;)Ljava/lang/String;
    .locals 2
    .param p0, "originalUrl"

    const-string v0, "https://gltools.app"

    invoke-virtual {p0, v0}, Ljava/lang/String;->startsWith(Ljava/lang/String;)Z

    move-result v0

    if-eqz v0, :cond_original

    # Redirigir a servidor mock local
    const-string v0, "https://gltools.app"
    const-string v1, "http://127.0.0.1:8765"
    invoke-virtual {p0, v0, v1}, Ljava/lang/String;->replace(Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Ljava/lang/String;

    move-result-object p0

    :cond_original
    return-object p0
.end method
