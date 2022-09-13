package itrf.doctor.handlers;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.text.SimpleDateFormat;

import itrf.doctor.R;
import itrf.doctor.activities.HPDetail;
import itrf.doctor.support.UserPreference;

import static itrf.doctor.support.Const.ServerUrl;
import static itrf.doctor.support.Const.doctorAppVersion;
import static itrf.doctor.support.GeneralUtil.displayToast;

public class GetProfileHandler extends AsyncTask<String, String, String> {
    Context ctx;
    GetAllKitsHandler KitHandler;
    AppCompatActivity HPDetailActivity;
    private TextView Cardno_TV, Name_TV, Gender_TV, Age_TV, Disease_TV, Treatment_TV, Kit_TV, interest_level_tv, med_left_curr_tv;
    Button Call_Patient_Btn, Call_Volunteer_Btn;
    public String Mobileno, VolunteerMobileNo;
    private TableRow Kit_Row, interest_level_row, med_left_curr_row;
    public JSONArray jsonResponse;
    public JSONObject Profile;

    public GetProfileHandler(Context ctx) {
        this.ctx = ctx;
        HPDetailActivity = (AppCompatActivity) ctx;
        getActivityComponents();
    }

    private void getActivityComponents() {
        Cardno_TV = HPDetailActivity.findViewById(R.id.cardno_tv);
        Name_TV = HPDetailActivity.findViewById(R.id.name_tv);
        Gender_TV = HPDetailActivity.findViewById(R.id.gender_tv);
//        Category_TV = HPDetailActivity.findViewById(R.id.category_tv);
        Age_TV = HPDetailActivity.findViewById(R.id.age_tv);
        Call_Patient_Btn = HPDetailActivity.findViewById(R.id.call_patient_btn);
        Call_Volunteer_Btn = HPDetailActivity.findViewById(R.id.call_volunteer_btn);
        Disease_TV = HPDetailActivity.findViewById(R.id.disease_tv);
        Treatment_TV = HPDetailActivity.findViewById(R.id.treatment_tv);
        Kit_Row = HPDetailActivity.findViewById(R.id.kit_row);
        Kit_TV = HPDetailActivity.findViewById(R.id.kit_tv);
        interest_level_row = HPDetailActivity.findViewById(R.id.interest_level_row);
        interest_level_tv = HPDetailActivity.findViewById(R.id.interest_level_tv);
        med_left_curr_row = HPDetailActivity.findViewById(R.id.med_left_curr_row);
        med_left_curr_tv = HPDetailActivity.findViewById(R.id.med_left_curr_tv);
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
            //  5 is current doctor app version
            if(UserPreference.getPatientProfileType(ctx).equals("new")) {
                response = new ConnectionHandler().sendGetRequest(ServerUrl + "viewpatientfull/findSinglePatientToPrescribe/" + UserPreference.getDoctorID(this.ctx) + "/" + doctorAppVersion);
            } else if(UserPreference.getPatientProfileType(ctx).equals("old")) {
                response = new ConnectionHandler().sendGetRequest(ServerUrl + "viewpatientfull/findSingleCyclicPatientToPrescribe/" + UserPreference.getDoctorID(this.ctx) + "/" + doctorAppVersion);
            }

            Log.e("RESULT", response);
        } catch (Exception E) {
            E.printStackTrace();
            Log.e("Exception", "Get Profile Handler");
        }

        return response;
    }

    @Override
    protected void onPostExecute(String result) {
        if ((result != null) && (!result.equals("[]")) && (!result.equals("appversion mismatch"))) {
//            mprogressbar.setVisibility(View.GONE);
            Log.e("RESULT", result);
            try {
                jsonResponse = (JSONArray) new JSONParser().parse(result);
                Log.e("Response", jsonResponse.toJSONString());
                Profile = (JSONObject) jsonResponse.get(0);
                showProfile();

                //  Get all the kits and add to kit spinner
                KitHandler = new GetAllKitsHandler(
                        ctx,
                        Profile.get("patientDid").toString(),
                        "1"
                );
                KitHandler.execute();
                Call_Patient_Btn.setEnabled(true);
                Call_Volunteer_Btn.setEnabled(true);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else if ((result.equals("appversion mismatch"))) {
//            Log.e("TRY", "2");
            displayToast(ctx, "Please update your app to continue using it");
        } else {
//            Log.e("TRY", "3");
            Call_Patient_Btn.setEnabled(false);
            Call_Volunteer_Btn.setEnabled(false);
            displayToast(ctx, "No health profile is available");
        }
    }

    private void showProfile() {
        try {
            Mobileno = Profile.get("contactno").toString();
            VolunteerMobileNo = Profile.get("volunteerContactno").toString();
            Cardno_TV.setText(Profile.get("patientDid").toString());
            Name_TV.setText(Profile.get("fullname").toString().toUpperCase());
            Gender_TV.setText(Profile.get("gender").toString().equals("M") ? "Male" : "Female");
            Age_TV.setText(Profile.get("age").toString() + " yrs.");
//            String DOB = Profile.get("dob").toString();
//            SimpleDateFormat SrcSDF = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
//            SimpleDateFormat DestSDF = new SimpleDateFormat("dd-MMM-yyyy");
//            DOB_TV.setText(DestSDF.format(SrcSDF.parse(DOB)));
//            Category_TV.setText(Profile.get("concernName").toString().toUpperCase());
            Disease_TV.setText(Profile.get("problems").toString());
            Treatment_TV.setText(Profile.get("medicinesTaking").toString());

            if(UserPreference.getPatientProfileType(ctx).equals("new")) {
                Kit_Row.setVisibility(View.GONE);
                interest_level_row.setVisibility(View.GONE);
                med_left_curr_row.setVisibility(View.GONE);
            } else if(UserPreference.getPatientProfileType(ctx).equals("old")) {
                Kit_TV.setText(Profile.get("kitName").toString());
                interest_level_tv.setText(Profile.get("interestLevel").toString());
                med_left_curr_tv.setText(Profile.get("medLeftCurr").toString());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}