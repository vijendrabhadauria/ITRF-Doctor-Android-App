package itrf.doctor.support;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import java.util.Date;

public class UserPreference {
    static boolean IS_USER_LOGGED_IN = false;
    static String DoctorID = null;
    static String FullName = null;
    static String MobileNo = null;
    static Date LastUsedTime = null;

    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    //  DoctorID
    public static void setDoctorID(Context ctx, String doctorid) {
        Editor editor = getSharedPreferences(ctx).edit();
        DoctorID = doctorid;
        editor.putString("doctorid", DoctorID);
        editor.commit();
    }

    public static String getDoctorID(Context ctx) {
        return getSharedPreferences(ctx).getString("doctorid", DoctorID);
    }

    //  MobileNo
    public static void setMobileNo(Context ctx, String mobileno) {
        Editor editor = getSharedPreferences(ctx).edit();
        MobileNo = mobileno;
        editor.putString("mobileno", MobileNo);
        editor.commit();
    }

    public static String getMobileNo(Context ctx) {
        return getSharedPreferences(ctx).getString("mobileno", MobileNo);
    }

    //  FullName
    public static void setFullName(Context ctx, String fullname) {
        Editor editor = getSharedPreferences(ctx).edit();
        FullName = fullname;
        editor.putString("fullname", FullName);
        editor.commit();
    }

    public static String getFName(Context ctx) {
        return getSharedPreferences(ctx).getString("fullname", FullName);
    }

    //  Last Used Date-Time
    public static void setLastUsedTime(Context ctx) {
        Editor editor = getSharedPreferences(ctx).edit();
        LastUsedTime = new Date(System.currentTimeMillis());
        editor.putLong("lastusedtime", LastUsedTime.getTime());
        editor.commit();
    }

    public static long getLastUsedTime(Context ctx) {
        return getSharedPreferences(ctx).getLong("lastusedtime", new Date(System.currentTimeMillis()).getTime());
    }

    //  UserLogInLogout
    public static void setUserPreferences(Context ctx, String doctorid, String fullname, String mobileno) {
        IS_USER_LOGGED_IN = true;
        DoctorID = doctorid;
        FullName = fullname;
        MobileNo = mobileno;

        Editor editor = getSharedPreferences(ctx).edit();
        editor.putBoolean("is_logged_in", IS_USER_LOGGED_IN);
        editor.putString("doctorid", DoctorID);
        editor.putString("fullname", FullName);
        editor.putString("mobileno", MobileNo);
        editor.commit();
    }

    public static boolean getUserLoginStatus(Context ctx) {
        return getSharedPreferences(ctx).getBoolean("is_logged_in", IS_USER_LOGGED_IN);
    }

    public static void removeUserPreferences(Context ctx) {
        Editor editor = getSharedPreferences(ctx).edit();
        IS_USER_LOGGED_IN = false;
        DoctorID = null;
        FullName = null;
        MobileNo = null;
        LastUsedTime = null;

        editor.clear();
        editor.commit();
    }
}
