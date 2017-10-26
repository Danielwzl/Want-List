package com.prog4.wangz_jamileh.wishlist;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.prog4.wangz_jamileh.wishlist.Model.User;
import com.prog4.wangz_jamileh.wishlist.databinding.ActivityChangeProfileBinding;
import com.prog4.wangz_jamileh.wishlist.magic.Ajax;

import java.util.Map;
import java.util.TreeMap;

public class ChangeProfileActivity extends AppCompatActivity {
    private User user;
    private String dob, gender;
    private EditText dobView;
    private RadioGroup genderView;
    public static final int DIALOG_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_profile);
        user = User.getInstance();
        ActivityChangeProfileBinding bind = DataBindingUtil.setContentView(this, R.layout.activity_change_profile);
        bind.setUser(user);
        dobView = (EditText) findViewById(R.id.changeProfile_dob);
        dobView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_ID);
            }
        });
        genderView = (RadioGroup) findViewById(R.id.changeProfile_gender);
        genderView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRadioButtonClicked(v);
            }
        });
    }

    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_ID) {
            int[] dob = user.getAllfieldsFromDOB();
            return new DatePickerDialog(this, dpickerListner, dob[0], dob[1], dob[2]);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener dpickerListner = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            month += 1;
            dob = year + "/" + month + "/" + dayOfMonth;
            Toast.makeText(ChangeProfileActivity.this, dob, Toast.LENGTH_SHORT).show();
            dobView.setText(dob);
        }
    };

    public void goback(View v) {
        onBackPressed();
        finish();
    }

    public void update(View v) {
        Ajax a = new Ajax();
        TreeMap<String, String> params = new TreeMap<>();
        params.put("id", user.session);
        params.put("nick_name", user.username);
        params.put("fName", user.fname);
        params.put("lName", user.lname);
        params.put("dob", user.dob);
        params.put("gender", user.gender);
        a.post("/updatePersonalInfo", params);
        Map<String, Object> res = a.response();
        if (res.get("status").toString().equals("ok")) {

            Toast.makeText(ChangeProfileActivity.this, "updated", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(ChangeProfileActivity.this, "failed", Toast.LENGTH_SHORT).show();
        }

    }


    public void onRadioButtonClicked(View view) {
        if (view instanceof RadioButton) {
            boolean checked = ((RadioButton) view).isChecked();
            switch (view.getId()) {
                case R.id.changeProfile_male:
                    if (checked) {
                        gender = "male";
                        user.gender = gender;
                    }
                    break;
                case R.id.changeProfile_female:
                    if (checked) {
                        gender = "female";
                        user.gender = gender;
                    }
                    break;
            }
        }
    }
}
