package itrf.doctor.handlers;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import itrf.doctor.R;
import itrf.doctor.support.UserPreference;

import static itrf.doctor.support.Const.ServerUrl;
import static itrf.doctor.support.GeneralUtil.displayToast;

public class GetCountsHandler extends AsyncTask<String, String, String> {
    Context ctx;
    AppCompatActivity HPDetailActivity;
    private TextView TodaysCount_TV, MonthCount_TV;
    public JSONObject jsonResponse;

    public GetCountsHandler(Context ctx) {
        this.ctx = ctx;
        HPDetailActivity = (AppCompatActivity) ctx;
        getActivityComponents();
    }

    private void getActivityComponents() {
        TodaysCount_TV = HPDetailActivity.findViewById(R.id.todayscount_tv);
        MonthCount_TV = HPDetailActivity.findViewById(R.id.monthcount_tv);
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
//            jsondata.put("key", UserPreference.getProjectKey(ctx));
            response = new ConnectionHandler().sendGetRequest(ServerUrl + "eventlog/countByDoctor/"+UserPreference.getDoctorID(ctx));
        } catch (Exception E) {
            E.printStackTrace();
            Log.e("Exception", "Get Counts Handler");
        }

        return response;
    }

    @Override
    protected void onPostExecute(String result) {
        if ((result != null) && (!result.equals("{}"))) {
//            mprogressbar.setVisibility(View.GONE);
            try {
                jsonResponse = (JSONObject) new JSONParser().parse(result);
                TodaysCount_TV.setText("Todays count: "+jsonResponse.get("todayscount").toString());
                MonthCount_TV.setText("Month count: "+jsonResponse.get("monthscount").toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            displayToast(ctx, "Counts are unavailable");
        }
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