package itrf.doctor.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import itrf.doctor.R;
import itrf.doctor.handlers.GetAllReasonsHandler;
import itrf.doctor.handlers.GetCountsHandler;
import itrf.doctor.handlers.GetProfileHandler;
import itrf.doctor.handlers.PersonStatusHandler;
import itrf.doctor.handlers.PrescriptionHandler;
import itrf.doctor.handlers.UpdatePatientStatusHandler;
import itrf.doctor.support.KeyValue;
import itrf.doctor.support.UserPreference;

import static itrf.doctor.support.Const.doctorAppVersion_Display;
import static itrf.doctor.support.GeneralUtil.displayToast;
import static itrf.doctor.support.GeneralUtil.isNetworkAvailable;

public class HPDetail extends AppCompatActivity {

    GetProfileHandler ProfileHandler;
    PersonStatusHandler StatusHandler;
    GetAllReasonsHandler ReasonsHandler;
    GetCountsHandler CountsHandler;
    private TextView DoctorName_TV, appversion;
    private EditText Remarks_ET;
    private Button Call_Patient_Btn, Call_Volunteer_Btn, Prescribe_Btn, Skip_Btn, Reject_Btn;
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
        Kit_SP = findViewById(R.id.kit_sp);
        DoctorName_TV = findViewById(R.id.drname);
        Call_Patient_Btn = findViewById(R.id.call_patient_btn);
        Call_Volunteer_Btn = findViewById(R.id.call_volunteer_btn);
        Prescribe_Btn = findViewById(R.id.prescribe_btn);
        Skip_Btn = findViewById(R.id.skip_btn);
        Reject_Btn = findViewById(R.id.reject_btn);

        appversion = findViewById(R.id.appversion);
        appversion.setText("v." + doctorAppVersion_Display);

//        String ReasonTitle = "";
//        ReasonValue keyValuePair;
    }

    private void setListeners() {

        Skip_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Disable all the Buttons to prevent profile corruption by multiple button presses
                Reject_Btn.setEnabled(false);
                Reject_Btn.setBackgroundColor(getResources().getColor(R.color.colorDisabled));
                Reject_Btn.setText("Reject");

                Prescribe_Btn.setEnabled(false);
                Prescribe_Btn.setBackgroundColor(getResources().getColor(R.color.colorDisabled));
                Prescribe_Btn.setText("Submit Prescription");

                Skip_Btn.setEnabled(false);
                Skip_Btn.setBackgroundColor(getResources().getColor(R.color.colorDisabled));
                Skip_Btn.setText("Please wait..");

                if (isNetworkAvailable(HPDetail.this)) {
                    try {
                        //  Submit request
                        UpdatePatientStatusHandler patientStatusHandler = new UpdatePatientStatusHandler(HPDetail.this);
                        patientStatusHandler.execute(
                                ProfileHandler.Profile.get("patientId").toString(),
                                "false",
                                UserPreference.getDoctorID(HPDetail.this)
                        );
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    displayToast(HPDetail.this, "Check your internet connectivity and try again");
                }
            }
        });

        Reject_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                //  Disable all the Buttons to prevent profile corruption by multiple button presses
