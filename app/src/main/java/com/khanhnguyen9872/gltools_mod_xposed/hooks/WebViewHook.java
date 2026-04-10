package com.khanhnguyen9872.gltools_mod_xposed.hooks;

import com.khanhnguyen9872.gltools_mod_xposed.utils.ResourceProvider;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;

public class WebViewHook {

    public static void hook(ClassLoader classLoader) {
        // Hook WebView.loadUrl
        XposedHelpers.findAndHookMethod(
                "android.webkit.WebView",
                classLoader,
                "loadUrl",
                String.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        String url = (String) param.args[0];
                        if (url != null && url.contains("gltools.app")) {
                            if (url.contains("help.html")) {
                                Object webView = param.thisObject;
                                XposedHelpers.callMethod(webView, "loadData", ResourceProvider.getCustomHtml(), "text/html", "utf-8");
                                param.setResult(null); // Không gọi loadUrl gốc
                            } else {
                                param.args[0] = "about:blank"; // Chặn load web do domain đã die
                            }
                        }
                    }
                }
        );

        // Hook WebView.loadData
        XposedHelpers.findAndHookMethod(
                "android.webkit.WebView",
                classLoader,
                "loadData",
                String.class, // data
                String.class, // mimeType
                String.class, // encoding
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        String data = (String) param.args[0];
                        if (data != null && data.contains("twitter-timeline") && data.contains("GLToolsApp")) {
                            param.args[0] = ResourceProvider.getCustomHtml();
                        }
                    }
                }
        );
    }
}
