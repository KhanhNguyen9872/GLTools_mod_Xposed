# CMakeLists.txt
# Compilación de la capa GLES para Android NDK.
# Requisito: Android NDK r21+ y CMake 3.10+

cmake_minimum_required(VERSION 3.10)
project(GLToolsLayer)

add_library(GLToolsLayer SHARED gltools_layer.cpp)

target_link_libraries(GLToolsLayer
    EGL
    GLESv2
    log
)

# La biblioteca resultante debe empaquetarse en lib/arm64-v8a/ o lib/armeabi-v7a/
# del APK objetivo (no de GLTools), junto con android:debuggable="true".
