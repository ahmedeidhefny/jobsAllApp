package com.udacity.ahmed_eid.jobsallapp.Activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.udacity.ahmed_eid.jobsallapp.R;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyResumeActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 55;
    private static final int INTENT_REQUEST_CODE = 1;
    private static final String TAG = "MyResumeActivity";
    private Uri fileUri;
    private String url;
    private String pdf ;

    //@BindView(R.id.showFile_webView)
    //WebView showFileWebView;
    @BindView(R.id.pdf_viewer)
    PDFView pdfViewer;
    @BindView(R.id.add_file_btn)
    Button AddFileBtn;

    private StorageReference storageRef;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private DownloadManager downloadManager ;
    private AlertDialog.Builder alertBuilder ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_resume);
        ButterKnife.bind(this);
        setTitle("MyResume File..");
        storageRef = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        alertBuilder  = new AlertDialog.Builder(this);
        checkResumeFileFoundedInDB();
    }

    private void checkResumeFileFoundedInDB() {
        String employeeId = mAuth.getCurrentUser().getUid();
        mDatabase.child("Users").child(employeeId).child("employeeResumeFile").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    AddFileBtn.setVisibility(View.GONE);
                    pdfViewer.setVisibility(View.VISIBLE);
                   pdf = dataSnapshot.getValue(String.class);
//                    try {
//                        url = URLEncoder.encode(pdf, "UTF-8");
//                    } catch (UnsupportedEncodingException e) {
//                        e.printStackTrace();
//                    }
//                    //Uri uri = Uri.parse(pdf);
//                    //showFileWebView.getSettings().setJavaScriptEnabled(true);
//                    //showFileWebView.loadUrl("http://drive.google.com/viewerng/viewer?embedded=true&url=" + url);
                    new RetrivePdfStreame().execute(pdf);

                } else {
                    AddFileBtn.setVisibility(View.VISIBLE);
                    pdfViewer.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                String massage = databaseError.getMessage();
                Toast.makeText(MyResumeActivity.this, "Error: " + massage, Toast.LENGTH_LONG).show();
            }
        });
    }

    public class RetrivePdfStreame extends AsyncTask<String,Void,InputStream>{

        @Override
        protected InputStream doInBackground(String... strings) {
            InputStream inputStream = null ;
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection)  url.openConnection();
                if (urlConnection.getResponseCode() == 200){
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            super.onPostExecute(inputStream);
            pdfViewer.fromStream(inputStream).load();
        }
    }


    @OnClick({R.id.add_file_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.add_file_btn:
                selectAndUploadFile();
                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_myresume,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.app_bar_change:
                selectAndUploadFile();
                break;
            case R.id.app_bar_download:
                alertBuilder.setTitle("Download File !");
                alertBuilder.setMessage("Are You Sure You Want To Download This Resume File ?");
                alertBuilder.setIcon(R.drawable.ic_file_download_black_24dp);
                alertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        downloadFile();
                    }
                });
                alertBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                alertBuilder.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void downloadFile() {
        Uri uri = Uri.parse(pdf);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        long reference  = downloadManager.enqueue(request);
    }

    private void selectAndUploadFile(){
        if (ContextCompat.checkSelfPermission(MyResumeActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            selectFile();
        } else {
            ActivityCompat.requestPermissions(MyResumeActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            selectFile();
        } else {
            Toast.makeText(this, "Please Enable Storage Permission ..", Toast.LENGTH_SHORT).show();
        }
    }

    private void selectFile() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        startActivityForResult(intent, INTENT_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == INTENT_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            fileUri = data.getData();
            uploadFile(fileUri);
        } else {
            Toast.makeText(this, "Please Select a File", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadFile(Uri fileUri) {
        if (fileUri != null) {
            progressDialog.setTitle("Uploading File...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setProgress(0);
            progressDialog.show();
            String employeeId = mAuth.getCurrentUser().getUid();
            final StorageReference filePath = storageRef.child("EmployeesResumesFiles").child(employeeId + ".pdf");
            filePath.putFile(fileUri).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    int currentProgress = (int) (100 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    progressDialog.setProgress(currentProgress);
                }
            }).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return filePath.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadFileUri = task.getResult();
                        writeFilePathToRealTimeDB(downloadFileUri.toString());
                    } else {
                        String error = task.getException().getMessage();
                        Log.e(TAG, "Error: " + error);
                        Toast.makeText(getApplicationContext(), "Filed to Upload The File..", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
            });
        } else {
            Toast.makeText(this, "Please Select a File", Toast.LENGTH_SHORT).show();
        }
    }

    private void writeFilePathToRealTimeDB(String downloadFileUri) {
        String employeeId = mAuth.getCurrentUser().getUid();
        mDatabase.child("Users").child(employeeId).child("employeeResumeFile").setValue(downloadFileUri).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "The File Successfully Uploaded.. ", Toast.LENGTH_SHORT).show();
                } else {
                    String error = task.getException().getMessage();
                    Log.e(TAG, "Error: " + error);
                    Toast.makeText(getApplicationContext(), "Filed to Upload The File..", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
                startActivity(getIntent());
            }
        });
    }

}
