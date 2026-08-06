package com.xrevival.gltools26.core;

public final class UserAgentFactory {
    public static final String CHROME_VERSION = "142.0.7444.60";
    public static final String SAFARI_VERSION = "19.0";
    public static final String WEBKIT_VERSION = "605.1.15";
    public static final String IOS_VERSION = "19_3";
    public static final String DEFAULT_BUILD = "BP2A.250605.015";
    public static final int DESKTOP_VIEWPORT_WIDTH = 1280;

    public static String resolve(SpoofConfig cfg, DeviceProfile profile) {
        if (cfg.isCustomUserAgentEnabled()) {
            String ua = cfg.getCustomUserAgent();
            if (ua != null && !ua.trim().isEmpty()) return ua.trim();
        }
        if (cfg.isForceDesktopViewport()) return desktopFor(profile);
        return forProfile(profile);
    }

    public static int resolveViewportWidthCss(SpoofConfig cfg, DeviceProfile profile) {
        if (cfg.isForceDesktopViewport()) return DESKTOP_VIEWPORT_WIDTH;
        float scale = profile.getDensityDpi() / 160f;
        if (scale <= 0f) scale = 1f;
        return Math.round(profile.getWidthPx() / scale);
    }

    public static String forProfile(DeviceProfile p) {
        if (p.getPlatform() == DeviceProfile.Platform.IOS) return iosSafari(p);
        return androidChrome(p);
    }

    public static String iosSafari(DeviceProfile p) {
        String deviceToken = (p.getCategory() == DeviceProfile.Category.TABLET) ? "iPad" : "iPhone";
        return "Mozilla/5.0 (" + deviceToken + "; CPU " + deviceToken + " OS " + IOS_VERSION + " like Mac OS X) AppleWebKit/" + WEBKIT_VERSION + " (KHTML, like Gecko) Version/" + SAFARI_VERSION + " Mobile/15E148 Safari/604.1";
    }

    public static String androidChrome(DeviceProfile p) {
        return "Mozilla/5.0 (Linux; Android " + extractAndroidVersion(p) + "; " + p.getModel() + " Build/" + extractBuildId(p) + "; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/" + CHROME_VERSION + " Mobile Safari/537.36";
    }

    public static String desktopFor(DeviceProfile p) {
        if (p.getPlatform() == DeviceProfile.Platform.IOS) return desktopSafariMac();
        return desktopChromeWindows();
    }

    public static String desktopChromeWindows() {
        return "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/" + CHROME_VERSION + " Safari/537.36";
    }

    public static String desktopSafariMac() {
        return "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/" + WEBKIT_VERSION + " (KHTML, like Gecko) Version/" + SAFARI_VERSION + " Safari/" + WEBKIT_VERSION;
    }

    public static String extractBuildId(DeviceProfile p) {
        String fp = p.getFingerprint();
        if (fp == null || fp.isEmpty()) return DEFAULT_BUILD;
        String[] parts = fp.split("/");
        if (parts.length >= 4 && !parts[3].isEmpty()) return parts[3];
        return DEFAULT_BUILD;
    }

    public static String extractAndroidVersion(DeviceProfile p) {
        String fp = p.getFingerprint();
        if (fp == null || fp.isEmpty()) return "16";
        String[] parts = fp.split("/");
        if (parts.length >= 3) {
            String devVer = parts[2];
            int colon = devVer.indexOf(':');
            if (colon >= 0 && colon + 1 < devVer.length()) return devVer.substring(colon + 1);
        }
        return "16";
    }

    private UserAgentFactory() {}
}
