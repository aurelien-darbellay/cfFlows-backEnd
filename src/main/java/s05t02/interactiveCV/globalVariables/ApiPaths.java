package s05t02.interactiveCV.globalVariables;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ApiPaths {

    //protected paths
    public static final String PROTECTED_BASE_PATH = "/api";
    /// /user path
    public static final String USER_BASE_PATH = PROTECTED_BASE_PATH + "/users/{username}";
    public static final String USER_DASHBOARD_PATH = USER_BASE_PATH + "/dashboard";

    public static final String CREATE_DOC_PATH = USER_BASE_PATH + "/doc"; //newDoc in Body
    public static final String DOC_PATH = CREATE_DOC_PATH + "/{docId}"; //get + update
    public static final String DELETE_DOC_PATH = DOC_PATH + "/delete";

    public static final String CREATE_PV_PATH = USER_BASE_PATH + "/public-view"; // doc in Body
    public static final String PV_PATH = CREATE_PV_PATH + "/{pvId}";

    public static final String ADD_ENTRY_PATH = DOC_PATH + "/entry/add";
    public static final String DELETE_ENTRY_PATH = DOC_PATH + "/entry/delete";
    public static final String UPDATE_ENTRY_PATH = DOC_PATH + "/entry/update";

    public static final String CLOUD_STORAGE_PATH = USER_BASE_PATH + "/cloud-storage";
    public static final String ADMIN_BASE_PATH = PROTECTED_BASE_PATH + "/admin";


    //unprotected path
    public static final String LOGIN_PATH = "/login";
    public static final String REGISTER_PATH = "/register";
    public static final String PUBLIC_VIEWS_PATH = "/public-views"; //add ?id={pv_id}


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
