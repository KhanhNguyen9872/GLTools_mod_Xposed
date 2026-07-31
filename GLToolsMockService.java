package com.gltools.offline.server;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * Servicio de foreground que mantiene vivo el servidor mock
 * mientras GLTools está en ejecución.
 */
public class GLToolsMockService extends Service {

    private GLToolsMockServer server;
    private static final String TAG = "GLToolsOffline";

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            server = new GLToolsMockServer(this);
            server.start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
            Log.i(TAG, "Mock server started on port 8765");
        } catch (Exception e) {
            Log.e(TAG, "Failed to start mock server", e);
        }
    }

    @Override
    public void onDestroy() {
        if (server != null) {
            server.stop();
        }
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
