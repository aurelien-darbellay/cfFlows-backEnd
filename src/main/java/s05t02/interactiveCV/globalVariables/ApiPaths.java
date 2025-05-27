package s05t02.interactiveCV.globalVariables;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ApiPaths {

    public static final String PROTECTED_BASE_PATH = "/api";
    public static final String USER_BASE_PATH = PROTECTED_BASE_PATH + "/users/{username}";
    public static final String USER_DASHBOARD_PATH = USER_BASE_PATH + "/dashboard";
    public static final String CLOUD_STORAGE_PATH = USER_BASE_PATH + "/cloud-storage";
    public static final String ADMIN_BASE_PATH = PROTECTED_BASE_PATH + "/admin";
    public static final String LOGIN_PATH = "/login";

    private static final String USER_URL_TEMPLATE = "/dashboard/%s";


    public static String extractUserNameFromBaseUserSpaceUrl(String url) {
        String regex = USER_BASE_PATH.replace("{username}", "([^/]+)");
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(url);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
}
