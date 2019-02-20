package com.udacity.ahmed_eid.jobsallapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
    private CircleImageView navImageView;
    private TextView navName, navJob;

    private ActionBarDrawerToggle mToggle;
    private String userType;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;


    @OnClick(R.id.fab_add_job)
    public void onViewClicked() {
        Intent addIntent = new Intent(getApplicationContext(), AddNewJobActivity.class);
        startActivity(addIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen_with_navigation);
        ButterKnife.bind(this);
        setSupportActionBar(toolBar);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mToggle = new ActionBarDrawerToggle(this, drawerLayout, toolBar, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        fabAddJob.setVisibility(View.GONE);
        View header = navigationView.inflateHeaderView(R.layout.nav_header);
        navImageView = header.findViewById(R.id.nav_header_user_image);
        navName = header.findViewById(R.id.nav_header_user_name);
        navJob = header.findViewById(R.id.nav_header_user_job);
        getUserType();
        setupNavigationContent();
        setupBottomNav();
        //selectItemDrawer(navigationView.getMenu().getItem(0));
    }


    private void getUserType() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            mDatabase.child("Users").child(userId).child("userType").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    userType = dataSnapshot.getValue(String.class);
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
        if (userType != null && !userType.isEmpty()) {
            if (userType.equals("Employee")) {
                //menuBottom.findItem(R.id.nav_my_job).setVisible(true);
                menuBottom.removeItem(R.id.nav_my_job);
                fabAddJob.setVisibility(View.GONE);
                readEmployeeDataHandleNavHeader();
            } else if (userType.equals("Company")) {
                menuBottom.removeItem(R.id.nav_applied_job);
                fabAddJob.setVisibility(View.VISIBLE);
                readCompanyDataHandleNavHeader();
            } else {
                Log.e(TAG, "UserType Another Type about Employee/Company");
                handleNoUserType();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.app_bar_search) {

            return true;
        }
        return super.onOptionsItemSelected(item);
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
                    fragmentClass = new MyEmployeeProfileFragment();
                    loadFragment(fragmentClass);
                } else if (userType.equals("Company")) {
                    fragmentClass = new MyCompanyProfileFragment();
                    loadFragment(fragmentClass);
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
