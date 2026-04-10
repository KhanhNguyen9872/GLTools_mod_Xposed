package com.khanhnguyen9872.gltools_mod_xposed;

import com.khanhnguyen9872.gltools_mod_xposed.hooks.NetworkHook;
import com.khanhnguyen9872.gltools_mod_xposed.hooks.PremiumHook;
import com.khanhnguyen9872.gltools_mod_xposed.hooks.WebViewHook;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class MainModule implements IXposedHookLoadPackage {

    @Override
    public void handleLoadPackage(LoadPackageParam lpparam) throws Throwable {
        if (!lpparam.packageName.equals("com.superpaninbros.glng")) {
            return;
        }

        // Initialize all module hooks
        WebViewHook.hook(lpparam.classLoader);
        NetworkHook.hook(lpparam.classLoader);
        PremiumHook.hook(lpparam.classLoader);
    }
}