package com.xrevival.gltools26.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.xrevival.gltools26.core.DeviceCatalog;
import com.xrevival.gltools26.core.DeviceProfile;
import com.xrevival.gltools26.core.ModuleStatus;
import com.xrevival.gltools26.core.SpoofConfig;
import com.xrevival.gltools26.core.UserAgentFactory;
import com.xrevival.gltools26.magisk.MagiskExporter;

import java.util.List;

public class MainActivity extends Activity implements SpoofConfig.OnConfigChangedListener {
    private static final int BG = 0xFF121212, CARD_BG = 0xFF1E1E1E, CHIP_BG = 0xFF2A2A2A, CHIP_SEL = 0xFFF2F2F2;
    private static final int TEXT_MAIN = 0xFFF2F2F2, TEXT_SUB = 0xFF9E9E9E;

    private SpoofConfig cfg;
    private LinearLayout deviceFlow, categoryRow;
    private TextView statusBanner;
    private DeviceProfile.Category selectedCategory = DeviceProfile.Category.PHONE;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cfg = SpoofConfig.init(this);
        cfg.addListener(this);
        getWindow().setStatusBarColor(BG);
        setContentView(buildRoot());
    }

    @Override protected void onDestroy() { cfg.removeListener(this); super.onDestroy(); }
    @Override public void onConfigChanged(String key) { runOnUiThread(this::refreshAll); }

    private View buildRoot() {
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(BG);
        root.addView(buildHeader());
        root.addView(buildStatusBanner());

        ScrollView scroll = new ScrollView(this);
        scroll.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f));
        LinearLayout content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(dp(16), dp(8), dp(16), dp(16));

        content.addView(sectionTitle("Seleccion Rapida"));
        categoryRow = flowRow(); content.addView(categoryRow);
        content.addView(sectionTitle("Dispositivos Populares"));
        deviceFlow = flowRow(); content.addView(deviceFlow);
        content.addView(buildTogglesCard());
        content.addView(buildPerfCard());
        content.addView(buildCustomCard());
        content.addView(ShizukuPanel.build(this)); // P7

        scroll.addView(content);
        root.addView(scroll);
        root.addView(buildBottomBar());
        refreshAll();
        return root;
    }

    private View buildHeader() {
        LinearLayout bar = new LinearLayout(this);
        bar.setOrientation(LinearLayout.HORIZONTAL);
        bar.setGravity(Gravity.CENTER_VERTICAL);
        bar.setPadding(dp(16), dp(20), dp(16), dp(20));
        bar.setBackgroundColor(0xFF181818);
        TextView back = new TextView(this);
        back.setText("<-");
        back.setTextSize(22);
        back.setTextColor(TEXT_MAIN);
        back.setPadding(0, 0, dp(16), 0);
        back.setOnClickListener(v -> finish());
        bar.addView(back);
        TextView title = new TextView(this);
        title.setText("GLTools 2026 XRevival");
        title.setTextSize(20);
        title.setTextColor(TEXT_MAIN);
        title.getPaint().setFakeBoldText(true);
        bar.addView(title);
        return bar;
    }

    private View buildStatusBanner() {
        statusBanner = new TextView(this);
        statusBanner.setPadding(dp(16), dp(8), dp(16), dp(8));
        statusBanner.setTextSize(12);
        return statusBanner;
    }

    private TextView sectionTitle(String text) {
        TextView t = new TextView(this);
        t.setText(text); t.setTextColor(TEXT_SUB); t.setTextSize(13);
        t.setPadding(0, dp(16), 0, dp(8));
        return t;
    }

    private LinearLayout flowRow() {
        LinearLayout wrap = new LinearLayout(this);
        wrap.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        FlowLayout flow = new FlowLayout(this);
        wrap.addView(flow);
        wrap.removeAllViews();
        wrap.setOrientation(LinearLayout.VERTICAL);
        return wrap;
    }

    private void refreshAll() {
        statusBanner.setText(ModuleStatus.describe());
        statusBanner.setTextColor(ModuleStatus.isActive() ? 0xFF7CD97C : 0xFFE57373);
        rebuildCategories(); rebuildDevices();
    }

    private void rebuildCategories() {
        LinearLayout host = (LinearLayout) categoryRow.getChildAt(0);
        if (host == null) { host = new FlowLayout(this); categoryRow.addView(host); }
        final FlowLayout flow = (FlowLayout) host;
        flow.removeAllViews();
        for (DeviceProfile.Category c : DeviceProfile.Category.values()) {
            flow.addView(chip(label(c), c == selectedCategory, v -> { selectedCategory = c; rebuildCategories(); rebuildDevices(); }));
        }
    }

    private void rebuildDevices() {
        LinearLayout host = (LinearLayout) deviceFlow.getChildAt(0);
        if (host == null) { host = new FlowLayout(this); deviceFlow.addView(host); }
        final FlowLayout flow = (FlowLayout) host;
        flow.removeAllViews();
        List<DeviceProfile> list = DeviceCatalog.byCategory(selectedCategory);
        if (list.isEmpty()) list = DeviceCatalog.all();
        final String activeId = cfg.getDeviceId();
        for (final DeviceProfile p : list) {
            flow.addView(chip(p.getMarketName(), p.getId().equals(activeId), v -> cfg.setDeviceId(p.getId())));
        }
    }

    private View chip(String text, boolean selected, View.OnClickListener onClick) {
        TextView t = new TextView(this);
        t.setText(text); t.setTextSize(14);
        t.setPadding(dp(16), dp(10), dp(16), dp(10));
        GradientDrawable gd = new GradientDrawable();
        gd.setCornerRadius(dp(10));
        gd.setColor(selected ? CHIP_SEL : CHIP_BG);
        t.setBackground(gd);
        t.setTextColor(selected ? 0xFF111111 : TEXT_SUB);
        t.setOnClickListener(onClick);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(dp(4), dp(4), dp(4), dp(4));
        t.setLayoutParams(lp);
        return t;
    }

    private String label(DeviceProfile.Category c) {
        switch (c) { case TABLET: return "Tablet"; case DESKTOP: return "Escritorio"; case LAPTOP: return "Portatil"; case WATCH: return "Reloj"; default: return "Telefono"; }
    }

    private View buildTogglesCard() {
        LinearLayout card = card();
        card.addView(toggleRow("Auto-Inicio", "Aplicar spoof al arrancar apps objetivo", cfg.isAutoStart(), v -> cfg.setAutoStart(!cfg.isAutoStart())));
        card.addView(toggleRow("Forzar Viewport de Escritorio", "Cargar paginas con ancho de escritorio", cfg.isForceDesktopViewport(), v -> cfg.setForceDesktopViewport(!cfg.isForceDesktopViewport())));
        card.addView(toggleRow("User-Agent Personalizado", "Anula la cadena generada automaticamente", cfg.isCustomUserAgentEnabled(), v -> {
            if (!cfg.isCustomUserAgentEnabled()) promptText("User-Agent personalizado", cfg.getCustomUserAgent(), s -> cfg.setCustomUserAgent(s));
            cfg.setCustomUserAgentEnabled(!cfg.isCustomUserAgentEnabled());
        }));
        card.addView(toggleRow("Limpiar Cache WebView", "Borrar cache al iniciar para evitar fugas de identidad", cfg.isClearWebViewCache(), v -> cfg.setClearWebViewCache(!cfg.isClearWebViewCache())));
        return card;
    }

    private View buildPerfCard() {
        LinearLayout card = card();
        card.addView(sectionTitle("Rendimiento (iQOO 15 / MOs)"));
        card.addView(toggleRow("Thermal Bypass", "El juego nunca ve throttling termico", cfg.isThermalBypass(), v -> cfg.setThermalBypass(!cfg.isThermalBypass())));
        card.addView(toggleRow("FPS Unlock", "Elevar techo de FPS hasta el maximo elegido", cfg.isFpsUnlock(), v -> cfg.setFpsUnlock(!cfg.isFpsUnlock())));

        LinearLayout fpsRow = new LinearLayout(this);
        fpsRow.setOrientation(LinearLayout.HORIZONTAL);
        fpsRow.setGravity(Gravity.CENTER_VERTICAL);
        final TextView fpsLabel = new TextView(this);
        fpsLabel.setTextColor(TEXT_MAIN); fpsRow.addView(fpsLabel);
        SeekBar seek = new SeekBar(this);
        seek.setMax(210);
        seek.setProgress(cfg.getMaxFps() - 30);
        seek.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar s, int p, boolean user) { fpsLabel.setText(" maxFps: " + (p + 30) + " Hz "); if (user) cfg.setMaxFps(p + 30); }
            public void onStartTrackingTouch(SeekBar s) {}
            public void onStopTrackingTouch(SeekBar s) {}
        });
        fpsLabel.setText(" maxFps: " + cfg.getMaxFps() + " Hz ");
        fpsRow.addView(seek);
        card.addView(fpsRow);

        final Spinner perf = new Spinner(this);
        perf.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, new String[]{"BALANCED", "GAMING", "APEX", "POWERSAVE"}));
        perf.setSelection(cfg.getPerfProfile().ordinal());
        perf.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            public void onItemSelected(android.widget.AdapterView<?> a, View v, int pos, long id) { cfg.setPerfProfile(SpoofConfig.PerfProfile.values()[pos]); }
            public void onNothingSelected(android.widget.AdapterView<?> a) {}
        });
        card.addView(perf);

        // P5: Spinner modo con SHIZUKU
        final Spinner mode = new Spinner(this);
        mode.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, new String[]{"XPOSED", "MAGISK", "SHIZUKU", "BOTH"}));
        mode.setSelection(cfg.getSpoofMode().ordinal());
        mode.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            public void onItemSelected(android.widget.AdapterView<?> a, View v, int pos, long id) { cfg.setSpoofMode(SpoofConfig.SpoofMode.values()[pos]); }
            public void onNothingSelected(android.widget.AdapterView<?> a) {}
        });
        card.addView(mode);
        return card;
    }

    private View buildCustomCard() {
        LinearLayout card = card();
        card.addView(toggleRow("Dispositivo Personalizado", "Configurar manualmente nombre, modelo y resolucion", false, v -> promptCustomDevice()));
        return card;
    }

    private View buildBottomBar() {
        LinearLayout bar = new LinearLayout(this);
        bar.setOrientation(LinearLayout.HORIZONTAL);
        bar.setPadding(dp(16), dp(12), dp(16), dp(12));
        bar.setBackgroundColor(0xFF181818);
        TextView preview = button("> Vista previa", false);
        preview.setOnClickListener(v -> showPreview());
        bar.addView(preview);
        TextView save = button("Guardar", true);
        save.setOnClickListener(v -> save());
        bar.addView(save);
        return bar;
    }

    private TextView button(String text, boolean primary) {
        TextView t = new TextView(this);
        t.setText(text); t.setTextSize(15); t.setGravity(Gravity.CENTER);
        t.setPadding(dp(20), dp(14), dp(20), dp(14));
        GradientDrawable gd = new GradientDrawable();
        gd.setCornerRadius(dp(24));
        gd.setColor(primary ? CHIP_SEL : CHIP_BG);
        t.setBackground(gd);
        t.setTextColor(primary ? 0xFF111111 : TEXT_SUB);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
        lp.setMargins(dp(6), 0, dp(6), 0);
        t.setLayoutParams(lp);
        return t;
    }

    private void showPreview() {
        DeviceProfile p = cfg.getActiveProfile();
        String ua = UserAgentFactory.resolve(cfg, p);
        String msg = "Perfil: " + p.getMarketName() + "
SoC: " + p.getSocName() + " . GPU: " + p.getGpuRenderer() + "
Pantalla: " + p.getWidthPx() + "x" + p.getHeightPx() + " @" + p.getRefreshRateHz() + "Hz " + p.getDensityDpi() + "dpi
Viewport CSS: " + UserAgentFactory.resolveViewportWidthCss(cfg, p) + "px
UA efectivo:
" + ua + "

" + cfg.toSummary() + "
" + ModuleStatus.describe();
        new AlertDialog.Builder(this).setTitle("Vista previa").setMessage(msg).setPositiveButton("OK", null).show();
    }

    private void save() {
        if (cfg.getSpoofMode() != SpoofConfig.SpoofMode.XPOSED) {
            try {
                String path = MagiskExporter.exportZip(this, cfg.getActiveProfile(), cfg);
                Toast.makeText(this, "Guardado + modulo Magisk: " + path, Toast.LENGTH_LONG).show();
                return;
            } catch (Exception e) { Toast.makeText(this, "Export Magisk fallo: " + e.getMessage(), Toast.LENGTH_LONG).show(); }
        }
        Toast.makeText(this, "Configuracion guardada", Toast.LENGTH_SHORT).show();
    }

    private void promptText(String title, String initial, java.util.function.Consumer<String> onOk) {
        final EditText et = new EditText(this);
        et.setText(initial); et.setSingleLine(false);
        new AlertDialog.Builder(this).setTitle(title).setView(et).setPositiveButton("OK", (d, w) -> onOk.accept(et.getText().toString())).setNegativeButton("Cancelar", null).show();
    }

    private void promptCustomDevice() {
        LinearLayout form = new LinearLayout(this);
        form.setOrientation(LinearLayout.VERTICAL);
        form.setPadding(dp(16), dp(8), dp(16), 0);
        final EditText name = field(form, "Nombre comercial (ej: iQOO 15 Ultra)");
        final EditText model = field(form, "Modelo (ej: V2501A)");
        final EditText res = field(form, "Resolucion WxH (ej: 1440x3168)");
        new AlertDialog.Builder(this).setTitle("Dispositivo Personalizado").setView(form).setPositiveButton("Aplicar", (d, w) -> {
            int wp = 1440, hp = 3168;
            try { String[] xy = res.getText().toString().split("x"); wp = Integer.parseInt(xy[0].trim()); hp = Integer.parseInt(xy[1].trim()); } catch (Exception ignored) {}
            DeviceProfile custom = DeviceProfile.builder().id("custom").marketName(name.getText().toString().trim()).brand("custom").manufacturer("Custom").model(model.getText().toString().trim()).device("custom").product("custom").hardware("qcom").fingerprint("custom/custom/custom:16/BP2A.250605.015/1:user/release-keys").display(wp, hp, 480, 120).build();
            DeviceCatalog.registerRuntime(custom);
            cfg.setDeviceId("custom");
            refreshAll();
        }).setNegativeButton("Cancelar", null).show();
    }

    private EditText field(LinearLayout parent, String hint) {
        EditText et = new EditText(this);
        et.setHint(hint); et.setTextColor(TEXT_MAIN); et.setHintTextColor(TEXT_SUB);
        parent.addView(et);
        return et;
    }

    private LinearLayout card() {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(dp(16), dp(8), dp(16), dp(8));
        GradientDrawable gd = new GradientDrawable();
        gd.setCornerRadius(dp(16));
        gd.setColor(CARD_BG);
        card.setBackground(gd);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, dp(12), 0, 0);
        card.setLayoutParams(lp);
        return card;
    }

    private View toggleRow(String title, String subtitle, boolean initial, View.OnClickListener onClick) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(Gravity.CENTER_VERTICAL);
        row.setPadding(0, dp(10), 0, dp(10));
        LinearLayout texts = new LinearLayout(this);
        texts.setOrientation(LinearLayout.VERTICAL);
        texts.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
        TextView t = new TextView(this); t.setText(title); t.setTextColor(TEXT_MAIN); t.setTextSize(15);
        TextView s = new TextView(this); s.setText(subtitle); s.setTextColor(TEXT_SUB); s.setTextSize(12);
        texts.addView(t); texts.addView(s); row.addView(texts);
        Switch sw = new Switch(this);
        sw.setChecked(initial);
        sw.setOnClickListener(v -> { onClick.onClick(v); sw.setChecked(currentState(title)); });
        row.addView(sw);
        return row;
    }

    private boolean currentState(String title) {
        if (title.startsWith("Auto-Inicio")) return cfg.isAutoStart();
        if (title.startsWith("Forzar")) return cfg.isForceDesktopViewport();
        if (title.startsWith("User-Agent")) return cfg.isCustomUserAgentEnabled();
        if (title.startsWith("Limpiar")) return cfg.isClearWebViewCache();
        if (title.startsWith("Thermal")) return cfg.isThermalBypass();
        if (title.startsWith("FPS")) return cfg.isFpsUnlock();
        return false;
    }

    private int dp(int v) { return Math.round(v * getResources().getDisplayMetrics().density); }

    public static class FlowLayout extends ViewGroup {
        public FlowLayout(android.content.Context ctx) { super(ctx); }
        @Override protected void onMeasure(int wSpec, int hSpec) {
            int width = MeasureSpec.getSize(wSpec), x = 0, y = 0, rowH = 0;
            for (int i = 0; i < getChildCount(); i++) {
                View c = getChildAt(i); measureChild(c, wSpec, hSpec);
                int cw = c.getMeasuredWidth(), ch = c.getMeasuredHeight();
                if (x + cw > width) { x = 0; y += rowH; rowH = 0; }
                x += cw; rowH = Math.max(rowH, ch);
            }
            setMeasuredDimension(width, y + rowH);
        }
        @Override protected void onLayout(boolean changed, int l, int t, int r, int b) {
            int width = r - l, x = 0, y = 0, rowH = 0;
            for (int i = 0; i < getChildCount(); i++) {
                View c = getChildAt(i); int cw = c.getMeasuredWidth(), ch = c.getMeasuredHeight();
                if (x + cw > width) { x = 0; y += rowH; rowH = 0; }
                c.layout(x, y, x + cw, y + ch); x += cw; rowH = Math.max(rowH, ch);
            }
        }
    }
}
