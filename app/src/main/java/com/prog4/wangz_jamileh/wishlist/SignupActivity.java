package com.prog4.wangz_jamileh.wishlist;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.prog4.wangz_jamileh.wishlist.error.InputCheck;
import com.prog4.wangz_jamileh.wishlist.magic.Ajax;

import java.util.Map;
import java.util.TreeMap;

public class SignupActivity extends AppCompatActivity {
    private EditText emailView, pwView_1, pwView_2, unameView, fnameView, lnameView, phoneView, dobView;
    private RadioGroup genderView;
    private String gender, dob;
    public static final int DIALOG_ID = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        emailView = (EditText) findViewById(R.id.sign_email);
        pwView_1 = (EditText) findViewById(R.id.sign_pw);
        pwView_2 = (EditText) findViewById(R.id.confirm_pw);
        unameView = (EditText) findViewById(R.id.sign_uname);
        fnameView = (EditText) findViewById(R.id.sign_fname);
        lnameView = (EditText) findViewById(R.id.sign_lname);
        phoneView = (EditText) findViewById(R.id.sign_phone);
        genderView = (RadioGroup) findViewById(R.id.gender);
        dobView = (EditText) findViewById(R.id.dob);
        Button signup = (Button) findViewById(R.id.signup);

        dobView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_ID);
            }
        });
        genderView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRadioButtonClicked(v);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });
    }

    private void signup() {
        cleanError();
        String email = emailView.getText().toString(),
                pw_1 = pwView_1.getText().toString(),
                pw_2 = pwView_2.getText().toString(),
                uname = unameView.getText().toString(),
                fname = fnameView.getText().toString(),
                lname = lnameView.getText().toString(),
                phone = phoneView.getText().toString();

        InputCheck check = new InputCheck();
        if (check.empty(gender))
            check.error((RadioButton) findViewById(R.id.female), getString(R.string.error_field_required));
        checkSignup(check, phone, "please enter valid phone. eg. 403XXXXXXX", "phone", phoneView);
        if (check.empty(dob)) check.error(dobView, getString(R.string.error_field_required));
        if (check.empty(fname)) check.error(fnameView, getString(R.string.error_field_required));
        if (check.empty(lname)) check.error(lnameView, getString(R.string.error_field_required));
        if (check.empty(uname)) check.error(unameView, getString(R.string.error_field_required));
        if (!check.passwordMatch(pw_1, pw_2)) {
            check.error(pwView_1, "password does not match!");
        } else
            checkSignup(check, pw_1, "password must be at least 6 characters", "password", pwView_1);
        checkSignup(check, email, "please enter valid email", "email", emailView);
        View focusView = check.showErr();

        if (focusView != null) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            //AJAX CALL
            TreeMap<String, String> params = new TreeMap<>();
            params.put("email", email);
            params.put("nick_name", uname);
            params.put("password", pw_1);
            params.put("lName", lname);
            params.put("fName", fname);
            params.put("phone", phone);
            params.put("gender", gender);
            params.put("dob", dob);

            Ajax ajax = new Ajax();
            ajax.post("/newUser", params);
            Map<String, Object> res = ajax.response();
            if (res != null && res.containsKey("token")) {//                showProgress(true);
                String token = res.get("token").toString();
                Log.i("token", token);
                Intent i = new Intent(getBaseContext(), MenuActivity.class);
                i.putExtra("session", token);
                startActivity(i);
                finish();
            } else {
                if(res != null && res.containsKey("exist")){
                    String exist = res.get("exist").toString();
                    if(exist.equals("email"))
                        check.error(emailView, "Email exists...");
                    else
                        check.error(phoneView, "phone exists...");
                    check.showErr().requestFocus();
                }
            }
        }
    }

    private void cleanError() {
        emailView.setError(null);
        pwView_1.setError(null);
        pwView_2.setError(null);
        unameView.setError(null);
        fnameView.setError(null);
        lnameView.setError(null);
        phoneView.setError(null);
    }

    private void checkSignup(InputCheck check, String input, String msg, String type, TextView target) {
        if (!check.empty(input)) {
            switch (type) {
                case "email":
                    if (!check.isEmailValid(input)) {
                        check.error(emailView, msg);
                    }
                    break;
                case "phone":
                    if (!check.isPhoneValid(input)) {
                        check.error(phoneView, msg);
                    }
                    break;
                case "password":
                    if (!check.isPasswordValid(input)) {
                        check.error(pwView_1, msg);
                    }
                    break;
            }
        } else check.error(target, getString(R.string.error_field_required));
    }

    public void onRadioButtonClicked(View view) {
        if (view instanceof RadioButton) {
            boolean checked = ((RadioButton) view).isChecked();
            switch (view.getId()) {
                case R.id.male:
                    if (checked)
                        gender = "male";
                    break;
                case R.id.female:
                    if (checked)
                        gender = "female";
                    break;
            }
        }
    }

    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_ID) {
            return new DatePickerDialog(this, dpickerListner, 2017, 01, 01);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener dpickerListner = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            month += 1;
            dob = year + "/" + month + "/" + dayOfMonth;
            Toast.makeText(SignupActivity.this, dob, Toast.LENGTH_SHORT).show();
            dobView.setText(dob);
        }
    };

}
