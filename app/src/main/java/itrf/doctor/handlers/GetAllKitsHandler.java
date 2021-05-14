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

import static itrf.doctor.support.Const.ServerUrl;
import static itrf.doctor.support.GeneralUtil.displayToast;

public class GetAllKitsHandler extends AsyncTask<String, String, String> {
    Context ctx;
    AppCompatActivity HPDetailActivity;
    private String Cardno, ProfileType;
    private Spinner Kit_SPN;
    private ArrayAdapter<String> spinnerArrayAdapter;
    private ArrayList<KeyValue> KitsList;
    public JSONArray Kits;

    public GetAllKitsHandler(Context ctx, String Cardno, String ProfileType) {
        this.ctx = ctx;
        HPDetailActivity = (AppCompatActivity) ctx;
        this.Cardno = Cardno;
        this.ProfileType = ProfileType;
        getActivityComponents();
    }

    private void getActivityComponents() {
        Kit_SPN = HPDetailActivity.findViewById(R.id.kit_sp);
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
            response = new ConnectionHandler().sendGetRequest(ServerUrl + "kit");
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
                Kits = (JSONArray) new JSONParser().parse(result);
                KitsList = new ArrayList();
                int KitID = 0;
                String KitTitle = "";
                KeyValue keyValuePair;

                if (Kits.size() > 0) {
                    for (int i = 0; i < Kits.size(); i++) {
                        Log.e("Kit", ((JSONObject) Kits.get(i)).get("name").toString());

                        keyValuePair = new KeyValue(
                                Integer.parseInt(((JSONObject) Kits.get(i)).get("id").toString()),
                                ((JSONObject) Kits.get(i)).get("name").toString()
                        );
                        KitsList.add(keyValuePair);
                    }
                }
                //fill data in spinner
                ArrayAdapter<KeyValue> adapter = new ArrayAdapter<KeyValue>(ctx, android.R.layout.simple_spinner_dropdown_item, KitsList);
                Kit_SPN.setAdapter(adapter);

                KitID = selectApplicableKit(ProfileType);
                KeyValue KitItem = new KeyValue();
                for (int i = 0; i < adapter.getCount(); i++) {
                     KitItem = adapter.getItem(i);
                     if(KitItem.getId()==KitID) {
                        //  Select this Kit in dropdown by default
                         Kit_SPN.setSelection(adapter.getPosition(KitItem));
                     }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            displayToast(ctx, "No kits available");
        }
    }

    private int selectApplicableKit(String PersonType) {
        int ApplicableKitID = 0;
        switch (PersonType) {
            case "free":
                ApplicableKitID = 7;       //  Immunity Booster Kit
                break;
            case "normal":
                ApplicableKitID = 7;       //  Immunity Booster Yog Kit
                break;
            case "student":
                ApplicableKitID = 7;       //  Immunity Booster Yog Kit
                break;
            case "no-diabetes campaign":
                ApplicableKitID = 6;       //  ITRF Diabetes Care yog Kit
                break;
            case "no-cancer campaign":
                ApplicableKitID = 1;       //  Detoxification/Rejuvenation Kit
                break;
            case "immunity growth":
                ApplicableKitID = 7;       //  ITRF Immunity Booster Yog Kit
                break;
            case "parent care":
                ApplicableKitID = 8;       //  Parent Care Yog Kit
                break;
            case "ortho care":
                ApplicableKitID = 9;       //  Ortho Care Yog Kit
                break;
            case "heart care":
                ApplicableKitID = 10;       //  Heart Care Yog Kit
                break;
            case "digestive care":
                ApplicableKitID = 11;       //  Digestive Care Yog Kit
                break;
            case "vision care":
                ApplicableKitID = 12;       //  Vision Care Yog Kit
                break;
            case "gyno care":
                ApplicableKitID = 13;       //  Gyno Care Yog Kit
                break;
        }
    return ApplicableKitID;
    }
}