package itrf.doctor.handlers;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import itrf.doctor.R;
import itrf.doctor.activities.HPDetail;
import itrf.doctor.support.UserPreference;

import static itrf.doctor.support.Const.ServerUrl;
import static itrf.doctor.support.GeneralUtil.displayToast;

public class PrescriptionHandler extends AsyncTask<String, String, String> {
    Context ctx;
    AppCompatActivity HPDetailActivity;
    Button Prescribe_Btn;

    public PrescriptionHandler(Context ctx) {
        this.ctx = ctx;
        HPDetailActivity = (AppCompatActivity) ctx;
        getActivityComponents();
    }

    private void getActivityComponents() {
        Prescribe_Btn = HPDetailActivity.findViewById(R.id.prescribe_btn);
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
            jsondata.put("doctorId", params[0]);
            jsondata.put("kitId", params[1]);
            jsondata.put("patientId", params[2]);
            jsondata.put("remarks", params[3]);
            jsondata.put("concernId", params[4]);
            jsondata.put("displayRemarks", params[5]);
            response = new ConnectionHandler().sendPostJsonRequest(jsondata, ServerUrl + "prescription/add");
        } catch (Exception E) {
            E.printStackTrace();
            Log.e("Exception", "Prescription Handler");
        }

        return response;
    }

    @Override
    protected void onPostExecute(String result) {
        try {
            if ((result != null) && (result != "")) {
                JSONObject response = (JSONObject) new JSONParser().parse(result);
                if (response.get("status").toString().equals("success")) {
                    displayToast(ctx, "Prescription saved");
                    ((AppCompatActivity)ctx).finish();
                    Intent openProfileIntent = new Intent(ctx, HPDetail.class);     //  Re-open the activity for newFamily
                    openProfileIntent.putExtra("isSubmitted", "true");
                    openProfileIntent.putExtra("fetchType", "sameFamily");
                    ctx.startActivity(openProfileIntent);
                } else if (response.get("status").toString().equals("failure")) {
                    displayToast(ctx, response.get("msg").toString());
                    Prescribe_Btn.setEnabled(true);
                    Prescribe_Btn.setBackgroundColor(ctx.getResources().getColor(R.color.colorBlue));
                    Prescribe_Btn.setText("SUBMIT PRESCRIPTION");

//                    ((AppCompatActivity)ctx).finish();
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
            displayToast(ctx, "The app encountered an internal error, please try again");
        }
//--    This webservice does not send back any response hence following code is commented
//        Log.e("response", result);
//        try {
//            JSONObject jsonresponse = (JSONObject) new JSONParser().parse(result);
//            if(jsonresponse.get("status").equals("success")) {
//                displayToast(ctx, "Prescription submitted successfully");
//            } else {
//                displayToast(ctx, jsonresponse.get("cause").toString());
//            }
//        } catch (ParseException e) {
//            displayToast(ctx, "Prescription could not be submitted");
//        }
    }
}