package com.xrevival.gltools26.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class DeviceCatalog {
    private static final Map<String, DeviceProfile> PROFILES = new LinkedHashMap<>();

    static {
        register(DeviceProfile.builder()
                .id("iqoo15").marketName("iQOO 15")
                .brand("iQOO").manufacturer("iQOO").model("V2501A")
                .device("iqoo15").product("iqoo15_5g").hardware("qcom")
                .fingerprint("iQOO/iqoo15_5g/iqoo15:16/BP2A.250605.031.A1/V2501A_A_15.0.10:user/release-keys")
                .socName("Snapdragon 8 Gen 4").gpuVendor("Qualcomm").gpuRenderer("Adreno 830")
                .display(1440, 3168, 510, 144).ltpo(true).hdr10Plus(true)
                .battery(5000, 120).thermalBypass(true).fpsUnlock(true)
                .ramMb(16384).build());

        register(DeviceProfile.builder()
                .id("ip17pm").marketName("iPhone 17 Pro Max")
                .brand("apple").manufacturer("Apple").model("iPhone18,2")
                .platform(DeviceProfile.Platform.IOS)
                .socName("Apple A19 Pro").gpuVendor("Apple").gpuRenderer("Apple GPU")
                .display(1320, 2868, 460, 120).battery(4832, 30).ramMb(12288).build());

        register(DeviceProfile.builder()
                .id("ip17p").marketName("iPhone 17 Pro")
                .brand("apple").manufacturer("Apple").model("iPhone18,1")
                .platform(DeviceProfile.Platform.IOS)
                .socName("Apple A19 Pro").gpuVendor("Apple").gpuRenderer("Apple GPU")
                .display(1206, 2622, 460, 120).battery(4252, 30).ramMb(12288).build());

        register(DeviceProfile.builder()
                .id("ipair").marketName("iPhone Air")
                .brand("apple").manufacturer("Apple").model("iPhone18,4")
                .platform(DeviceProfile.Platform.IOS)
                .socName("Apple A19 Pro").gpuVendor("Apple").gpuRenderer("Apple GPU")
                .display(1290, 2796, 460, 120).battery(3149, 20).ramMb(8192).build());

        register(DeviceProfile.builder()
                .id("s26u").marketName("Galaxy S26 Ultra")
                .brand("samsung").manufacturer("Samsung").model("SM-S948B")
                .device("qb3q").product("qb3qxxx").hardware("qcom")
                .fingerprint("samsung/qb3qxxx/qb3q:16/BP2A.250605.015.A2/S948BXXU1AZA1:user/release-keys")
                .socName("Snapdragon 8 Elite Gen 5 for Galaxy").gpuVendor("Qualcomm").gpuRenderer("Adreno 840")
                .display(1440, 3120, 505, 120).ltpo(true).hdr10Plus(true)
                .battery(5000, 60).ramMb(16384).build());

        register(DeviceProfile.builder()
                .id("s26p").marketName("Galaxy S26+")
                .brand("samsung").manufacturer("Samsung").model("SM-S947B")
                .device("qb2q").product("qb2qxxx").hardware("qcom")
                .fingerprint("samsung/qb2qxxx/qb2q:16/BP2A.250605.015.A2/S947BXXU1AZA1:user/release-keys")
                .socName("Snapdragon 8 Elite Gen 5 for Galaxy").gpuVendor("Qualcomm").gpuRenderer("Adreno 840")
                .display(1440, 3088, 500, 120).ltpo(true).hdr10Plus(true)
                .battery(4900, 45).ramMb(16384).build());

        register(DeviceProfile.builder()
                .id("s26").marketName("Galaxy S26")
                .brand("samsung").manufacturer("Samsung").model("SM-S942B")
                .device("qbq").product("qbqxxx").hardware("qcom")
                .fingerprint("samsung/qbqxxx/qbq:16/BP2A.250605.015.A2/S942BXXU1AZA1:user/release-keys")
                .socName("Snapdragon 8 Elite Gen 5 for Galaxy").gpuVendor("Qualcomm").gpuRenderer("Adreno 840")
                .display(1080, 2340, 425, 120).ltpo(true)
                .battery(4300, 25).ramMb(12288).build());

        register(DeviceProfile.builder()
                .id("p10pxl").marketName("Pixel 10 Pro XL")
                .brand("google").manufacturer("Google").model("GC9VE")
                .device("telon").product("telon").hardware("telon")
                .fingerprint("google/telon/telon:16/AP4A.260105.002/13984521:user/release-keys")
                .socName("Google Tensor G5").gpuVendor("Imagination").gpuRenderer("IMG DXT-48-1536")
                .display(1344, 2992, 486, 120).ltpo(true).hdr10Plus(true)
                .battery(5200, 30).ramMb(12288).build());

        register(DeviceProfile.builder()
                .id("p10p").marketName("Pixel 10 Pro")
                .brand("google").manufacturer("Google").model("GC8VK")
                .device("telemaco").product("telemaco").hardware("telemaco")
                .fingerprint("google/telemaco/telemaco:16/AP4A.260105.002/13984521:user/release-keys")
                .socName("Google Tensor G5").gpuVendor("Imagination").gpuRenderer("IMG DXT-48-1536")
                .display(1280, 2856, 495, 120).ltpo(true).hdr10Plus(true)
                .battery(4870, 30).ramMb(12288).build());

        register(DeviceProfile.builder()
                .id("p10a").marketName("Pixel 10a")
                .brand("google").manufacturer("Google").model("GK8VK")
                .device("asteria").product("asteria").hardware("asteria")
                .fingerprint("google/asteria/asteria:16/AP3A.251205.004/13812345:user/release-keys")
                .socName("Google Tensor G4").gpuVendor("Imagination").gpuRenderer("IMG BXM-8-256")
                .display(1080, 2424, 422, 120)
                .battery(5100, 23).ramMb(12288).build());

        register(DeviceProfile.builder()
                .id("mi17u").marketName("Xiaomi 17 Ultra")
                .brand("Xiaomi").manufacturer("Xiaomi").model("2512BPX34G")
                .device("nuova").product("nuova_global").hardware("qcom")
                .fingerprint("Xiaomi/nuova/nuova:16/BP2A.250605.031.A5/OS2.0.100.0.WXAMIXM:user/release-keys")
                .socName("Snapdragon 8 Elite Gen 5").gpuVendor("Qualcomm").gpuRenderer("Adreno 840")
                .display(1440, 3200, 522, 120).ltpo(true).hdr10Plus(true)
                .battery(6800, 90).ramMb(16384).build());

        register(DeviceProfile.builder()
                .id("mi17").marketName("Xiaomi 17")
                .brand("Xiaomi").manufacturer("Xiaomi").model("2510BPX74G")
                .device("aurora").product("aurora_global").hardware("qcom")
                .fingerprint("Xiaomi/aurora/aurora:16/BP2A.250605.031.A5/OS2.0.100.0.WOCMIXM:user/release-keys")
                .socName("Snapdragon 8 Elite Gen 5").gpuVendor("Qualcomm").gpuRenderer("Adreno 840")
                .display(1200, 2670, 460, 120).ltpo(true).hdr10Plus(true)
                .battery(6300, 100).ramMb(16384).build());

        register(DeviceProfile.builder()
                .id("pura90p").marketName("Pura 90 Pro")
                .brand("HUAWEI").manufacturer("HUAWEI").model("GOT-W29")
                .device("pura90pro").product("pura90pro").hardware("kirin9030")
                .fingerprint("HUAWEI/pura90pro/pura90pro:16/HUAWEIPura90Pro/5.0.0.220C00:user/release-keys")
                .socName("Kirin 9030").gpuVendor("HiSilicon").gpuRenderer("Maleoon 930")
                .display(1260, 2844, 480, 120).ltpo(true).hdr10Plus(true)
                .battery(5170, 100).ramMb(16384).build());

        register(DeviceProfile.builder()
                .id("mate70pp").marketName("Mate 70 Pro+")
                .brand("HUAWEI").manufacturer("HUAWEI").model("BNE-N59")
                .device("mate70proplus").product("mate70proplus").hardware("kirin9020")
                .fingerprint("HUAWEI/mate70proplus/mate70proplus:16/HUAWEIMATE70Pro+/5.0.0.220C00:user/release-keys")
                .socName("Kirin 9020").gpuVendor("HiSilicon").gpuRenderer("Maleoon 920")
                .display(1260, 2844, 480, 120).ltpo(true).hdr10Plus(true)
                .battery(5700, 100).ramMb(16384).build());

        register(DeviceProfile.builder()
                .id("op15").marketName("OnePlus 15")
                .brand("OnePlus").manufacturer("OnePlus").model("CPH2651")
                .device("OP55D7L1").product("OP55D7L1").hardware("qcom")
                .fingerprint("OnePlus/OP55D7L1/OP55D7L1:16/BP2A.250605.015.A1/OP55D7L1_16.0.0.300:user/release-keys")
                .socName("Snapdragon 8 Elite Gen 5").gpuVendor("Qualcomm").gpuRenderer("Adreno 840")
                .display(1264, 2780, 450, 165).ltpo(true).hdr10Plus(true)
                .battery(7300, 80).thermalBypass(true).fpsUnlock(true).ramMb(16384).build());

        register(DeviceProfile.builder()
                .id("op15t").marketName("OnePlus 15T")
                .brand("OnePlus").manufacturer("OnePlus").model("CPH2683")
                .device("OP55AAL1").product("OP55AAL1").hardware("qcom")
                .fingerprint("OnePlus/OP55AAL1/OP55AAL1:16/BP2A.250605.015.A1/OP55AAL1_16.0.0.250:user/release-keys")
                .socName("Snapdragon 8 Elite Gen 5").gpuVendor("Qualcomm").gpuRenderer("Adreno 840")
                .display(1216, 2640, 460, 165).ltpo(true).hdr10Plus(true)
                .battery(6500, 80).thermalBypass(true).fpsUnlock(true).ramMb(16384).build());

        register(DeviceProfile.builder()
                .id("fx9u").marketName("Find X9 Ultra")
                .brand("OPPO").manufacturer("OPPO").model("CPH2803")
                .device("OP5939L1").product("OP5939L1").hardware("qcom")
                .fingerprint("OPPO/OP5939L1/OP5939L1:16/BP2A.250605.031/OP5939L1_16.0.0.305:user/release-keys")
                .socName("Snapdragon 8 Elite Gen 5").gpuVendor("Qualcomm").gpuRenderer("Adreno 840")
                .display(1440, 3168, 510, 120).ltpo(true).hdr10Plus(true)
                .battery(6100, 80).thermalBypass(true).fpsUnlock(true).ramMb(16384).build());

        register(DeviceProfile.builder()
                .id("fx9p").marketName("Find X9 Pro")
                .brand("OPPO").manufacturer("OPPO").model("CPH2791")
                .device("OP5971L1").product("OP5971L1").hardware("mt6991")
                .fingerprint("OPPO/OP5971L1/OP5971L1:16/BP2A.250605.031/OP5971L1_16.0.0.301:user/release-keys")
                .socName("Dimensity 9500").gpuVendor("MediaTek").gpuRenderer("Immortalis G1 Ultra")
                .display(1260, 2780, 460, 120).ltpo(true).hdr10Plus(true)
                .battery(5910, 80).ramMb(16384).build());

        register(DeviceProfile.builder()
                .id("x300u").marketName("X300 Ultra")
                .brand("vivo").manufacturer("vivo").model("V2432A")
                .device("x300ultra").product("x300ultra").hardware("qcom")
                .fingerprint("vivo/x300ultra/x300ultra:16/BP2A.250605.031/V2432A_A_15.1.10:user/release-keys")
                .socName("Snapdragon 8 Elite Gen 5").gpuVendor("Qualcomm").gpuRenderer("Adreno 840")
                .display(1260, 2844, 480, 120).ltpo(true).hdr10Plus(true)
                .battery(6000, 90).thermalBypass(true).fpsUnlock(true).ramMb(16384).build());

        register(DeviceProfile.builder()
                .id("x300p").marketName("X300 Pro")
                .brand("vivo").manufacturer("vivo").model("V2419A")
                .device("x300pro").product("x300pro").hardware("mt6991")
                .fingerprint("vivo/x300pro/x300pro:16/BP2A.250605.031/V2419A_A_15.1.8:user/release-keys")
                .socName("Dimensity 9500").gpuVendor("MediaTek").gpuRenderer("Immortalis G1 Ultra")
                .display(1260, 2844, 460, 120).ltpo(true).hdr10Plus(true)
                .battery(6510, 90).ramMb(16384).build());

        register(DeviceProfile.builder()
                .id("magic8p").marketName("Magic 8 Pro")
                .brand("HONOR").manufacturer("HONOR").model("MGI-NX9")
                .device("magic8pro").product("magic8pro").hardware("qcom")
                .fingerprint("HONOR/magic8pro/magic8pro:16/HONORMagic8Pro/9.0.0.150C636:user/release-keys")
                .socName("Snapdragon 8 Elite Gen 5").gpuVendor("Qualcomm").gpuRenderer("Adreno 840")
                .display(1280, 2800, 460, 120).ltpo(true).hdr10Plus(true)
                .battery(5850, 100).thermalBypass(true).fpsUnlock(true).ramMb(16384).build());

        register(DeviceProfile.builder()
                .id("xp1vii").marketName("Xperia 1 VII")
                .brand("Sony").manufacturer("Sony").model("XQ-FS72")
                .device("xperia1vii").product("xperia1vii").hardware("qcom")
                .fingerprint("Sony/Xperia1VII/Xperia1VII:16/BP2A.250605.015.A1/69.I.2.47:user/release-keys")
                .socName("Snapdragon 8 Elite Gen 5").gpuVendor("Qualcomm").gpuRenderer("Adreno 840")
                .display(1644, 3840, 520, 120).ltpo(true).hdr10Plus(true)
                .battery(5000, 30).ramMb(16384).build());

        register(DeviceProfile.builder()
                .id("np3").marketName("Nothing Phone (3)")
                .brand("Nothing").manufacturer("Nothing").model("A065")
                .device("asteroid").product("asteroid").hardware("qcom")
                .fingerprint("Nothing/asteroid/asteroid:16/BP2A.250605.015/3.5.1:user/release-keys")
                .socName("Snapdragon 8s Gen 4").gpuVendor("Qualcomm").gpuRenderer("Adreno 825")
                .display(1264, 2800, 460, 120).ltpo(true)
                .battery(5150, 50).ramMb(12288).build());

        register(DeviceProfile.builder()
                .id("edge50u").marketName("Edge 50 Ultra")
                .brand("motorola").manufacturer("Motorola").model("motorola edge 50 ultra")
                .device("racer").product("racer_global").hardware("qcom")
                .fingerprint("motorola/racer/racer:16/U1RKS36.63-18-2/9b1f3c:user/release-keys")
                .socName("Snapdragon 8s Gen 3").gpuVendor("Qualcomm").gpuRenderer("Adreno 735")
                .display(1220, 2712, 460, 144).ltpo(true).hdr10Plus(true)
                .battery(4500, 125).ramMb(16384).build());
    }

    private static void register(DeviceProfile p) { PROFILES.put(p.getId(), p); }

    // P1: Registro en runtime
    public static synchronized void registerRuntime(DeviceProfile p) { PROFILES.put(p.getId(), p); }

    public static List<DeviceProfile> all() {
        return Collections.unmodifiableList(new ArrayList<>(PROFILES.values()));
    }
    public static DeviceProfile byId(String id) { return PROFILES.get(id); }
    public static List<DeviceProfile> byCategory(DeviceProfile.Category category) {
        List<DeviceProfile> out = new ArrayList<>();
        for (DeviceProfile p : PROFILES.values()) if (p.getCategory() == category) out.add(p);
        return Collections.unmodifiableList(out);
    }
    public static List<DeviceProfile> nativeCapable() {
        List<DeviceProfile> out = new ArrayList<>();
        for (DeviceProfile p : PROFILES.values()) if (p.supportsNativeSpoof()) out.add(p);
        return Collections.unmodifiableList(out);
    }
    public static List<DeviceProfile> quickPick() {
        List<DeviceProfile> out = new ArrayList<>();
        for (String id : Arrays.asList("ip17pm", "ip17p", "ipair", "s26u", "s26p")) {
            DeviceProfile p = PROFILES.get(id); if (p != null) out.add(p);
        }
        return Collections.unmodifiableList(out);
    }
    public static DeviceProfile defaultProfile() { return PROFILES.get("iqoo15"); }
    public static int size() { return PROFILES.size(); }
    private DeviceCatalog() {}
}
