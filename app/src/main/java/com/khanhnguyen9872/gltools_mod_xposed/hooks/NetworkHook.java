package com.khanhnguyen9872.gltools_mod_xposed.hooks;

import com.khanhnguyen9872.gltools_mod_xposed.utils.ResourceProvider;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;

public class NetworkHook {

    public static void hook(ClassLoader classLoader) {
        // Hook hàm khởi tạo OkHttpClient(OkHttpClient.Builder) -> j.c0(j.c0$b)
        // Để chủ động nhúng tay vào 1 custom Interceptor
        XposedHelpers.findAndHookConstructor(
                "j.c0",
                classLoader,
                "j.c0$b",
                new XC_MethodHook() {
                    @SuppressWarnings("unchecked")
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        Object builder = param.args[0]; // Lấy OkHttpClient.Builder
                        
                        // Lấy mảng List<Interceptor> từ field 'e' của Builder
                        List<Object> interceptors = (List<Object>) XposedHelpers.getObjectField(builder, "e");
                        
                        // Tạo 1 Interceptor (j.z) bằng Java Dynamic Proxy
                        Class<?> interceptorInterface = classLoader.loadClass("j.z");
                        Object customInterceptor = Proxy.newProxyInstance(
                                classLoader,
                                new Class[]{interceptorInterface},
                                new InvocationHandler() {
                                    @Override
                                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                                        // implement method `intercept(Chain chain)`
                                        if ("intercept".equals(method.getName()) || args.length == 1) {
                                            Object chain = args[0]; // RealInterceptorChain (j.l0.h.f)
                                            
                                            // Lấy Request (j.f0) từ chain.e
                                            Object request = XposedHelpers.getObjectField(chain, "e");
                                            
                                            // Lấy URL (j.y) từ request.a
                                            Object httpUrl = XposedHelpers.getObjectField(request, "a");
                                            String urlStr = httpUrl != null ? httpUrl.toString() : "";
                                            
                                            // Chặn nếu URL request tới gltools.app
                                            if (urlStr.contains("gltools.app")) {
                                                String bodyStr = ResourceProvider.getEmptyJsonResponse();
                                                
                                                if (urlStr.contains("version.js")) {
                                                    bodyStr = ResourceProvider.getVersionJsResponse();
                                                }
                                                
                                                // Reflection tạo ResponseBody (j.i0) -> ResponseBody.create(MediaType, String)
                                                Class<?> responseBodyClass = classLoader.loadClass("j.i0");
                                                Class<?> mediaTypeClass = classLoader.loadClass("j.a0");
                                                Object responseBody = XposedHelpers.callStaticMethod(
                                                        responseBodyClass,
                                                        "create",
                                                        new Class[]{mediaTypeClass, String.class},
                                                        null, // null cho MediaType
                                                        bodyStr
                                                );
                                                
                                                // Lấy HTTP_1_1 từ enum j.d0.c
                                                Class<?> protocolClass = classLoader.loadClass("j.d0");
                                                Object protocol = XposedHelpers.getStaticObjectField(protocolClass, "c");
                                                
                                                // Reflection tạo Builder Response -> j.h0$a
                                                Class<?> responseBuilderClass = classLoader.loadClass("j.h0$a");
                                                Object responseBuilder = responseBuilderClass.newInstance();
                                                
                                                // Gán data cho Builder Response theo đúng các field
                                                XposedHelpers.setObjectField(responseBuilder, "a", request); // request
                                                XposedHelpers.setObjectField(responseBuilder, "b", protocol); // protocol
                                                XposedHelpers.setIntField(responseBuilder, "c", 200); // code
                                                XposedHelpers.setObjectField(responseBuilder, "d", "OK"); // message
                                                XposedHelpers.setObjectField(responseBuilder, "g", responseBody); // body
                                                
                                                // Build Response cuối cùng và trả về luôn (bỏ qua network)!
                                                return XposedHelpers.callMethod(responseBuilder, "a");
                                            }
                                            
                                            // Cho đi tiếp tục nếu khác gltools.app -> chain.a(request)
                                            return XposedHelpers.callMethod(chain, "a", request);
                                        }
                                        return null;
                                    }
                                }
                        );
                        
                        // Add vào OkHttpClient Interceptor List để request bị đi qua hàm của mình
                        interceptors.add(customInterceptor);
                    }
                }
        );
    }
}
