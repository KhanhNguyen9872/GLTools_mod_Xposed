package com.gltools.offline.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Base de datos local para perfiles de configuración por aplicación.
 * Reemplaza la dependencia de sincronización remota.
 */
public class ProfileDatabase extends SQLiteOpenHelper {

    private static final String DB_NAME = "gltools_profiles.db";
    private static final int DB_VERSION = 1;

    public ProfileDatabase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE profiles (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "package_name TEXT NOT NULL UNIQUE," +
            "fake_gpu TEXT," +
            "resolution_scale REAL DEFAULT 1.0," +
            "texture_quality INTEGER DEFAULT 0," +
            "msaa_level INTEGER DEFAULT 0," +
            "shader_optimize INTEGER DEFAULT 0," +
            "fps_counter INTEGER DEFAULT 0" +
            ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Migraciones futuras: implementar según evolución del esquema.
    }
}
