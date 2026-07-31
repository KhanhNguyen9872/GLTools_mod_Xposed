// gltools_layer.cpp
// Capa GLES de depuración para Android 10+ (GLES Layers API oficial).
// Alcance: interceptar glGetString y glTexImage2D en apps debuggables.
// Limitación: sin root, solo funciona en apps con android:debuggable="true".

#include <EGL/egl.h>
#include <GLES2/gl2.h>
#include <android/log.h>
#include <string.h>
#include <cstdio>

#define LOG_TAG "GLToolsLayer"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)

typedef const GLubyte* (*PFNGLGETSTRINGPROC)(GLenum name);
typedef void (*PFNGLTEXIMAGE2DPROC)(GLenum target, GLint level, GLint internalformat,
    GLsizei width, GLsizei height, GLint border, GLenum format, GLenum type, const void* pixels);

static PFNGLGETSTRINGPROC next_glGetString = nullptr;
static PFNGLTEXIMAGE2DPROC next_glTexImage2D = nullptr;

struct LayerConfig {
    char fake_gpu[64];
    bool enabled;
} g_config = {};

extern "C" {

void AndroidGLESLayer_Initialize(GLuint* major, GLuint* minor, void* context,
                                  void* (*get_proc_address)(const char* name)) {
    LOGI("GLTools Layer initializing");
    FILE* f = fopen("/sdcard/Android/data/com.superpaninbros.glng/files/layer_config.ini", "r");
    if (f) {
        fscanf(f, "fake_gpu=%63s\n", g_config.fake_gpu);
        int enabled_int = 0;
        fscanf(f, "enabled=%d", &enabled_int);
        g_config.enabled = enabled_int != 0;
        fclose(f);
    }
}

const GLubyte* hook_glGetString(GLenum name) {
    if (!g_config.enabled || !next_glGetString) return next_glGetString(name);
    if (name == GL_RENDERER && strlen(g_config.fake_gpu) > 0) {
        LOGI("Faking GPU to: %s", g_config.fake_gpu);
        return (const GLubyte*)g_config.fake_gpu;
    }
    return next_glGetString(name);
}

void hook_glTexImage2D(GLenum target, GLint level, GLint internalformat,
                        GLsizei width, GLsizei height, GLint border,
                        GLenum format, GLenum type, const void* pixels) {
    if (g_config.enabled) {
        LOGI("Intercepted glTexImage2D: %dx%d, format=%d", width, height, format);
    }
    next_glTexImage2D(target, level, internalformat, width, height, border, format, type, pixels);
}

void* AndroidGLESLayer_GetProcAddress(const char* name, void* (*next)(const char*)) {
    if (strcmp(name, "glGetString") == 0) {
        next_glGetString = (PFNGLGETSTRINGPROC)next(name);
        return (void*)hook_glGetString;
    }
    if (strcmp(name, "glTexImage2D") == 0) {
        next_glTexImage2D = (PFNGLTEXIMAGE2DPROC)next(name);
        return (void*)hook_glTexImage2D;
    }
    return next(name);
}

} // extern "C"
