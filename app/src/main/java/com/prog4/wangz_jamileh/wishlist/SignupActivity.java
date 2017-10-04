package com.prog4.wangz_jamileh.wishlist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.prog4.wangz_jamileh.wishlist.R;
import com.prog4.wangz_jamileh.wishlist.error.InputCheck;
import com.prog4.wangz_jamileh.wishlist.magic.Ajax;

import java.util.Map;
import java.util.TreeMap;

public class SignupActivity extends AppCompatActivity {
    private EditText emailView, pwView_1, pwView_2, unameView, fnameView, lnameView, phoneView;

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

        Button signup = (Button) findViewById(R.id.signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });
    }

    private boolean signup() {
        cleanError();
        boolean pass = false, cancel = true;
        String email = emailView.getText().toString(),
                pw_1 = pwView_1.getText().toString(),
                pw_2 = pwView_2.getText().toString(),
                uname = unameView.getText().toString(),
                fname = fnameView.getText().toString(),
                lname = lnameView.getText().toString(),
                phone = phoneView.getText().toString();

//        if(!InputCheck.empty(email)){
//            if(InputCheck.isEmailValid(email)){
//                focusView = InputCheck.error(emailView, "Please enter valid email...");
//                cancel = false;
//            }
//        }
//        else focusView = InputCheck.error(emailView, getString(R.string.error_field_required));

        View focusView = checkSignup(email, "please enter valid email");

        if (focusView != null) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            //AJAX CALL
            TreeMap<String, String> params = new TreeMap<>();
//            params.put("nick_name", email);
//            params.put("password", password);
            Ajax ajax = new Ajax();
            ajax.post("/newUser", params);
            Map<String, Object> res = ajax.response();
            if (res.containsKey("token")) {
//                showProgress(true);
                String token = res.get("token").toString();
                Log.i("token", token);
                Intent i = new Intent(getBaseContext(), DashboardActivity.class);
                i.putExtra("session", token);
                startActivity(i);
                finish();
            } else {
                focusView = InputCheck.error(emailView, "Email exists...");
                focusView.requestFocus();
            }
        }

        return pass;
    }

    private void cleanError(){
        emailView.setError(null);
        pwView_1.setError(null);
        pwView_2.setError(null);
        unameView.setError(null);
        fnameView.setError(null);
        lnameView.setError(null);
        phoneView.setError(null);
    }

    private View checkSignup(String input, String msg){
        View focusView = null;
        if(!InputCheck.empty(input)){
            if(!InputCheck.isEmailValid(input)){
                focusView = InputCheck.error(emailView, msg);
            }
        }
        else focusView = InputCheck.error(emailView, getString(R.string.error_field_required));
        return focusView;
    }

}
