package com.khanhnguyen9872.gltools_mod_xposed.hooks;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedHelpers;

public class PremiumHook {

    public static void hook(ClassLoader classLoader) {
        // Hook com.superpaninbros.glng.Utils$a -> a(Context)
        XposedHelpers.findAndHookMethod(
                "com.superpaninbros.glng.Utils$a",
                classLoader,
                "a",
                Context.class,
                new XC_MethodReplacement() {
                    @Override
                    protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
                        // Return com.superpaninbros.glng.Utils$a$a.OK
                        Class<?> enumClass = classLoader.loadClass("com.superpaninbros.glng.Utils$a$a");
                        @SuppressWarnings("unchecked")
                        Object okEnum = Enum.valueOf((Class<Enum>) enumClass, "OK");
                        return okEnum;
                    }
                }
        );

        // Hook CoinsManager e() to return true unconditionally
        try {
            XposedHelpers.findAndHookMethod(
                "h.h.a.i.i",
                classLoader,
                "e",
                de.robv.android.xposed.XC_MethodReplacement.returnConstant(true)
            );
        } catch (Throwable t) {
            // Ignore if class changes
        }

        // Hide "Get companion" button explicitly when sidebar interacts
        try {
            XposedHelpers.findAndHookMethod(
                "com.superpaninbros.glng.ui.MainActivity$c",
                classLoader,
                "a",
                android.view.View.class,
                float.class,
                new de.robv.android.xposed.XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        Object navigationView = XposedHelpers.getObjectField(param.thisObject, "c");
                        if (navigationView != null) {
                            android.view.Menu menu = (android.view.Menu) XposedHelpers.callMethod(navigationView, "getMenu");
                            if (menu != null) {
                                android.view.MenuItem item = menu.findItem(0x7f09011f);
                                if (item != null) {
                                    item.setVisible(false);
                                }
                            }
                        }
                    }
                }
            );
        } catch (Throwable t) {
            // Ignore if fails
        }
    }
}
