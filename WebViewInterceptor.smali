.class public Lcom/gltools/offline/smali/WebViewInterceptor;
.super Ljava/lang/Object;
.source "WebViewInterceptor.smali"

# Ilustra la interceptación de loadUrl en WebView interno de GLTools
# para servir contenido local en lugar de URLs externas caídas.

.method public static interceptLoadUrl(Landroid/webkit/WebView;Ljava/lang/String;)V
    .locals 2
    .param p0, "webView"
    .param p1, "url"

    const-string v0, "gltools.app"

    invoke-virtual {p1, v0}, Ljava/lang/String;->contains(Ljava/lang/CharSequence;)Z

    move-result v0

    if-eqz v0, :cond_load_original

    # Cargar página local de ayuda embebida en assets
    const-string v1, "file:///android_asset/help_offline.html"
    invoke-virtual {p0, v1}, Landroid/webkit/WebView;->loadUrl(Ljava/lang/String;)V

    return-void

    :cond_load_original
    invoke-virtual {p0, p1}, Landroid/webkit/WebView;->loadUrl(Ljava/lang/String;)V

    return-void
.end method
