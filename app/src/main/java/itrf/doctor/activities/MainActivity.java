package itrf.doctor.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.simple.JSONObject;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import itrf.doctor.R;
import itrf.doctor.handlers.ConnectionHandler;
import itrf.doctor.handlers.LoginHandler;
import itrf.doctor.support.GeneralUtil;
import itrf.doctor.support.UserPreference;

import static itrf.doctor.support.Const.ServerUrl;
import static itrf.doctor.support.GeneralUtil.displayToast;
import static itrf.doctor.support.GeneralUtil.generateOtp;

public class MainActivity extends AppCompatActivity {

    EditText mobilenoField, otpField;
    Button nextButton;
    String mobilenoValue, otpValue;
    String generatedOtp, displayMsg;
    LoginHandler DoctorLoginHandler;
    int stage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if ((UserPreference.getUserLoginStatus(MainActivity.this)) && (!isNextDay())) {
            openProfileActivity();
        } else {
            UserPreference.removeUserPreferences(MainActivity.this);
            initiateViews();
            performDefaultOperations();

            nextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    {
                        switch (stage) {
                            case 1:
                                if (validateFieldsForStage1()) {
                                    sendOtp();
                                } else {
                                    displayToast(getApplicationContext(), displayMsg);
                                }
                                break;
                            case 2:
                                if (validateFieldsForStage2()) {
                                    if (
                                            (DoctorLoginHandler.jsonResponse.containsKey("doctorId")) &&
                                                    (DoctorLoginHandler.jsonResponse.containsKey("mobileno")) &&
                                                    (DoctorLoginHandler.jsonResponse.containsKey("fullname"))
                                    ) {
                                        UserPreference.setUserPreferences(
                                                MainActivity.this,
                                                (DoctorLoginHandler.jsonResponse.get("doctorId").toString()),
                                                (DoctorLoginHandler.jsonResponse.get("fullname").toString()),
                                                (DoctorLoginHandler.jsonResponse.get("mobileno").toString())
                                        );
                                        openProfileActivity();
                                    }
//                                    displayToast(getApplicationContext(), "Next screen will open");
                                } else {
                                    displayToast(getApplicationContext(), displayMsg);
                                }
                                break;
                        }
                    }
                }
            });
        }
    }

    void initiateViews() {
        mobilenoField = findViewById(R.id.mobileno_field);
        otpField = findViewById(R.id.otp_field);
        nextButton = findViewById(R.id.next_btn);
    }

    void performDefaultOperations() {
        otpField.setVisibility(View.GONE);
    }

    boolean validateFieldsForStage1() {
        if (mobilenoField.getText().length() == 0) {
            displayMsg = "Please enter your ten digit mobile number";
            return false;
        } else {
            displayMsg = "Please wait..";
            return true;
        }
    }

    private boolean validateFieldsForStage2() {
        Log.e("Entered OTP", otpField.getText().toString());
        Log.e("Generated OTP", generatedOtp);

        if (mobilenoField.getText().length() == 0) {
            displayMsg = "Please enter your ten digit mobile number";
            return false;
        } else if (otpField.getText().length() == 0) {
            displayMsg = "Please enter the received otp";
            return false;
        } else if (!generatedOtp.equals(otpField.getText().toString())) {
            displayMsg = "You have entered incorrect otp";
            return false;
        } else if (generatedOtp.equals(otpField.getText().toString())) {
            return true;
        }
        return false;
    }

    private void sendOtp() {
        generatedOtp = generateOtp();
        mobilenoValue = mobilenoField.getText().toString();

        DoctorLoginHandler = new LoginHandler(MainActivity.this);
        DoctorLoginHandler.execute(mobilenoValue, generatedOtp);
        mobilenoField.setEnabled(false);
        otpField.setVisibility(View.VISIBLE);
        nextButton.setActivated(false);
        stage = 2;
    }

    private void openProfileActivity() {
        finish();
        Intent openProfileIntent = new Intent(MainActivity.this, HPDetail.class);
        startActivity(openProfileIntent);
    }

    private boolean isNextDay() {
        //  Get last used time
        long lastusedtime = UserPreference.getLastUsedTime(MainActivity.this);
        Date d1 = new Date(lastusedtime);

        long currenttime = new Date(System.currentTimeMillis()).getTime();
        Date d2 = new Date(currenttime);

        Log.e("last time", d1.toString());
        Log.e("current time", d2.toString());

        long DateDifference = d2.getTime() - d1.getTime();
        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = DateDifference / daysInMilli;
        DateDifference = DateDifference % daysInMilli;

        long elapsedHours = DateDifference / hoursInMilli;
        DateDifference = DateDifference % hoursInMilli;

        long elapsedMinutes = DateDifference / minutesInMilli;
        DateDifference = DateDifference % minutesInMilli;

        long elapsedSeconds = DateDifference / secondsInMilli;
        Log.e("days", elapsedDays + "");

        //  Set current time as last used time
        UserPreference.setLastUsedTime(MainActivity.this);

        if (elapsedHours >= 6)
            return true;
        else
            return false;
    }
}
