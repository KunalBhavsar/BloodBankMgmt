package co.project.bloodbankmgmt.utils;

/**
 * Created by Kunal on 17/03/17.
 */

public class KeyConstants {

    public static final String FIREBASE_URL = "https://bloodbankmgmt.firebaseio.com/";

    public static final String MASTER_DATA_URL = FIREBASE_URL + "master/";
    public static final String VARIABLE_DATA_URL = FIREBASE_URL + "variable/";

    public static final String BLOOD_GROUPS_URL = MASTER_DATA_URL + "bloodGroups";
    public static final String USERS_URL = MASTER_DATA_URL + "users";
    public static final String BLOOD_BANK_URL = MASTER_DATA_URL + "bloodBank";
    public static final String BLOOD_REQUESTS_URL = MASTER_DATA_URL + "bloodRequests";
}
