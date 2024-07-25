package com.example.healthpulse;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.example.healthpulse.databinding.ActivityMainBinding;
import com.example.healthpulse.worker.DataSyncWorker;
import com.example.healthpulse.worker.HeavyTaskWorker;
import com.google.android.material.navigation.NavigationView;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ActivityMainBinding binding;
    private NavController navController;
    private AppBarConfiguration appBarConfiguration;
    private DrawerLayout drawerLayout;

    private final ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            isGranted -> {
                if (isGranted) {
                    createNotificationChannel();
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        drawerLayout = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();
            appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph())
                    .setDrawerLayout(drawerLayout)
                    .build();
            NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
            NavigationUI.setupWithNavController(navigationView, navController);
        }

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, binding.toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                createNotificationChannel();
            } else {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        } else {
            createNotificationChannel();
        }

        OneTimeWorkRequest dataSyncRequest = new OneTimeWorkRequest.Builder(DataSyncWorker.class).build();
        WorkManager.getInstance(this).enqueue(dataSyncRequest);

        PeriodicWorkRequest periodicSyncRequest = new PeriodicWorkRequest.Builder(DataSyncWorker.class, 24, TimeUnit.HOURS).build();
        WorkManager.getInstance(this).enqueue(periodicSyncRequest);

        OneTimeWorkRequest heavyTaskRequest = new OneTimeWorkRequest.Builder(HeavyTaskWorker.class).build();
        WorkManager.getInstance(this).enqueue(heavyTaskRequest);
    }

    public void setTitle(String title) {
        binding.toolbar.setTitle(title);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            navController.navigateUp();
            return true;
        }
        return NavigationUI.onNavDestinationSelected(item, navController)
                || super.onOptionsItemSelected(item);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "HealthTrackerChannel";
            String description = "Channel for health data reminders";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("HealthTracker_channel", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            navController.navigate(R.id.mainMenuFragment);
        } else if (id == R.id.nav_profile) {
            navController.navigate(R.id.viewProfileFragment);
        } else if (id == R.id.nav_health_records) {
            navController.navigate(R.id.viewHealthRecordsFragment);
        } else if (id == R.id.nav_notifications) {
            navController.navigate(R.id.notificationSettingsFragment);
        } else if (id == R.id.nav_add_health_record) {
            navController.navigate(R.id.addHealthRecordFragment);
        } else if (id == R.id.nav_educational_content) {
            navController.navigate(R.id.EducationalContentFragment);
        } else if (id == R.id.nav_chart) {
            navController.navigate(R.id.chartFragment);
        }


        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }
}
