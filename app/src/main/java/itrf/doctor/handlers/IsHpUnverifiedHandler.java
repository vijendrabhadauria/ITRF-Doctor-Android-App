package itrf.doctor.handlers;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import itrf.doctor.R;
import itrf.doctor.activities.HPDetail;
import itrf.doctor.support.UserPreference;

import static itrf.doctor.support.Const.ServerUrl;
import static itrf.doctor.support.GeneralUtil.displayToast;

public class IsHpUnverifiedHandler extends AsyncTask<String, String, String> {
    Context ctx;
    AppCompatActivity HPDetailActivity;
    private TextView TodaysCount_TV, MonthCount_TV;
    public JSONObject jsonResponse;

    public IsHpUnverifiedHandler(Context ctx) {
        this.ctx = ctx;
        HPDetailActivity = (AppCompatActivity) ctx;
        getActivityComponents();
    }

    private void getActivityComponents() {
    }

    @Override
    protected void onPreExecute() {
//        mprogressbar.setVisibility(View.VISIBLE);
    }

    @Override
    protected String doInBackground(String... params) {
        String cardno = params[0];
        String response = null;
        try {
            JSONObject jsondata = new JSONObject();
            Log.e("URL", ServerUrl + "person/isHpVerifiedByCardno/"+cardno);
//            jsondata.put("key", UserPreference.getProjectKey(ctx));
            response = new ConnectionHandler().sendGetRequest(ServerUrl + "person/isHpVerifiedByCardno/"+cardno);
            return response;
        } catch (Exception E) {
            E.printStackTrace();
            Log.e("Exception", "Get Counts Handler");
        }

        return response;
    }

    private void showCounts() {
        try {
            TodaysCount_TV.setText(jsonResponse.get("todayscount").toString());
            MonthCount_TV.setText(jsonResponse.get("monthscount").toString());
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }
}