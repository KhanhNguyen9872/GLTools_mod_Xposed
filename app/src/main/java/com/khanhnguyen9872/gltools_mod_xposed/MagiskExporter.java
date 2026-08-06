package com.xrevival.gltools26.magisk;

import android.content.Context;
import com.xrevival.gltools26.core.DeviceProfile;
import com.xrevival.gltools26.core.ModuleStatus;
import com.xrevival.gltools26.core.SpoofConfig;
import com.xrevival.gltools26.core.UserAgentFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public final class MagiskExporter {
    public static String exportZip(Context ctx, DeviceProfile p, SpoofConfig cfg) throws IOException {
        File outDir = ctx.getExternalFilesDir(null);
        if (outDir != null && !outDir.exists()) outDir.mkdirs();
        File zip = new File(outDir, "XRevival_" + p.getId() + ".zip");
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zip));
        try {
            put(zos, "module.prop", moduleProp(p, cfg));
            put(zos, "system.prop", systemProp(p));
            put(zos, "service.sh", serviceSh(p, cfg));
            put(zos, "action.sh", actionSh(p, cfg));
            put(zos, "customize.sh", customizeSh(p));
        } finally { zos.close(); }
        return zip.getAbsolutePath();
    }

    private static void put(ZipOutputStream zos, String name, String content) throws IOException {
        zos.putNextEntry(new ZipEntry(name));
        zos.write(content.getBytes(StandardCharsets.UTF_8));
        zos.closeEntry();
    }

    private static String moduleProp(DeviceProfile p, SpoofConfig cfg) {
        return "id=xrevival_" + p.getId() + "\nname=GLTools 2026 XRevival - " + p.getMarketName() + " Spoofer\nversion=v1.0\nversionCode=20260806\nauthor=XRevival Team\ndescription=Spoof " + p.getMarketName() + ": " + p.getSocName() + " + " + p.getGpuRenderer() + ", " + p.getRefreshRateHz() + "Hz, thermal bypass, FPS unlock. Credits: KhanhNguyen9872, magiskmodule.com\n";
    }

    private static String systemProp(DeviceProfile p) {
        StringBuilder sb = new StringBuilder("# XRevival system.prop\n");
        for (Map.Entry<String, String> e : expandProps(p).entrySet()) sb.append(e.getKey()).append('=').append(e.getValue()).append('\n');
        return sb.toString();
    }

    private static Map<String, String> expandProps(DeviceProfile p) {
        Map<String, String> m = new LinkedHashMap<>(p.toBuildProps());
        String buildId = UserAgentFactory.extractBuildId(p), ver = UserAgentFactory.extractAndroidVersion(p);
        m.put("ro.build.id", buildId); m.put("ro.build.display.id", buildId);
        m.put("ro.build.version.release_or_codename", ver); m.put("ro.build.version.security_patch", "2026-07-05");
        String[] partitions = {"system", "vendor", "odm", "product"};
        String[] fields = {"brand", "manufacturer", "model", "device", "name"};
        for (String part : partitions) for (String f : fields) { String base = m.get("ro.product." + f); if (base != null) m.put("ro.product." + part + "." + f, base); }
        return m;
    }

    private static String serviceSh(DeviceProfile p, SpoofConfig cfg) {
        int fps = cfg.isFpsUnlock() ? cfg.getMaxFps() : p.getRefreshRateHz();
        StringBuilder sb = new StringBuilder();
        sb.append("#!/system/bin/sh\n# XRevival service.sh\nwhile [ "$(getprop sys.boot_completed)" != "1" ]; do sleep 3; done\n\n");
        sb.append("resetprop sys.thermal.status 0\nresetprop vendor.thermal.status 0\nresetprop persist.sys.thermal.status 0\nresetprop sys.game.fps.limit 0\nresetprop persist.vendor.max_fps ").append(fps).append("\n\n");
        if (cfg.getPerfProfile() == SpoofConfig.PerfProfile.APEX || cfg.getPerfProfile() == SpoofConfig.PerfProfile.GAMING) {
            sb.append("for c in /sys/class/devfreq/*; do echo performance > "$c/governor" 2>/dev/null; done\n");
            sb.append("for c in /sys/devices/system/cpu/cpu*/cpufreq/scaling_governor; do echo performance > "$c" 2>/dev/null; done\n");
        }
        sb.append("\nexit 0\n");
        return sb.toString();
    }

    private static String actionSh(DeviceProfile p, SpoofConfig cfg) {
        int fps = cfg.isFpsUnlock() ? cfg.getMaxFps() : p.getRefreshRateHz();
        return "#!/system/bin/sh\n# XRevival action.sh\nresetprop sys.thermal.status 0\nresetprop vendor.thermal.status 0\nresetprop persist.vendor.max_fps " + fps + "\nresetprop sys.game.fps.limit 0\necho "[XRevival] Perfil " + cfg.getPerfProfile() + " aplicado . techo " + fps + "Hz . thermal NONE"\nexit 0\n";
    }

    private static String customizeSh(DeviceProfile p) {
        return "#!/system/bin/sh\nui_print "=================================="\nui_print " GLTools 2026 XRevival " + ModuleStatus.MODULE_VERSION + ""\nui_print " Spoof activo: " + p.getMarketName() + ""\nui_print " SoC: " + p.getSocName() + " . GPU: " + p.getGpuRenderer() + ""\nui_print "=================================="\n";
    }

    private MagiskExporter() {}
}
