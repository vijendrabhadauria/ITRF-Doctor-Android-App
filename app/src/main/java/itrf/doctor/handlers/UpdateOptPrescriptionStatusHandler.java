package itrf.doctor.handlers;

import static itrf.doctor.support.Const.ServerUrl;
import static itrf.doctor.support.GeneralUtil.displayToast;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import itrf.doctor.R;

public class UpdateOptPrescriptionStatusHandler
        extends AsyncTask<String, String, String> {
    Context ctx;
    AppCompatActivity HPDetailActivity;
    private Button Skip_Btn;

    public UpdateOptPrescriptionStatusHandler(Context ctx) {
        this.ctx = ctx;
        HPDetailActivity = (AppCompatActivity) ctx;
        getActivityComponents();
    }

    private void getActivityComponents() {
        Skip_Btn = HPDetailActivity.findViewById(R.id.skip_btn);
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
            jsondata.put("patientId", params[0]);
            jsondata.put("status", params[1]);
            jsondata.put("doctorId", params[2]);
            jsondata.put("requestor", params[3]);
            response = new ConnectionHandler().sendPostJsonRequest(jsondata, ServerUrl + "patient/updateOptPrescriptionStatus");
        } catch (Exception E) {
            E.printStackTrace();
            Log.e("Exception", "In Update Patient Opt Prescription Status Handler");
        }

        return response;
    }

    @Override
    protected void onPostExecute(String result) {
        try {
            if ((!result.equals("")) && (!result.equals("{}")) && (result != null)) {
                JSONObject response = (JSONObject) new JSONParser().parse(result);
                if (response.get("status").toString().equals("success")) {
                    displayToast(ctx, response.get("msg").toString());
                    ((AppCompatActivity) ctx).finish();
                } else if (response.get("status").toString().equals("failure")) {
                    displayToast(ctx, response.get("cause").toString());
                    Skip_Btn.setEnabled(true);
                    Skip_Btn.setBackgroundColor(ctx.getResources().getColor(R.color.colorTomato));
//                    ((AppCompatActivity)ctx).finish();
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}