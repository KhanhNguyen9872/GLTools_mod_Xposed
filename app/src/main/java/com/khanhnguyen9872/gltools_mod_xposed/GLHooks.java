package com.xrevival.gltools26.hook;

import android.content.pm.FeatureInfo;
import android.opengl.GLES20;
import com.xrevival.gltools26.core.DeviceProfile;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public final class GLHooks {
    private static final int GL_VENDOR = 0x1F00, GL_RENDERER = 0x1F01, GL_VERSION = 0x1F02, GL_EXTENSIONS = 0x1F03;
    private static boolean installed = false;

    public static synchronized void install(LoadPackageParam lpparam, DeviceProfile profile) {
        if (installed) return;
        installed = true;
        hookGlGetString(profile);
        hookGlesFeatureLevel(lpparam, parseGlEsHex(profile.getGlEsVersion()));
    }

    private static void hookGlGetString(final DeviceProfile p) {
        final String vendor = vendorString(p), renderer = rendererString(p), version = versionString(p), extensions = extensionsString(p);
        try {
            XposedHelpers.findAndHookMethod(GLES20.class, "glGetString", int.class, new XC_MethodHook() {
                @Override protected void beforeHookedMethod(MethodHookParam param) {
                    int name = (Integer) param.args[0];
                    if (name == GL_VENDOR) param.setResult(vendor);
                    else if (name == GL_RENDERER) param.setResult(renderer);
                    else if (name == GL_VERSION) param.setResult(version);
                    else if (name == GL_EXTENSIONS) param.setResult(extensions);
                }
            });
            MainModule.log("glGetString hook OK -> " + renderer);
        } catch (Throwable t) { MainModule.log("hookGlGetString fallo: " + t); }
    }

    private static void hookGlesFeatureLevel(LoadPackageParam lpparam, final int targetEs) {
        try {
            XposedHelpers.findAndHookMethod("android.app.ApplicationPackageManager", lpparam.classLoader, "getSystemAvailableFeatures", new XC_MethodHook() {
                @Override protected void afterHookedMethod(MethodHookParam param) {
                    Object result = param.getResult();
                    if (!(result instanceof FeatureInfo[])) return;
                    for (FeatureInfo fi : (FeatureInfo[]) result) if (fi.reqGlEsVersion != 0) fi.reqGlEsVersion = targetEs;
                }
            });
            MainModule.log("GLES feature -> 0x" + Integer.toHexString(targetEs));
        } catch (Throwable t) { MainModule.log("hookGlesFeatureLevel fallo: " + t); }
    }

    private static String vendorString(DeviceProfile p) {
        switch (p.getGpuVendor()) {
            case "Qualcomm": return "Qualcomm";
            case "MediaTek": case "HiSilicon": return "ARM";
            case "Imagination": return "Imagination Technologies";
            case "Apple": return "Apple Inc.";
            default: return p.getGpuVendor();
        }
    }

    private static String rendererString(DeviceProfile p) {
        String r = p.getGpuRenderer();
        if ("Qualcomm".equals(p.getGpuVendor()) && r.startsWith("Adreno ")) return "Adreno (TM) " + r.substring("Adreno ".length());
        return r;
    }

    private static String versionString(DeviceProfile p) {
        if ("Qualcomm".equals(p.getGpuVendor())) {
            String num = p.getGpuRenderer().replace("Adreno ", "");
            return "OpenGL ES 3.2 V@" + num + ".0 (GIT@xrevival26)";
        }
        return "OpenGL ES " + p.getGlEsVersion();
    }

    private static String extensionsString(DeviceProfile p) {
        StringBuilder sb = new StringBuilder(EXT_COMMON);
        if ("Qualcomm".equals(p.getGpuVendor())) sb.append(' ').append(EXT_QUALCOMM);
        else if ("MediaTek".equals(p.getGpuVendor()) || "HiSilicon".equals(p.getGpuVendor())) sb.append(' ').append(EXT_ARM);
        return sb.toString();
    }

    private static int parseGlEsHex(String ver) {
        try { String[] parts = ver.split("\."); return (Integer.parseInt(parts[0]) << 16) | (Integer.parseInt(parts[1]) & 0xFFFF); }
        catch (Exception e) { return 0x30002; }
    }

    private static final String EXT_COMMON = "GL_OES_EGL_image GL_OES_EGL_image_external GL_OES_EGL_sync GL_OES_vertex_half_float GL_OES_framebuffer_object GL_OES_rgb8_rgba8 GL_OES_compressed_ETC1_RGB8_texture GL_OES_compressed_ETC2_RGB8_texture GL_OES_compressed_ETC2_RGBA8_texture GL_OES_texture_float GL_OES_texture_half_float GL_OES_texture_3D GL_OES_depth_texture GL_OES_depth24 GL_OES_depth32 GL_OES_packed_depth_stencil GL_OES_get_program_binary GL_OES_mapbuffer GL_OES_vertex_array_object GL_OES_surfaceless_context GL_EXT_texture_filter_anisotropic GL_EXT_texture_format_BGRA8888 GL_EXT_sRGB GL_EXT_sRGB_write_control GL_EXT_color_buffer_float GL_EXT_color_buffer_half_float GL_EXT_disjoint_timer_query GL_EXT_multisampled_render_to_texture GL_EXT_texture_mirror_clamp_to_edge GL_KHR_texture_compression_astc_hdr GL_KHR_texture_compression_astc_ldr GL_KHR_texture_compression_astc_sliced_3d GL_KHR_debug GL_KHR_robustness GL_KHR_blend_equation_advanced GL_KHR_parallel_shader_compile";
    private static final String EXT_QUALCOMM = "GL_EXT_texture_type_2_10_10_10_rev GL_OES_sample_variables GL_EXT_clip_control GL_EXT_buffer_storage GL_EXT_external_buffer GL_EXT_memory_object GL_EXT_memory_object_fd GL_EXT_semaphore GL_EXT_semaphore_fd GL_QCOM_performance_counters GL_QCOM_shader_framebuffer_fetch";
    private static final String EXT_ARM = "GL_ARM_shader_framebuffer_fetch_depth_stencil GL_EXT_texture_type_2_10_10_10_rev GL_EXT_buffer_storage GL_EXT_external_buffer GL_EXT_memory_object GL_EXT_memory_object_fd";

    private GLHooks() {}
}
