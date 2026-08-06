package com.xrevival.gltools26.core;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public final class DeviceProfile implements Serializable {
    private static final long serialVersionUID = 1L;
    public enum Category { PHONE, TABLET, DESKTOP, LAPTOP, WATCH }
    public enum Platform { ANDROID, IOS }

    private final String id, marketName, brand, manufacturer, model,
                         device, product, hardware, fingerprint;
    private final Platform platform;
    private final Category category;
    private final String socName, gpuVendor, gpuRenderer, glEsVersion;
    private final int widthPx, heightPx, densityDpi, refreshRateHz;
    private final boolean ltpo, hdr10Plus;
    private final int batteryMah, fastChargeW;
    private final boolean thermalBypass, fpsUnlock;
    private final int ramMb;

    private DeviceProfile(Builder b) {
        this.id = b.id; this.marketName = b.marketName; this.brand = b.brand;
        this.manufacturer = b.manufacturer; this.model = b.model;
        this.device = b.device; this.product = b.product; this.hardware = b.hardware;
        this.fingerprint = b.fingerprint; this.platform = b.platform; this.category = b.category;
        this.socName = b.socName; this.gpuVendor = b.gpuVendor;
        this.gpuRenderer = b.gpuRenderer; this.glEsVersion = b.glEsVersion;
        this.widthPx = b.widthPx; this.heightPx = b.heightPx;
        this.densityDpi = b.densityDpi; this.refreshRateHz = b.refreshRateHz;
        this.ltpo = b.ltpo; this.hdr10Plus = b.hdr10Plus;
        this.batteryMah = b.batteryMah; this.fastChargeW = b.fastChargeW;
        this.thermalBypass = b.thermalBypass; this.fpsUnlock = b.fpsUnlock;
        this.ramMb = b.ramMb;
    }

    public String getId()            { return id; }
    public String getMarketName()    { return marketName; }
    public String getBrand()         { return brand; }
    public String getManufacturer()  { return manufacturer; }
    public String getModel()         { return model; }
    public String getDevice()        { return device; }
    public String getProduct()       { return product; }
    public String getHardware()      { return hardware; }
    public String getFingerprint()   { return fingerprint; }
    public Platform getPlatform()    { return platform; }
    public Category getCategory()    { return category; }
    public String getSocName()       { return socName; }
    public String getGpuVendor()     { return gpuVendor; }
    public String getGpuRenderer()   { return gpuRenderer; }
    public String getGlEsVersion()   { return glEsVersion; }
    public int getWidthPx()          { return widthPx; }
    public int getHeightPx()         { return heightPx; }
    public int getDensityDpi()       { return densityDpi; }
    public int getRefreshRateHz()    { return refreshRateHz; }
    public boolean isLtpo()          { return ltpo; }
    public boolean isHdr10Plus()     { return hdr10Plus; }
    public int getBatteryMah()       { return batteryMah; }
    public int getFastChargeW()      { return fastChargeW; }
    public boolean isThermalBypass() { return thermalBypass; }
    public boolean isFpsUnlock()     { return fpsUnlock; }
    public int getRamMb()            { return ramMb; }

    public boolean supportsNativeSpoof() { return platform == Platform.ANDROID; }

    public Map<String, String> toBuildProps() {
        Map<String, String> p = new LinkedHashMap<>();
        if (!supportsNativeSpoof()) return Collections.unmodifiableMap(p);
        p.put("ro.product.brand", brand);
        p.put("ro.product.manufacturer", manufacturer);
        p.put("ro.product.model", model);
        p.put("ro.product.device", device);
        p.put("ro.product.name", product);
        p.put("ro.hardware", hardware);
        p.put("ro.build.fingerprint", fingerprint);
        p.put("ro.build.version.release", "16");
        p.put("ro.build.version.sdk", "36");
        p.put("ro.soc.model", socName);
        p.put("ro.opengles.version", "196610");
        return Collections.unmodifiableMap(p);
    }

    public String toSystemPropBlock() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> e : toBuildProps().entrySet()) {
            sb.append(e.getKey()).append('=').append(e.getValue()).append('\n');
        }
        return sb.toString();
    }

    @Override public boolean equals(Object o) {
        return (o instanceof DeviceProfile) && id.equals(((DeviceProfile) o).id);
    }
    @Override public int hashCode() { return id.hashCode(); }
    @Override public String toString() { return marketName; }

    public static Builder builder() { return new Builder(); }

    public static final class Builder {
        private String id, marketName, brand, manufacturer, model,
                       device, product, hardware, fingerprint;
        private Platform platform = Platform.ANDROID;
        private Category category = Category.PHONE;
        private String socName = "Snapdragon 8 Elite Gen 5";
        private String gpuVendor = "Qualcomm";
        private String gpuRenderer = "Adreno 840";
        private String glEsVersion = "3.2";
        private int widthPx = 1440, heightPx = 3120, densityDpi = 560, refreshRateHz = 120;
        private boolean ltpo = true, hdr10Plus = true;
        private int batteryMah = 5000, fastChargeW = 120;
        private boolean thermalBypass = false, fpsUnlock = false;
        private int ramMb = 12288;

        public Builder id(String v)            { this.id = v; return this; }
        public Builder marketName(String v)    { this.marketName = v; return this; }
        public Builder brand(String v)         { this.brand = v; return this; }
        public Builder manufacturer(String v)  { this.manufacturer = v; return this; }
        public Builder model(String v)         { this.model = v; return this; }
        public Builder device(String v)        { this.device = v; return this; }
        public Builder product(String v)       { this.product = v; return this; }
        public Builder hardware(String v)      { this.hardware = v; return this; }
        public Builder fingerprint(String v)   { this.fingerprint = v; return this; }
        public Builder platform(Platform v)    { this.platform = v; return this; }
        public Builder category(Category v)    { this.category = v; return this; }
        public Builder socName(String v)       { this.socName = v; return this; }
        public Builder gpuVendor(String v)     { this.gpuVendor = v; return this; }
        public Builder gpuRenderer(String v)   { this.gpuRenderer = v; return this; }
        public Builder glEsVersion(String v)   { this.glEsVersion = v; return this; }
        public Builder display(int w, int h, int dpi, int hz) {
            this.widthPx = w; this.heightPx = h; this.densityDpi = dpi; this.refreshRateHz = hz;
            return this;
        }
        public Builder ltpo(boolean v)         { this.ltpo = v; return this; }
        public Builder hdr10Plus(boolean v)    { this.hdr10Plus = v; return this; }
        public Builder battery(int mah, int w) { this.batteryMah = mah; this.fastChargeW = w; return this; }
        public Builder thermalBypass(boolean v){ this.thermalBypass = v; return this; }
        public Builder fpsUnlock(boolean v)    { this.fpsUnlock = v; return this; }
        public Builder ramMb(int v)            { this.ramMb = v; return this; }

        public DeviceProfile build() {
            Objects.requireNonNull(id, "id obligatorio");
            Objects.requireNonNull(marketName, "marketName obligatorio");
            return new DeviceProfile(this);
        }
    }
}
