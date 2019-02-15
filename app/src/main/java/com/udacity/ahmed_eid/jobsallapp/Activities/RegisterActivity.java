package com.udacity.ahmed_eid.jobsallapp.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.udacity.ahmed_eid.jobsallapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";
    @BindView(R.id.re_user_email)
    EditText reUserEmail;
    @BindView(R.id.re_user_password)
    EditText reUserPassword;
    @BindView(R.id.re_user_password_confirm)
    EditText reUserPasswordConfirm;
    @BindView(R.id.register_btn)
    RelativeLayout registerBtn;

    FirebaseAuth mAuth;
    @BindView(R.id.register_bar)
    ProgressBar registerBar;

    private Dialog myDialog;

    @OnClick(R.id.register_btn)
    public void onViewClicked() {
        createNewUserAccount();
        //goToPopupUserType();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        myDialog = new Dialog(this);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            goToMainActivity();
        }
    }

    private void goToMainActivity() {
        Intent mainIntent = new Intent(getApplicationContext(), MainScreenWithNavigation.class);
        startActivity(mainIntent);
        finish();
    }

    private void createNewUserAccount() {
        String email = reUserEmail.getText().toString();
        String password = reUserPassword.getText().toString();
        String confirmPassword = reUserPasswordConfirm.getText().toString();

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(confirmPassword)) {
            if (password.equals(confirmPassword)) {
                registerBar.setVisibility(View.VISIBLE);
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "AccountCreated:Successfully..", Toast.LENGTH_LONG).show();
                            goToPopupUserType();
                        } else {
                            String massage = task.getException().getMessage();
                            Toast.makeText(getApplicationContext(), "Error: " + massage, Toast.LENGTH_LONG).show();
                        }
                    }
                });
            } else {
                Toast.makeText(this, "Password and ConfirmPassword Field Doesn't Match..!", Toast.LENGTH_SHORT).show();
            }
        }
        registerBar.setVisibility(View.INVISIBLE);
    }

    public void goToPopupUserType() {
        TextView txtClose;
        Button btnNext;
        final RadioGroup radioGroup;
        myDialog.setContentView(R.layout.popup_user_type);
        txtClose = myDialog.findViewById(R.id.text_close);
        btnNext = myDialog.findViewById(R.id.btn_next);
        radioGroup = myDialog.findViewById(R.id.radio_group);
        txtClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int radioButtonId = radioGroup.getCheckedRadioButtonId();
                checkedRadioButton(radioButtonId);
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    public void checkedRadioButton(int radioButtonId) {
        if (radioButtonId == R.id.employee_radioBtn) {
            goToCompanyOrEmployeeRegisterActivity(EmployeeRegisterActivity.class);
        } else if (radioButtonId == R.id.company_radioBtn) {
            goToCompanyOrEmployeeRegisterActivity(CompanyRegisterActivity.class);
        } else {
            Toast.makeText(this, R.string.massage_notSeclect_userType, Toast.LENGTH_LONG).show();
        }
    }

    private void goToCompanyOrEmployeeRegisterActivity(Class destinationActivity) {
        Intent intent = new Intent(getApplicationContext(), destinationActivity);
        startActivity(intent);
    }


}
