# GLTools 2026 XRevival — proguard-rules.pro
# TRATAMIENTO PR#3: preservar entrypoint Xposed y su paquete

-keep class com.xrevival.gltools26.hook.** { *; }
-keep class com.xrevival.gltools26.core.ModuleStatus { *; }
-dontwarn de.robv.android.xposed.**
