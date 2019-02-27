package com.udacity.ahmed_eid.jobsallapp.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.udacity.ahmed_eid.jobsallapp.Model.SocialMedia;
import com.udacity.ahmed_eid.jobsallapp.R;
import com.udacity.ahmed_eid.jobsallapp.Utilites.AppConstants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ContactMeActivity extends AppCompatActivity {

    @BindView(R.id.phone_number)
    TextView phoneNumber;
    @BindView(R.id.whats_app)
    TextView whatsApp;
    @BindView(R.id.mail)
    TextView mail;
    @BindView(R.id.facebok_link)
    TextView facebokLink;
    @BindView(R.id.twitter_link)
    TextView twitterLink;
    @BindView(R.id.linkedin_link)
    TextView linkedinLink;
    @BindView(R.id.btn_close)
    Button btnClose;
    private String employeeId;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth ;
    private SocialMedia socialMedia = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_me);
        ButterKnife.bind(this);
        setTitle("Communication..!");
        mDatabase = FirebaseDatabase.getInstance().getReference().child("SocialMedia");
        mAuth = FirebaseAuth.getInstance();
        receiveDataFromEmpProfile();

    }

    private void receiveDataFromEmpProfile() {
        Intent intent = getIntent();
        if (intent == null) {
            showErrorMassage();
            return;
        }
        if (intent.hasExtra(AppConstants.INTENT_employeeIdKey)) {
            employeeId = intent.getStringExtra(AppConstants.INTENT_employeeIdKey);
            readSocialMediaThisEmployee(employeeId);

        } else {
            showErrorMassage();
        }
    }

    private void showErrorMassage() {
        Toast.makeText(this, "Not Found Data", Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_contact_me, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.app_contact_edit) {
            showPopupEditContact();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (!mAuth.getCurrentUser().getUid().equals(employeeId)) {
            menu.findItem(R.id.app_contact_edit).setVisible(false);
            return true;
        }
        return super.onPrepareOptionsMenu(menu);
    }

    private void showPopupEditContact() {
        final Dialog mDialog = new Dialog(this);
        mDialog.setContentView(R.layout.popup_contact_me);
        Button closeBtn = mDialog.findViewById(R.id.edit_btn_close);
        final EditText phone_ET = mDialog.findViewById(R.id.edit_phone_number);
        final EditText mail_ET = mDialog.findViewById(R.id.edit_mail);
        final EditText whatApp_ET = mDialog.findViewById(R.id.edit_whats_app);
        final EditText facebook_ET = mDialog.findViewById(R.id.edit_facebook_link);
        final EditText twitter_ET = mDialog.findViewById(R.id.edit_twitter_link);
        final EditText linkedIn_ET = mDialog.findViewById(R.id.edit_linkedin_link);

        if (socialMedia != null) {
            phone_ET.setText(socialMedia.getPhoneNumber());
            mail_ET.setText(socialMedia.getMail());
            whatApp_ET.setText(socialMedia.getWhatsApp());
            facebook_ET.setText(socialMedia.getFacebook());
            twitter_ET.setText(socialMedia.getTwitter());
            linkedIn_ET.setText(socialMedia.getLinkedIn());
        }

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = phone_ET.getText().toString();
                String mail = mail_ET.getText().toString();
                String whatsApp = whatApp_ET.getText().toString();
                String facebook = facebook_ET.getText().toString();
                String twitter = twitter_ET.getText().toString();
                String linkedIn = linkedIn_ET.getText().toString();
                SocialMedia socialMedia = new SocialMedia(employeeId, phone, mail, whatsApp, facebook, twitter, linkedIn);
                mDatabase.child(employeeId).setValue(socialMedia).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ContactMeActivity.this, "Edit Your Data Successfully..!", Toast.LENGTH_SHORT).show();
                            mDialog.dismiss();
                            startActivity(getIntent());
                        } else {
                            Toast.makeText(ContactMeActivity.this, "Filed To Edit Your Data!", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        });
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.show();

    }

    private void readSocialMediaThisEmployee(String employeeId) {
        Query query = mDatabase.orderByChild("empId").equalTo(employeeId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        socialMedia = snapshot.getValue(SocialMedia.class);
                        populateUI(socialMedia);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                String massage = error.getMessage();
                Toast.makeText(getApplicationContext(), "Error: " + massage, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void populateUI(SocialMedia socialMedia) {
        checkIsEmpty(socialMedia.getPhoneNumber(),  phoneNumber);
        checkIsEmpty(socialMedia.getMail(),  mail);
        checkIsEmpty(socialMedia.getWhatsApp(), whatsApp);
        checkIsEmpty(socialMedia.getFacebook(), facebokLink);
        checkIsEmpty(socialMedia.getTwitter(), twitterLink);
        checkIsEmpty(socialMedia.getLinkedIn(), linkedinLink);
    }
    private void checkIsEmpty(String text,TextView textView){
        if (TextUtils.isEmpty(text)){
            textView.setText("Unspicified");
        }else {
            textView.setText(text);
        }
    }

    @OnClick(R.id.btn_close)
    public void onViewClicked() {
        finish();
    }
}
