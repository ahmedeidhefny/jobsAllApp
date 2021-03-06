package com.udacity.ahmed_eid.jobsallapp.Activities;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.udacity.ahmed_eid.jobsallapp.Fragments.CategoriesFragment;
import com.udacity.ahmed_eid.jobsallapp.Fragments.HomeFragment;
import com.udacity.ahmed_eid.jobsallapp.Fragments.MyAddedJobsFragment;
import com.udacity.ahmed_eid.jobsallapp.Fragments.MyAppliedJobsFragment;
import com.udacity.ahmed_eid.jobsallapp.Fragments.MyCompanyProfileFragment;
import com.udacity.ahmed_eid.jobsallapp.Fragments.MyEmployeeProfileFragment;
import com.udacity.ahmed_eid.jobsallapp.Fragments.MySavedJobsFragment;
import com.udacity.ahmed_eid.jobsallapp.Model.Company;
import com.udacity.ahmed_eid.jobsallapp.Model.Employee;
import com.udacity.ahmed_eid.jobsallapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainScreenWithNavigation extends AppCompatActivity {

    private static final String TAG = "MainScreen";
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.fab_add_job)
    FloatingActionButton fabAddJob;
    @BindView(R.id.navigation_bottom)
    BottomNavigationView navigationBottom;
    @BindView(R.id.navigation_view)
    NavigationView navigationView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.noInternet_layout)
    LinearLayout noInternetLayout;
    @BindView(R.id.progress_bar)
    ContentLoadingProgressBar progressBar;
    @BindView(R.id.progress_layout)
    RelativeLayout progressLayout;
    @BindView(R.id.container_layout)
    FrameLayout containerLayout;
    private CircleImageView navImageView;
    private TextView navName, navJob;

    ActionBarDrawerToggle mToggle;
    private String userType;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private ConnectivityManager conMgr;


    @OnClick({R.id.fab_add_job, R.id.retry_btn})
    public void onViewClicked(View view) {
        if (view.getId() == R.id.fab_add_job) {
            Intent addIntent = new Intent(getApplicationContext(), AddNewJobActivity.class);
            startActivity(addIntent);
        } else if (view.getId() == R.id.retry_btn) {
            startActivity(getIntent());
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen_with_navigation);
        ButterKnife.bind(this);
        setSupportActionBar(toolBar);
        initializeUI();
        if (conMgr.getActiveNetworkInfo() == null
                || !conMgr.getActiveNetworkInfo().isAvailable()
                || !conMgr.getActiveNetworkInfo().isConnected()) {
            toggleError();
        } else {
            mToggle = new ActionBarDrawerToggle(this, drawerLayout, toolBar, R.string.nav_open, R.string.nav_close);
            drawerLayout.addDrawerListener(mToggle);
            mToggle.syncState();
            getUserType();
            setupNavigationContent();
            setupBottomNav();
            setTitle("Home");
            loadFragment(new HomeFragment());
        }
    }


    private void initializeUI() {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        conMgr = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        fabAddJob.setVisibility(View.GONE);
        View header = navigationView.inflateHeaderView(R.layout.nav_header);
        navImageView = header.findViewById(R.id.nav_header_user_image);
        navName = header.findViewById(R.id.nav_header_user_name);
        navJob = header.findViewById(R.id.nav_header_user_job);
        progressLayout.setVisibility(View.VISIBLE);
        progressBar.show();
    }

    private void toggleShowData() {
        progressLayout.setVisibility(View.GONE);
        progressBar.hide();
        noInternetLayout.setVisibility(View.GONE);
        containerLayout.setVisibility(View.VISIBLE);
    }

    private void toggleError() {
        progressLayout.setVisibility(View.GONE);
        progressBar.hide();
        noInternetLayout.setVisibility(View.VISIBLE);
        containerLayout.setVisibility(View.GONE);
    }

    private void getUserType() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            mDatabase.child("Users").child(userId).child("userType").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    userType = dataSnapshot.getValue(String.class);
                    toggleShowData();
                    Log.e(TAG, "getUserType: " + userType);
                    handleMenuVisibleByUserType();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e(TAG, "getUserType:onCancelled " + databaseError.toException());
                }
            });
        }
    }

    private void handleMenuVisibleByUserType() {
        //Menu menuView = navigationView.getMenu();
        Menu menuBottom = navigationBottom.getMenu();
        if (!TextUtils.isEmpty(userType)) {
            switch (userType) {
                case "Employee":
                    //menuView.findItem(R.id.nav_my_job).setVisible(true);
                    Log.i("MainScreen", "emp");
                    menuBottom.removeItem(R.id.nav_my_job);
                    fabAddJob.setVisibility(View.GONE);
                    readEmployeeDataHandleNavHeader();
                    break;
                case "Company":
                    Log.i("MainScreen", "comp");
                    menuBottom.removeItem(R.id.nav_applied_job);
                    fabAddJob.setVisibility(View.VISIBLE);
                    readCompanyDataHandleNavHeader();
                    break;
                default:
                    Log.e(TAG, "UserType Another Type about Employee/Company");
                    handleNoUserType();
                    break;
            }
        } else {
            Log.e(TAG, "UserType No found: Null");
            handleNoUserType();
        }
    }

    private void readCompanyDataHandleNavHeader() {

        String id = mAuth.getCurrentUser().getUid();
        Query query = mDatabase.child("Users").orderByChild("userId").equalTo(id);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Company company = snapshot.getValue(Company.class);

                        String image = company.getCompLogo();
                        if (!TextUtils.isEmpty(image)) {
                            Glide.with(MainScreenWithNavigation.this)
                                    .load(image)
                                    .error(R.drawable.default_logo)
                                    .into(navImageView);
                        } else {
                            navImageView.setImageResource(R.drawable.default_logo);
                        }
                        navName.setText(company.getCompName());
                        navJob.setText(company.getCompCategory());
                    }
                } else {
                    Log.e(TAG, "company data is empty");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                String massage = error.getMessage();
                Toast.makeText(MainScreenWithNavigation.this, "Error: " + massage, Toast.LENGTH_LONG).show();

            }
        });
    }

    private void readEmployeeDataHandleNavHeader() {
        String id = mAuth.getCurrentUser().getUid();
        Query query = mDatabase.child("Users").orderByChild("userId").equalTo(id);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Employee employee = snapshot.getValue(Employee.class);

                        String sex = employee.getGender();
                        String image = employee.getEmployeeImage();
                        Log.i("MainScreen", "image");
                        if (!TextUtils.isEmpty(image)) {
                            Glide.with(MainScreenWithNavigation.this)
                                    .load(image)
                                    .error(R.drawable.user_profile_default)
                                    .into(navImageView);
                        } else {
                            if (sex.equals("Male")) {
                                navImageView.setImageResource(R.drawable.male);
                            } else if (sex.equals("Female")) {
                                navImageView.setImageResource(R.drawable.female);
                            }
                        }
                        navName.setText(employee.getEmployeeName());
                        navJob.setText(employee.getJobTitle());
                    }
                } else {
                    Log.e(TAG, "employee data is empty");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                String massage = error.getMessage();
                Toast.makeText(MainScreenWithNavigation.this, "Error: " + massage, Toast.LENGTH_LONG).show();

            }
        });
    }

    private void handleNoUserType() {
        Menu menuBottom = navigationBottom.getMenu();
        menuBottom.removeItem(R.id.nav_profile);
        menuBottom.removeItem(R.id.nav_my_job);
        menuBottom.removeItem(R.id.nav_saved_job);
        menuBottom.removeItem(R.id.nav_applied_job);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            goToLoginActivity();
        }
    }

    private void goToLoginActivity() {
        Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(loginIntent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_overflow, menu);
        return true;
    }

    public void selectItemBottomNav(MenuItem item) {
        Fragment fragmentClass;
        switch (item.getItemId()) {
            case R.id.nav_home:
                fragmentClass = new HomeFragment();
                loadFragment(fragmentClass);
                break;
            case R.id.nav_saved_job:
                fragmentClass = new MySavedJobsFragment();
                loadFragment(fragmentClass);
                break;
            case R.id.nav_applied_job:
                fragmentClass = new MyAppliedJobsFragment();
                loadFragment(fragmentClass);
                break;
            case R.id.nav_my_job:
                String id = mAuth.getCurrentUser().getUid();
                MyAddedJobsFragment fragment = new MyAddedJobsFragment();
                fragment.setCompanyId(id);
                loadFragment(fragment);
                break;
            case R.id.nav_profile:
                if (userType.equals("Employee")) {
                    MyEmployeeProfileFragment empProFrg = new MyEmployeeProfileFragment();
                    empProFrg.setEmployeeId(mAuth.getCurrentUser().getUid());
                    //empProFrg.setActivityName(AppConstants.MainActivityScreen);
                    loadFragment(empProFrg);
                } else if (userType.equals("Company")) {
                    MyCompanyProfileFragment compProFrg = new MyCompanyProfileFragment();
                    compProFrg.setCompanyId(mAuth.getCurrentUser().getUid());
                    //compProFrg.setActivityName(AppConstants.MainActivityScreen);
                    loadFragment(compProFrg);
                }
                break;
            default:
                fragmentClass = new HomeFragment();
                loadFragment(fragmentClass);
        }
        setTitle(item.getTitle());
        item.setChecked(true);
    }

    public void selectItemDrawer(MenuItem item) {
        Fragment myFragment;
        switch (item.getItemId()) {
            case R.id.nav_home:
                myFragment = new HomeFragment();
                loadFragment(myFragment);
                break;
            case R.id.nav_categories:
                myFragment = new CategoriesFragment();
                loadFragment(myFragment);
                break;
            case R.id.nav_logout:
                mAuth.signOut();
                goToLoginActivity();
                return;
        }
        setTitle(item.getTitle());
        item.setChecked(true);
        drawerLayout.closeDrawers();
    }

    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container_layout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void setupNavigationContent() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectItemDrawer(item);
                return true;
            }
        });
    }

    public void setupBottomNav() {
        navigationBottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectItemBottomNav(item);
                return false;
            }
        });
    }

}
