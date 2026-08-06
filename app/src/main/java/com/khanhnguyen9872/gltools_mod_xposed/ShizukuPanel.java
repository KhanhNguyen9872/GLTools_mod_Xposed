package com.xrevival.gltools26.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xrevival.gltools26.core.ShizukuBridge;

public final class ShizukuPanel {
    private static final int CARD_BG = 0xFF1E1E1E, BTN_BG = 0xFF2A2A2A, BTN_ACT = 0xFFF2F2F2;
    private static final int TEXT_MAIN = 0xFFF2F2F2, TEXT_SUB = 0xFF9E9E9E;
    private static final int OK_COLOR = 0xFF7CD97C, WARN_COLOR = 0xFFFFB74D, ERR_COLOR = 0xFFE57373;

    private ShizukuPanel() {}

    public static View build(final Activity activity) {
        final LinearLayout card = card();
        card.addView(sectionTitle("Modo Shizuku (No Root)"));
        final TextView status = new TextView(activity);
        status.setTextSize(13);
        status.setPadding(0, 0, 0, dp(activity, 12));
        card.addView(status);

        card.addView(actionBtn(activity, "Abrir Shizuku Manager", false, v -> {
            ShizukuBridge.openManager(activity);
            refreshStatus(activity, status);
        }));

        card.addView(divider(activity));
        card.addView(sectionTitle("Acciones sin root"));

        card.addView(actionBtn(activity, "Thermal Override (NONE)", true, v -> {
            ShizukuBridge.ExecResult r = ShizukuBridge.overrideThermalStatus(0);
            toast(activity, "Thermal: " + (r.ok ? "NONE OK" : "fallo -> " + r.output));
        }));
        card.addView(actionBtn(activity, "RAM Boost (kill-all)", true, v -> {
            ShizukuBridge.ExecResult r = ShizukuBridge.ramBoost();
            toast(activity, "RAM Boost: " + (r.ok ? "OK" : "fallo -> " + r.output));
        }));
        card.addView(actionBtn(activity, "Trim Caches", true, v -> {
            ShizukuBridge.ExecResult r = ShizukuBridge.trimCaches();
            toast(activity, "Trim: " + (r.ok ? "OK" : "fallo -> " + r.output));
        }));
        card.addView(actionBtn(activity, "Reset Thermal", false, v -> {
            ShizukuBridge.ExecResult r = ShizukuBridge.resetThermal();
            toast(activity, "Reset: " + (r.ok ? "OK" : "fallo -> " + r.output));
        }));

        card.addView(divider(activity));
        card.addView(sectionTitle("Setup inalambrico (pairing)"));
        card.addView(guideText(activity, "1) Instala Shizuku desde Play Store.
2) Ajustes -> Acerca del telefono -> toca 7 veces Build Number.
3) Opciones de desarrollador -> activa Depuracion inalambrica.
4) Toca Depuracion inalambrica -> Vincular dispositivo.
5) Abre notificacion de Shizuku -> pega el codigo.
6) Vuelve aqui y pulsa Abrir Shizuku Manager -> concede permiso."));

        final Handler h = new Handler(Looper.getMainLooper());
        Runnable tick = new Runnable() {
            @Override public void run() {
                if (card.isAttachedToWindow()) { refreshStatus(activity, status); h.postDelayed(this, 2000); }
            }
        };
        h.post(tick);
        return card;
    }

    private static void refreshStatus(Context ctx, TextView status) {
        if (!ShizukuBridge.isInstalled(ctx)) { status.setText("Shizuku no instalado"); status.setTextColor(WARN_COLOR); return; }
        if (!ShizukuBridge.isRunning()) { status.setText("Shizuku instalado, servicio detenido"); status.setTextColor(WARN_COLOR); return; }
        if (!ShizukuBridge.hasPermission()) { status.setText("Shizuku activo, falta permiso"); status.setTextColor(WARN_COLOR); ShizukuBridge.requestPermission(42); return; }
        status.setText("Shizuku listo"); status.setTextColor(OK_COLOR);
    }

    private static LinearLayout card() {
        LinearLayout c = new LinearLayout(null);
        c.setOrientation(LinearLayout.VERTICAL);
        c.setPadding(dp(null, 16), dp(null, 8), dp(null, 16), dp(null, 8));
        GradientDrawable gd = new GradientDrawable();
        gd.setCornerRadius(dp(null, 16));
        gd.setColor(CARD_BG);
        c.setBackground(gd);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, dp(null, 12), 0, 0);
        c.setLayoutParams(lp);
        return c;
    }

    private static TextView sectionTitle(String text) {
        TextView t = new TextView(null);
        t.setText(text); t.setTextColor(TEXT_SUB); t.setTextSize(13);
        t.setPadding(0, dp(null, 8), 0, dp(null, 8));
        return t;
    }

    private static View actionBtn(final Context ctx, String label, boolean primary, View.OnClickListener onClick) {
        TextView t = new TextView(ctx);
        t.setText(label); t.setTextSize(14); t.setGravity(Gravity.CENTER);
        t.setPadding(0, dp(ctx, 14), 0, dp(ctx, 14));
        GradientDrawable gd = new GradientDrawable();
        gd.setCornerRadius(dp(ctx, 12));
        gd.setColor(primary ? BTN_ACT : BTN_BG);
        t.setBackground(gd);
        t.setTextColor(primary ? 0xFF111111 : TEXT_MAIN);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, dp(ctx, 6), 0, 0);
        t.setLayoutParams(lp);
        t.setOnClickListener(onClick);
        return t;
    }

    private static View divider(Context ctx) {
        View v = new View(ctx);
        v.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(ctx, 1)));
        v.setBackgroundColor(0xFF2A2A2A);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(ctx, 1));
        lp.setMargins(0, dp(ctx, 12), 0, dp(ctx, 4));
        v.setLayoutParams(lp);
        return v;
    }

    private static TextView guideText(Context ctx, String text) {
        TextView t = new TextView(ctx);
        t.setText(text); t.setTextColor(TEXT_MAIN); t.setTextSize(13);
        t.setLineSpacing(dp(ctx, 4), 1f);
        return t;
    }

    private static void toast(Activity activity, String msg) {
        if (activity == null) return;
        activity.runOnUiThread(() -> Toast.makeText(activity, msg, Toast.LENGTH_LONG).show());
    }

    private static int dp(Context ctx, int v) {
        if (ctx == null) return v;
        return Math.round(v * ctx.getResources().getDisplayMetrics().density);
    }
}