//                Reject_Btn.setEnabled(false);
//                Reject_Btn.setBackgroundColor(getResources().getColor(R.color.colorDisabled));
//                Reject_Btn.setText("Please wait..");
//
//                Prescribe_Btn.setEnabled(false);
//                Prescribe_Btn.setBackgroundColor(getResources().getColor(R.color.colorDisabled));
//                Prescribe_Btn.setText("Submit Prescription");
//
//                Skip_Btn.setEnabled(false);
//                Skip_Btn.setBackgroundColor(getResources().getColor(R.color.colorDisabled));
//                Skip_Btn.setText("Skip");
//
//                if (isNetworkAvailable(HPDetail.this)) {
//                    try {
//                        //  Submit request
//                        UpdatePatientStatusHandler patientStatusHandler = new UpdatePatientStatusHandler(HPDetail.this);
//                        patientStatusHandler.execute(
//                                ProfileHandler.Profile.get("patientId").toString(),
//                                "reject",
//                                UserPreference.getDoctorID(HPDetail.this)
//                        );
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                } else {
//                    displayToast(HPDetail.this, "Check your internet connectivity and try again");
//                }
                askForRejectReason();
            }
        });

        Call_Patient_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkAvailable(HPDetail.this)) {
                    try {
                        initiateCall(ProfileHandler.Mobileno);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    displayToast(HPDetail.this, "Check your internet connectivity and try again");
                }
            }
        });

        Call_Volunteer_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkAvailable(HPDetail.this)) {
                    try {
                        initiateCall(ProfileHandler.VolunteerMobileNo);
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
                    //  Get selected Prescription from spinner
                    KeyValue SelectedKit = (KeyValue) Kit_SP.getSelectedItem();
                    if (SelectedKit.getId() == 0) {
                        Toast.makeText(HPDetail.this, "Please select a Kit", Toast.LENGTH_LONG).show();
                    } else {
                        //  Disable both the Buttons to prevent profile corruption by multiple button presses
                        Reject_Btn.setEnabled(false);
                        Reject_Btn.setBackgroundColor(getResources().getColor(R.color.colorDisabled));
                        Reject_Btn.setText("Reject");

                        Prescribe_Btn.setEnabled(false);
                        Prescribe_Btn.setBackgroundColor(getResources().getColor(R.color.colorDisabled));
                        Prescribe_Btn.setText("Please wait..");

                        Skip_Btn.setEnabled(false);
                        Skip_Btn.setBackgroundColor(getResources().getColor(R.color.colorDisabled));
                        Skip_Btn.setText("Skip Patient");
                        //  Submit prescription
                        PrescriptionHandler prescriptionHandler = new PrescriptionHandler(HPDetail.this);
                        prescriptionHandler.execute(
                                UserPreference.getDoctorID(HPDetail.this),
                                "" + SelectedKit.getId(),
                                ProfileHandler.Profile.get("patientId").toString(),
                                Remarks_ET.getText().toString(),
                                "18",
                                "0"
                        );
                    }
                } else {
                    displayToast(HPDetail.this, "Check your internet connectivity and try again");
                }
            }
        });
    }

    private void performDefaultOperations() {
        //  Hide Remarks field and Prescribe Button
        Remarks_ET.setVisibility(View.GONE);
        Prescribe_Btn.setVisibility(View.GONE);
        Skip_Btn.setVisibility(View.GONE);
        Reject_Btn.setVisibility(View.GONE);

        //  Display Doctor's prescription counts
        DoctorName_TV.setText("Doctor " + UserPreference.getFName(HPDetail.this));
        CountsHandler = new GetCountsHandler(HPDetail.this);
        CountsHandler.execute();

        //  Get all reasons for reject and store in a field inside GetAllReasonsHandler class
        ReasonsHandler = new GetAllReasonsHandler(HPDetail.this);
        ReasonsHandler.execute();
    }

    public void initiateCall(String numberToDial) {
        //  Show Remarks field and Prescribe Button
        Remarks_ET.setVisibility(View.VISIBLE);
        Prescribe_Btn.setVisibility(View.VISIBLE);
        Skip_Btn.setVisibility(View.VISIBLE);
        Reject_Btn.setVisibility(View.VISIBLE);

        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", numberToDial, null));
        startActivity(intent);
    }

    public void askForRejectReason() {
        AlertDialog.Builder builder = new AlertDialog.Builder(HPDetail.this);
//        builder.setCancelable(false);
        builder.setTitle("Please select a reason");
        Reason_SP = new Spinner(this);
        Reason_SP.setAdapter(ReasonsHandler.ReasonsSpinnerAdapter);
        builder.setView(Reason_SP);

        builder.setPositiveButton("Reject", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                  disappear the dialog
//  Disable all the Buttons to prevent profile corruption by multiple button presses
                ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEGATIVE).setEnabled(false);
                Reject_Btn.setEnabled(false);
                Reject_Btn.setBackgroundColor(getResources().getColor(R.color.colorDisabled));
                Reject_Btn.setText("Please wait..");

                Prescribe_Btn.setEnabled(false);
                Prescribe_Btn.setBackgroundColor(getResources().getColor(R.color.colorDisabled));
                Prescribe_Btn.setText("Submit Prescription");

                Skip_Btn.setEnabled(false);
                Skip_Btn.setBackgroundColor(getResources().getColor(R.color.colorDisabled));
                Skip_Btn.setText("Skip");

                if (isNetworkAvailable(HPDetail.this)) {
                    try {
                        //  Get selected Prescription from spinner
                        KeyValue SelectedReason = (KeyValue) Reason_SP.getSelectedItem();

                        //  Submit request
                        UpdatePatientStatusHandler patientStatusHandler = new UpdatePatientStatusHandler(HPDetail.this);
                        patientStatusHandler.execute(
                                ProfileHandler.Profile.get("patientId").toString(),
                                "reject",
                                UserPreference.getDoctorID(HPDetail.this),
                                "" + SelectedReason.getId()
                        );
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    displayToast(HPDetail.this, "Check your internet connectivity and try again");
                }

                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

}
