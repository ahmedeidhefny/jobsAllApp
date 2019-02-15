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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.udacity.ahmed_eid.jobsallapp.Fragments.CategoriesFragment;
import com.udacity.ahmed_eid.jobsallapp.Fragments.HomeFragment;
import com.udacity.ahmed_eid.jobsallapp.Fragments.MyAddedJobsFragment;
import com.udacity.ahmed_eid.jobsallapp.Fragments.MyAppliedJobsFragment;
import com.udacity.ahmed_eid.jobsallapp.Fragments.MyCompanyProfileFragment;
import com.udacity.ahmed_eid.jobsallapp.Fragments.MyEmployeeProfileFragment;
import com.udacity.ahmed_eid.jobsallapp.Fragments.MySavedJobsFragment;
import com.udacity.ahmed_eid.jobsallapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
        navigationBottom.setVisibility(View.GONE);
        fabAddJob.setVisibility(View.GONE);
        getUserType();
        setupNavigationContent();
        selectItemDrawer(navigationView.getMenu().getItem(0));
    }



    private void handleMenuVisibleByUserType() {
        Menu menu = navigationView.getMenu();
        if (userType != null && !userType.isEmpty()) {
            if (userType.equals("Employee")) {
                menu.findItem(R.id.nav_my_job).setVisible(false);
                fabAddJob.setVisibility(View.GONE);
                navigationBottom.setVisibility(View.VISIBLE);
                setupBottomNav();
            } else if (userType.equals("Company")) {
                navigationBottom.setVisibility(View.GONE);
                fabAddJob.setVisibility(View.VISIBLE);
            }else {
                Log.e(TAG, "UserType Another Type about Employee/Company");
                handleNoUserType();
            }
        } else {
            Log.e(TAG, "UserType No found: Null");
            handleNoUserType();
        }
    }


    private void handleNoUserType(){
        Menu menu = navigationView.getMenu();
        menu.findItem(R.id.nav_my_job).setVisible(false);
        menu.findItem(R.id.nav_profile).setVisible(false);
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
            case R.id.nav_profile:
                if (userType.equals("Employee")) {
                    myFragment = new MyEmployeeProfileFragment();
                    loadFragment(myFragment);
                } else if (userType.equals("Company")) {
                    myFragment = new MyCompanyProfileFragment();
                    loadFragment(myFragment);
                }
                break;
            case R.id.nav_categories:
                myFragment = new CategoriesFragment();
                loadFragment(myFragment);
                break;
            case R.id.nav_my_job:
                String id = mAuth.getCurrentUser().getUid();
                MyAddedJobsFragment fragment = new MyAddedJobsFragment();
                fragment.setCompanyId(id);
                loadFragment(fragment);
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

    public void setupBottomNav(){
        navigationBottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectItemBottomNav(item);
                return false;
            }
        });
    }
}
