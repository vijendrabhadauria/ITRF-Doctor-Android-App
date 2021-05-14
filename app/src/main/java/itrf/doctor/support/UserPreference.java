package itrf.doctor.support;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import java.util.Date;

public class UserPreference {
    static boolean IS_USER_LOGGED_IN = false;
    static String DoctorMDID = null;
    static String DoctorID = null;
    static String FName = null;
    static String MobileNo = null;
    static String District = null;
    static String ProjectKey = null;
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

    //  DoctorMDID
    public static void setDoctorMDID(Context ctx, String doctorMdid) {
        Editor editor = getSharedPreferences(ctx).edit();
        DoctorMDID = doctorMdid;
        editor.putString("doctormdid", DoctorMDID);
        editor.commit();
    }

    public static String getDoctorMDID(Context ctx) {
        return getSharedPreferences(ctx).getString("doctormdid", DoctorMDID);
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

    //  FName
    public static void setFName(Context ctx, String fname) {
        Editor editor = getSharedPreferences(ctx).edit();
        FName = fname;
        editor.putString("fname", FName);
        editor.commit();
    }

    public static String getFName(Context ctx) {
        return getSharedPreferences(ctx).getString("fname", FName);
    }

    //  District
    public static void setDistrict(Context ctx, String district) {
        Editor editor = getSharedPreferences(ctx).edit();
        District = district;
        editor.putString("district", District);
        editor.commit();
    }

    public static String getDistrict(Context ctx) {
        return getSharedPreferences(ctx).getString("district", District);
    }

    //  Project Key
    public static void setProjectKey(Context ctx, String projectkey) {
        Editor editor = getSharedPreferences(ctx).edit();
        ProjectKey = projectkey;
        editor.putString("projectkey", ProjectKey);
        editor.commit();
    }

    public static String getProjectKey(Context ctx) {
        return getSharedPreferences(ctx).getString("projectkey", ProjectKey);
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
    public static void setUserPreferences(Context ctx, String doctorid, String doctormdid, String fname, String mobileno, String district, String projectkey) {
        IS_USER_LOGGED_IN = true;
        DoctorID = doctorid;
        DoctorMDID = doctormdid;
        FName = fname;
        MobileNo = mobileno;
        District = district;
        ProjectKey = projectkey;

        Editor editor = getSharedPreferences(ctx).edit();
        editor.putBoolean("is_logged_in", IS_USER_LOGGED_IN);
        editor.putString("doctorid", DoctorID);
        editor.putString("doctormdid", DoctorMDID);
        editor.putString("fname", FName);
        editor.putString("mobileno", MobileNo);
        editor.putString("district", District);
        editor.putString("projectkey", ProjectKey);
        editor.commit();
    }

    public static boolean getUserLoginStatus(Context ctx) {
        return getSharedPreferences(ctx).getBoolean("is_logged_in", IS_USER_LOGGED_IN);
    }

    public static void removeUserPreferences(Context ctx) {
        Editor editor = getSharedPreferences(ctx).edit();
        IS_USER_LOGGED_IN = false;
        DoctorMDID = null;
        DoctorID = null;
        FName = null;
        MobileNo = null;
        District = null;
        ProjectKey = null;
        LastUsedTime = null;

        editor.clear();
        editor.commit();
    }
}
