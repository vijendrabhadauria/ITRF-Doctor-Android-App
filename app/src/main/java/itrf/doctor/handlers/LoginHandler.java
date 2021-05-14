package itrf.doctor.handlers;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import itrf.doctor.R;

import static itrf.doctor.support.Const.ServerUrl;
import static itrf.doctor.support.GeneralUtil.displayToast;

public class LoginHandler extends AsyncTask<String, String, String> {
    Context ctx;
    AppCompatActivity MainActivity;
    EditText mobilenoField, otpField;
    Button nextButton;
    public JSONObject jsonResponse;

    public LoginHandler(Context ctx) {
        this.ctx = ctx;
        MainActivity = (AppCompatActivity) ctx;
        getActivityComponents();
    }

    private void getActivityComponents() {
        nextButton = MainActivity.findViewById(R.id.next_btn);
        mobilenoField = MainActivity.findViewById(R.id.mobileno_field);
        otpField = MainActivity.findViewById(R.id.otp_field);
    }

    @Override
    protected void onPreExecute() {
//        mprogressbar.setVisibility(View.VISIBLE);
    }

    @Override
    protected String doInBackground(String... params) {
        String response = null;
        try {
            JSONObject jsondata = new JSONObject();
            jsondata.put("mobileno", params[0]);
            jsondata.put("otp", params[1]);
            jsondata.put("platform", "android");

            response = new ConnectionHandler().sendPostJsonRequest(jsondata, ServerUrl + "doctor/login");
            jsonResponse = (JSONObject) new JSONParser().parse(response);
        } catch (Exception E) {
            E.printStackTrace();
            Log.e("Exception", "In Login Handler");
        }

        return response;
    }

    @Override
    protected void onPostExecute(String result) {
        if ((result != null) && (!result.equals("{}"))) {
            nextButton.setActivated(true);
//            mprogressbar.setVisibility(View.GONE);
//            Intent dashboardActivityIntent = new Intent(LoginActivity.this, UploadFileActivity.class);
//            finish();
//            startActivity(dashboardActivityIntent);
        } else {
            displayToast(ctx, "Login failed, please try again");
        }
    }
}