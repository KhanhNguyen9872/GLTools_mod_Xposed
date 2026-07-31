package com.gltools.offline.server;

import fi.iki.elonen.NanoHTTPD;
import android.content.Context;
import org.json.JSONObject;

/**
 * Servidor HTTP embebido que intercepta tráfico destinado a gltools.app
 * para endpoints no relacionados con verificación de licencia.
 *
 * Alcance: version.js, companion stub.
 * Fuera de alcance: /api/premium (no se implementa endpoint de licencia).
 */
public class GLToolsMockServer extends NanoHTTPD {

    private static final int PORT = 8765;
    private final Context context;

    public GLToolsMockServer(Context context) {
        super(PORT);
        this.context = context;
    }

    @Override
    public Response serve(IHTTPSession session) {
        String uri = session.getUri();

        if (uri.equals("/api/version.js") || uri.contains("version")) {
            return newFixedLengthResponse(
                Response.Status.OK,
                "application/javascript",
                getVersionJs()
            );
        }

        if (uri.contains("companion")) {
            // Stub neutro: no confirma ni deniega estado de licencia.
            // Solo evita que la UI contacte un servidor inexistente.
            return newFixedLengthResponse(
                Response.Status.OK,
                "application/json",
                "{"reachable": false}"
            );
        }

        return newFixedLengthResponse(
            Response.Status.OK,
            "application/json",
            "{"status":"offline"}"
        );
    }

    private String getVersionJs() {
        JSONObject version = new JSONObject();
        try {
            version.put("version", "1.99");
            version.put("min_android", 16);
            version.put("max_android", 34);
            version.put("force_update", false);
            version.put("changelog", "Offline mode active");
            version.put("server_time", System.currentTimeMillis() / 1000);
        } catch (Exception e) {
            // Manejo silencioso para evitar crash en inicialización.
        }
        return "var gltools_version = " + version.toString() + ";";
    }
}
