package itrf.doctor.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import itrf.doctor.R;
import itrf.doctor.handlers.GetAllReasonsHandler;
import itrf.doctor.handlers.GetCountsHandler;
import itrf.doctor.handlers.GetProfileHandler;
import itrf.doctor.handlers.HaltHPHandler;
import itrf.doctor.handlers.IsHpUnverifiedHandler;
import itrf.doctor.handlers.PersonStatusHandler;
import itrf.doctor.handlers.PrescriptionHandler;
import itrf.doctor.support.KeyValue;
import itrf.doctor.support.UserPreference;

import static itrf.doctor.support.GeneralUtil.displayToast;
import static itrf.doctor.support.GeneralUtil.isNetworkAvailable;

public class HPDetail extends AppCompatActivity {

    GetProfileHandler ProfileHandler;
    PersonStatusHandler StatusHandler;
    GetAllReasonsHandler ReasonsHandler;
    GetCountsHandler CountsHandler;
    private TextView DoctorName_TV;
    private EditText Remarks_ET;
    private Button Call_Btn, Prescribe_Btn, Halt_Btn;
    Spinner Kit_SP, Reason_SP;
    int noOfCallBtnPressed = 0;

    private ArrayAdapter<String> spinnerArrayAdapter;
    private ArrayList<KeyValue> ReasonsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hpdetail);
        initiateViews();
        setListeners();
        performDefaultOperations();

        //  Fetch Person Profile
        ProfileHandler = new GetProfileHandler(HPDetail.this);
        ProfileHandler.execute();
    }

    private void initiateViews() {
        Remarks_ET = findViewById(R.id.remarks_et);
        Prescribe_Btn = findViewById(R.id.prescribe_btn);
        DoctorName_TV = findViewById(R.id.drname);
        Call_Btn = findViewById(R.id.call_btn);
        Kit_SP = findViewById(R.id.kit_sp);
        Halt_Btn = findViewById(R.id.halt_btn);

//        String ReasonTitle = "";
//        ReasonValue keyValuePair;
    }

    private void setListeners() {
        Call_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkAvailable(HPDetail.this)) {
//                    ++noOfCallBtnPressed;
//                    IsHpUnverifiedHandler HpUnverifiedHandler = new IsHpUnverifiedHandler(HPDetail.this);
                    try {
//                        String result = HpUnverifiedHandler.execute(ProfileHandler.Profile.get("cardno").toString()).get();
//                        JSONObject jobj = new JSONObject();
//                        jobj.put("status", "true");
//                        String result = jobj.toString();
//                        Log.e("Result", result);
//                        if ((result != null) && (!result.equals("{}"))) {
//                          mprogressbar.setVisibility(View.GONE);
//                            try {
//                                Log.e("Reponse", result);
//                                JSONObject jsonResponse = (JSONObject) new JSONParser().parse(result);
//                                String status = jsonResponse.get("status").toString();
//                                if (status.equals("true")) {
                        initiateCall();
//                                } else if (status.equals("false")) {
//                                    if ((jsonResponse.get("state").toString().equals("under process")) && (noOfCallBtnPressed > 1)) {
//                                        initiateCall();
//                                    } else if ((jsonResponse.get("state").toString().equals("under process")) && (noOfCallBtnPressed == 1)) {
//                                        displayToast(HPDetail.this, jsonResponse.get("msg").toString());
//                                    } else if (jsonResponse.get("state").toString().equals("processed")) {
//                                        displayToast(HPDetail.this, jsonResponse.get("msg").toString());
//                                    }
//                                }
//                            } catch (ParseException e) {
//                                e.printStackTrace();
//                            }
//                        } else {
//                            displayToast(HPDetail.this, "Please try again");
//                        }
//                    } catch (ExecutionException e) {
//                        e.printStackTrace();
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    displayToast(HPDetail.this, "Check your internet connectivity and try again");
                }
            }
        });

        Prescribe_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkAvailable(HPDetail.this)) {
                    //  Disable both the Buttons to prevent profile corruption by multiple button presses
                    Prescribe_Btn.setEnabled(false);
                    Halt_Btn.setEnabled(false);
                    Prescribe_Btn.setBackgroundColor(getResources().getColor(R.color.colorDisabled));
                    Halt_Btn.setBackgroundColor(getResources().getColor(R.color.colorDisabled));
                    Prescribe_Btn.setText("Please wait..");

                    //  Get selected Prescription from spinner
                    KeyValue SelectedKit = (KeyValue) Kit_SP.getSelectedItem();
//                Log.e("Kit ID", "" + SelectedKit.getId());
//                Log.e("Card no", ProfileHandler.Profile.get("cardno").toString());
//                Log.e("Doctor MD ID", UserPreference.getDoctorMDID(HPDetail.this));
//                Log.e("Remarks", Remarks_ET.getText().toString());
//                Log.e("Key", UserPreference.getProjectKey(HPDetail.this));
//                Log.e("Kit Name", "" + SelectedKit.getTitle());

                    //  Submit prescription
                    PrescriptionHandler prescriptionHandler = new PrescriptionHandler(HPDetail.this);
                    prescriptionHandler.execute(
                            UserPreference.getDoctorMDID(HPDetail.this),
                            "" + SelectedKit.getId(),
                            ProfileHandler.Profile.get("cardno").toString(),
                            Remarks_ET.getText().toString()
                    );
                } else {
                    displayToast(HPDetail.this, "Check your internet connectivity and try again");
                }
            }
        });

        Halt_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkAvailable(HPDetail.this)) {
                    //  Disable both the Buttons to prevent profile corruption by multiple button presses
                    Halt_Btn.setEnabled(false);
                    Prescribe_Btn.setEnabled(false);
                    Halt_Btn.setBackgroundColor(getResources().getColor(R.color.colorDisabled));
                    Prescribe_Btn.setBackgroundColor(getResources().getColor(R.color.colorDisabled));
                    Halt_Btn.setText("Please wait..");

                    // Custom Dialog
                    final Dialog dialog = new Dialog(HPDetail.this);
                    dialog.setContentView(R.layout.dialog_reason);
                    dialog.setTitle("Select a reason");

                    Reason_SP = dialog.findViewById(R.id.reason_spn);
                    Reason_SP.setAdapter(ReasonsHandler.ReasonsSpinnerAdapter);

                    Button Ok_Btn_DLG = dialog.findViewById(R.id.ok_btn_dlg);
                    Button Cancel_Btn_DLG = dialog.findViewById(R.id.cancel_btn_dlg);

                    Ok_Btn_DLG.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int ReasonID = ((KeyValue) Reason_SP.getSelectedItem()).getId();
                            HaltHPHandler HaltHandler = new HaltHPHandler(HPDetail.this);
                            HaltHandler.execute(
                                    ProfileHandler.Profile.get("cardno").toString(),
                                    "halted",
                                    "doctor",
                                    UserPreference.getDoctorMDID(HPDetail.this).toString(),
                                    "" + ReasonID,
                                    UserPreference.getProjectKey(HPDetail.this)
                            );
                            dialog.dismiss();
                            finish();
                        }
                    });

                    Cancel_Btn_DLG.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            Halt_Btn.setEnabled(true);
                            Prescribe_Btn.setEnabled(true);
                            Halt_Btn.setBackgroundColor(getResources().getColor(R.color.colorTomato));
                            Prescribe_Btn.setBackgroundColor(getResources().getColor(R.color.colorBlue));
                            Halt_Btn.setText("HOLD PROFILE");
                        }
                    });

                    dialog.show();
                } else {
                    displayToast(HPDetail.this, "Check your internet connectivity and try again");
                }
            }
        });
    }

    private void performDefaultOperations() {
        //  Hide Remarks field and Prescribe Button
        Remarks_ET.setVisibility(View.INVISIBLE);
        Prescribe_Btn.setVisibility(View.INVISIBLE);
        Halt_Btn.setVisibility(View.INVISIBLE);

        //  Display Doctor related info
        DoctorName_TV.setText("Doctor " + UserPreference.getFName(HPDetail.this));
        CountsHandler = new GetCountsHandler(HPDetail.this);
        CountsHandler.execute();

        //  Get all reasons for halt and store in a field inside GetAllReasonsHandler class
        ReasonsHandler = new GetAllReasonsHandler(HPDetail.this);
        ReasonsHandler.execute();
    }

    public void initiateCall() {
//        StatusHandler = new PersonStatusHandler(HPDetail.this);
//        StatusHandler.execute(ProfileHandler.Profile.get("cardno").toString(), "under process");

        //  Show Remarks field and Prescribe Button
        Remarks_ET.setVisibility(View.VISIBLE);
        Prescribe_Btn.setVisibility(View.VISIBLE);
        Halt_Btn.setVisibility(View.VISIBLE);

        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", ProfileHandler.Mobileno, null));
        startActivity(intent);
    }
}
