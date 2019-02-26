package com.udacity.ahmed_eid.jobsallapp.Fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.udacity.ahmed_eid.jobsallapp.Model.Company;
import com.udacity.ahmed_eid.jobsallapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


public class MyCompanyProfileFragment extends Fragment {


    @BindView(R.id.profile_comp_name)
    TextView profileCompName;
    @BindView(R.id.profile_comp_category)
    TextView profileCompCategory;
    @BindView(R.id.profile_comp_website)
    TextView profileCompWebsite;
    @BindView(R.id.profile_comp_address)
    TextView profileCompAddress;
    @BindView(R.id.profile_comp_foundedDate)
    TextView profileCompFoundedDate;
    @BindView(R.id.profile_comp_profile)
    TextView profileCompProfile;
    @BindView(R.id.profile_comp_container_layout)
    FrameLayout profileCompContainerLayout;
    @BindView(R.id.comp_logo)
    CircleImageView compLogo;
    @BindView(R.id.comp_add_image_icon)
    CircleImageView compAddImageIcon;

    Unbinder unbinder;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private StorageReference mStorageRef;
    private Uri imageUri = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        getActivity().findViewById(R.id.search_EText).setVisibility(View.GONE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_company_profile, container, false);
        unbinder = ButterKnife.bind(this, view);
        mAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        readCmpanyData();
        return view;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.app_bar_search);
        item.setVisible(false);
    }

    private void readCmpanyData() {
        String compId = mAuth.getCurrentUser().getUid();
        Query query = mDatabase.orderByChild("userId").equalTo(compId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Company company = snapshot.getValue(Company.class);
                        populateUI(company);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void populateUI(Company company) {
        profileCompAddress.setText(company.getCompCountry() + " - " + company.getCompCity());
        profileCompCategory.setText(company.getCompCategory());
        profileCompFoundedDate.append(company.getCompFounderDate());
        profileCompProfile.setText(company.getCompProfile());
        profileCompWebsite.setText(company.getCompWebsite());
        profileCompName.setText(company.getCompName());
        String logo = company.getCompLogo();
        if (!TextUtils.isEmpty(logo)) {
            Glide.with(getActivity())
                    .load(logo)
                    .error(R.drawable.default_logo)
                    .into(compLogo);
        }else {
            compLogo.setImageResource(R.drawable.default_logo);
        }
        String compId = company.getUserId();
        MyAddedJobsFragment fragment = new MyAddedJobsFragment();
        fragment.setCompanyId(compId);
        FragmentManager manager = getFragmentManager();
        manager.beginTransaction().replace(R.id.profile_comp_container_layout, fragment).commit();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.comp_add_image_icon)
    public void onViewClicked() {
        pickImage();
    }

    private void pickImage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getActivity(), "Permission is denied", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            } else {
                BuildImagePicker();
            }

        } else {
            BuildImagePicker();
        }
    }

    private void BuildImagePicker() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(getContext(), MyCompanyProfileFragment.this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageUri = result.getUri();
                compLogo.setImageURI(imageUri);
                uploadImage();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(getActivity(), "Error: " + error, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void uploadImage() {
        String company_id = mAuth.getCurrentUser().getUid();
        final StorageReference image_pathRef = mStorageRef.child("UsersProfileImages").child("CompanyImages").child(company_id + ".jpg");
        image_pathRef.putFile(imageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return image_pathRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    Toast.makeText(getActivity(), "downloadUri: " + downloadUri, Toast.LENGTH_SHORT).show();
                    writeImageRefInRealtimeDatabase(downloadUri.toString());
                } else {
                    String error = task.getException().getMessage();
                    Toast.makeText(getActivity(), "Error: " + error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


//                .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
//                if (task.isSuccessful()) {
//                    Uri downloadUrl = task.getResult().getUploadSessionUri();
//                    Toast.makeText(getActivity(), "downloadUri: " + downloadUrl, Toast.LENGTH_SHORT).show();
//                    writeImageRefInRealtimeDatabase(downloadUrl.toString());
//
//                } else {
//
//                }
//            }
//        });

    private void writeImageRefInRealtimeDatabase(String downloadUrl) {
        String company_id = mAuth.getCurrentUser().getUid();
        mDatabase.child(company_id).child("compLogo").setValue(downloadUrl);
    }

}
