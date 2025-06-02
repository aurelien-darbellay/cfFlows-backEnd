package s05t02.interactiveCV.globalVariables;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ApiPaths {

    public static final String BACK_ORIGIN = "http://localhost:8080";
    public static final String FRONT_ORIGIN = "http://localhost:5173";

    //protected paths
    public static final String PROTECTED_BASE_PATH = "/api";
    /// /user path
    public static final String USER_BASE_PATH = PROTECTED_BASE_PATH + "/users/{username}";
    public static final String USER_DASHBOARD_REL = "/dashboard";
    public static final String USER_DASHBOARD_PATH = USER_BASE_PATH + USER_DASHBOARD_REL;
    public static final String USER_DELETE_REL = "/delete";
    public static final String USER_DELETE_PATH = USER_BASE_PATH + USER_DELETE_REL;

    public static final String DOC_REL = "/doc";
    public static final String DOC_PATH = USER_BASE_PATH + DOC_REL; //newDoc in Body
    public static final String DOC_ID_REL = "/{docId}";
    public static final String DOC_ID_PATH = DOC_PATH + DOC_ID_REL; //get + update
    public static final String DELETE_DOC_REL = "/delete";
    public static final String DELETE_DOC_PATH = DOC_ID_PATH + DELETE_DOC_REL;

    public static final String CREATE_PV_PATH = USER_BASE_PATH + "/public-view"; // doc in Body
    public static final String PV_PATH = CREATE_PV_PATH + "/{pvId}";

    public static final String ENTRY_BASE_PATH = DOC_ID_PATH + "/entry";
    public static final String ENTRY_ADD_REL = "/add";
    public static final String ENTRY_DELETE_REL = "/delete";
    public static final String ENTRY_UPDATE_REL= "/update";

    public static final String CLOUD_STORAGE_PATH = USER_BASE_PATH + "/cloud-storage";
    public static final String ADMIN_BASE_PATH = PROTECTED_BASE_PATH + "/admin";


    //unprotected path
    public static final String LOGIN_PATH = "/login";
    public static final String REGISTER_PATH = "/register";
    public static final String PUBLIC_VIEWS_PATH = "/public-views";//add ?id={pv_id}
    public static final String CSRF_TOKEN_PATH = "/csrf";
    public static final String TYPES_CONFIG_PATH = "/config";


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
