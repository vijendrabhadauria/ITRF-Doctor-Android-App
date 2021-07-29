package itrf.doctor.handlers;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;

import itrf.doctor.support.KeyValue;

import static itrf.doctor.support.Const.ServerUrl;
import static itrf.doctor.support.GeneralUtil.displayToast;

public class GetAllReasonsHandler extends AsyncTask<String, String, String> {
    Context ctx;
    AppCompatActivity HPDetailActivity;
    public ArrayAdapter<KeyValue> ReasonsSpinnerAdapter;
    private ArrayList<KeyValue> ReasonsList;
    public JSONArray Reasons;

    public GetAllReasonsHandler(Context ctx) {
        this.ctx = ctx;
        HPDetailActivity = (AppCompatActivity) ctx;
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
            response = new ConnectionHandler().sendGetRequest(ServerUrl + "reason");
        } catch (Exception E) {
            E.printStackTrace();
            Log.e("Exception", "Get All Reasons Handler");
        }

        return response;
    }

    @Override
    protected void onPostExecute(String result) {
        if ((result != null) && (!result.equals("{}"))) {
//            mprogressbar.setVisibility(View.GONE);
            try {
                Reasons = (JSONArray) new JSONParser().parse(result);
                ReasonsList = new ArrayList();
                int ReasonID = 0;
                String ReasonTitle = "";
                KeyValue keyValuePair;

                if (Reasons.size() > 0) {
                    for (int i = 0; i < Reasons.size(); i++) {
                        Log.e("Reason", ((JSONObject) Reasons.get(i)).get("description").toString());

                        keyValuePair = new KeyValue(
                                Integer.parseInt(((JSONObject) Reasons.get(i)).get("reasonId").toString()),
                                ((JSONObject) Reasons.get(i)).get("description").toString()
                        );
                        ReasonsList.add(keyValuePair);
                    }
                }
                //fill data in spinner
                ReasonsSpinnerAdapter = new ArrayAdapter<KeyValue>(ctx, android.R.layout.simple_spinner_dropdown_item, ReasonsList);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            displayToast(ctx, "No reasons available");
        }
    }
}