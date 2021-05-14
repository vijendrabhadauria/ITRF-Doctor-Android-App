package itrf.doctor.handlers;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import itrf.doctor.R;
import itrf.doctor.support.UserPreference;

import static itrf.doctor.support.Const.ServerUrl;
import static itrf.doctor.support.GeneralUtil.displayToast;

public class HaltHPHandler extends AsyncTask<String, String, String> {
    Context ctx;
    AppCompatActivity HPDetailActivity;
    private Button Halt_Btn;

    public HaltHPHandler(Context ctx) {
        this.ctx = ctx;
        HPDetailActivity = (AppCompatActivity) ctx;
        getActivityComponents();
    }

    private void getActivityComponents() {
        Halt_Btn = HPDetailActivity.findViewById(R.id.halt_btn);
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
            jsondata.put("usertype", params[2]);
            jsondata.put("userid", params[3]);
            jsondata.put("reasonid", params[4]);
            jsondata.put("key", UserPreference.getProjectKey(ctx));
            response = new ConnectionHandler().sendPostJsonRequest(jsondata, ServerUrl+"person/sethpashalt");
        } catch (Exception E) {
            E.printStackTrace();
            Log.e("Exception", "In Person Status Handler");
        }
//  16501039D001
        return response;
    }

    @Override
    protected void onPostExecute(String result) {
        try {
            if((!result.equals("")) && (!result.equals("{}")) && (result!=null)) {
                JSONObject response = (JSONObject) new JSONParser().parse(result);
                if (response.get("status").toString().equals("success")) {
                    displayToast(ctx, response.get("msg").toString());
                    ((AppCompatActivity)ctx).finish();
                } else if (response.get("status").toString().equals("failure")) {
                    displayToast(ctx, response.get("cause").toString());
                    Halt_Btn.setEnabled(true);
                    Halt_Btn.setBackgroundColor(ctx.getResources().getColor(R.color.colorTomato));
                    Halt_Btn.setText("HOLD PROFILE");
//                    ((AppCompatActivity)ctx).finish();
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}