package itrf.doctor.handlers;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;

import itrf.doctor.R;
import itrf.doctor.support.KeyValue;
import itrf.doctor.support.UserPreference;

import static itrf.doctor.support.Const.ServerUrl;
import static itrf.doctor.support.GeneralUtil.displayToast;

public class PersonStatusHandler extends AsyncTask<String, String, String> {
    Context ctx;

    public PersonStatusHandler(Context ctx) {
        this.ctx = ctx;
    }

    private void getActivityComponents() {

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
            jsondata.put("cardno", params[0]);
            jsondata.put("status", params[1]);
            jsondata.put("key", UserPreference.getProjectKey(ctx));
            response = new ConnectionHandler().sendPostJsonRequest(jsondata, ServerUrl+"person/beginhpverification");
        } catch (Exception E) {
            E.printStackTrace();
            Log.e("Exception", "In Person Status Handler");
        }

        return response;
    }

    @Override
    protected void onPostExecute(String result) {
        Log.e("response", result);
    }
}