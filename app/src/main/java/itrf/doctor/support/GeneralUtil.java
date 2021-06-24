package itrf.doctor.support;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import static androidx.core.content.ContextCompat.getSystemService;

public class GeneralUtil {

    //  generate and returns a 4 digit integer
    public static String generateOtp() {
        int randomPIN = (int)(Math.random()*9000)+1000;
//        Log.e("otp", ""+randomPIN);
        return String.valueOf(randomPIN);
    }

    public static boolean isNetworkAvailable(Context ctx) {
        ConnectivityManager cm = (ConnectivityManager)ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = (activeNetwork != null) && (activeNetwork.isConnectedOrConnecting());
        return isConnected;
    }

    public static void displayToast(Context ctx, String msg){
        Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show();
    }
}
