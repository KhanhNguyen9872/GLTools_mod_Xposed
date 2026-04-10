package com.khanhnguyen9872.gltools_mod_xposed.utils;

public class ResourceProvider {

    public static String getCustomHtml() {
        return "<html><head><meta name=\"viewport\" content=\"width=device-width, initial-scale=1\"><style>" +
                "body { font-family: sans-serif; background-color: rgb(21, 32, 43); color: rgb(255, 255, 255); display: flex; flex-direction: column; justify-content: center; align-items: center; min-height: 90vh; margin: 0; text-align: center; padding: 20px; box-sizing: border-box; } " +
                ".gltools-logo { width: 80px; height: 80px; border-radius: 20px; margin-bottom: 10px; box-shadow: 0 4px 10px rgba(0,0,0,0.5); } " +
                "h2 { color: rgb(0, 230, 118); margin-bottom: 5px; margin-top: 0; } " +
                "h4 { color: rgb(176, 190, 197); margin-top: 0; margin-bottom: 25px; font-weight: normal; } " +
                ".author-box { display: flex; align-items: center; justify-content: center; margin-bottom: 20px; background: rgba(255, 255, 255, 0.05); padding: 10px 20px; border-radius: 30px; box-shadow: 0 2px 5px rgba(0,0,0,0.2); } " +
                ".author-avatar { width: 45px; height: 45px; border-radius: 50%; margin-right: 15px; } " +
                ".author-info { text-align: left; } " +
                ".author-info p { margin: 0; font-size: 14px; color: rgb(220, 220, 220); } " +
                ".author-info a { color: rgb(41, 182, 246); text-decoration: none; font-weight: bold; } " +
                ".card { background: rgba(255, 255, 255, 0.08); padding: 20px; border-radius: 12px; width: 100%; max-width: 320px; font-size: 14px; box-shadow: 0 4px 6px rgba(0,0,0,0.3); } " +
                ".card a.repo { color: rgb(0, 230, 118); font-weight: bold; text-decoration: none; font-size: 15px; display: block; margin-top: 5px; } " +
                ".card a.issue { color: rgb(255, 82, 82); font-weight: bold; text-decoration: none; display: block; margin-top: 5px; background: rgba(255,82,82,0.15); padding: 8px; border-radius: 8px; } " +
                ".footer { margin-top: 30px; font-size: 14px; color: rgb(255, 64, 129); font-weight: bold; opacity: 0.9; } " +
                "</style></head><body>" +
                "<img class=\"gltools-logo\" src=\"https://raw.githubusercontent.com/KhanhNguyen9872/GLTools_mod_Xposed/main/image/logo_gltools.jpg\" onerror=\"this.style.display='none'\"/>" +
                "<h2>GLTools Mod</h2>" +
                "<h4>Premium Graphics Optimizer</h4>" +
                "<div class=\"author-box\">" +
                "<img class=\"author-avatar\" src=\"https://avatars.githubusercontent.com/u/88880309?v=4\" />" +
                "<div class=\"author-info\">" +
                "<p>Modded by <b>KhanhNguyen9872</b></p>" +
                "<p><a href=\"https://github.com/KhanhNguyen9872\">View GitHub Profile</a></p>" +
                "</div></div>" +
                "<div class=\"card\">" +
                "<p style=\"margin-top: 0; margin-bottom: 5px; color: rgb(176,190,197);\"><b>Open Source Project</b></p>" +
                "<a class=\"repo\" href=\"https://github.com/KhanhNguyen9872/GLTools_mod_Xposed\">GLTools_mod_Xposed</a>" +
                "<br/>" +
                "<p style=\"margin:0; color: rgb(200,200,200);\">Experiencing bugs or need help?</p>" +
                "<a class=\"issue\" href=\"https://github.com/KhanhNguyen9872/GLTools_mod_Xposed/issues\">Report an Issue</a>" +
                "</div>" +
                "<div class=\"footer\">Made with ❤️ from Vietnam</div>" +
                "</body></html>";
    }

    public static String getVersionJsResponse() {
        return "{\"url\": {\"arm\": \"https://web.archive.org/web/20240809073954/https://gltools.app/static/files/dl/1/GLTools.apk\", \"x86\": \"https://web.archive.org/web/20240809073954/https://gltools.app/static/files/dl/1/GLTools_x86.apk\"}, \"currentVersion\": 1, \"oldestSecureVersion\": 1, \"currentVersionName\": \"1.0\", \"currentVersionSize\": {\"arm\": 23948086, \"x86\": 25270915}, \"changelog\": \"Release\", \"currentReleaseDate\": \"2021-05-08\"}";
    }

    public static String getEmptyJsonResponse() {
        return "{}";
    }
}
