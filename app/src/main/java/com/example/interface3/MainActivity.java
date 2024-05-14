package com.example.interface3;

import android.Manifest;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements CustomAdapter.OnItemClickListener {
    private List<Map<String, Object>> data;
    private List<Map<String, Object>> applicants = new ArrayList<Map<String, Object>>();
    private List<String> scores = new ArrayList<>();
    private List<String> priorities = new ArrayList<>();
    private List<String> profiles = new ArrayList<>();
    private List<Boolean> isChecked = new ArrayList<>();
    List<String> selectedStatuses = new ArrayList<>();
    private boolean isScoreFiltered = false;
    private boolean isPriorityFiltered = false;
    private static final int YOUR_REQUEST_CODE = 1;
    private List<Map<String, Object>> applicantData = new ArrayList<>();
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private RecyclerView recyclerView;
    private CustomAdapter adapter;
    private List<Map<String, Object>> filteredData;
    private List<Map<String, Object>> originalData;
    private List<Applicant> applicantsToSearch = new ArrayList<>();
    /*private PhoneCallReceiver phoneCallReceiver;*/
    private static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 1;
    private static final String TAG = "MainActivity";
    private static final int PERMISSION_REQUEST_CODE = 1;
    private CallReceiver callReceiver;
    private boolean isApplicantButtonClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        data = new ArrayList<>();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new CustomAdapter(this, data, this);
        recyclerView.setAdapter(adapter);

        NetworkRequestTask networkTask = new NetworkRequestTask(new NetworkRequestTask.NetworkResponseListener() {
            @Override
            public void onDataReceived(List<Applicant> newData) {
                List<Map<String, Object>> convertedData = new ArrayList<>();
                for (Applicant applicant : newData) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("firstName", applicant.getFirstName());
                    map.put("lastName", applicant.getLastName());
                    map.put("middleName", applicant.getMiddleName());
                    map.put("eGE", applicant.geteGE());
                    map.put("priority", applicant.getPriority());
                    map.put("profile", applicant.getProfile());
                    // Получаем список статусов как ArrayList
                    ArrayList<String> statuses = new ArrayList<>(applicant.getStatuses());
                    // Преобразуем ArrayList в List<String>
                    List<String> statusesList = new ArrayList<>(statuses);

                    map.put("statuses", statusesList);
                    Log.d("Applicant Statuses", "Applicant Statuses: " + statusesList);
                    convertedData.add(map);
                }

                runOnUiThread(() -> {
                    data.clear();
                    data.addAll(convertedData);

                    if (adapter != null) {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }, getApplicationContext());
        networkTask.execute();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.status_go || id == R.id.status_notgo || id == R.id.status_nesti || id == R.id.status_ok || id == R.id.status_sdan || id == R.id.status_outsider || id == R.id.status_goout || id == R.id.status_call) {
                item.setChecked(!item.isChecked()); // Переключение состояния радиокнопки или флажка

                List<String> selectedStatuses = new ArrayList<>();
                MenuItem goItem = navigationView.getMenu().findItem(R.id.status_go);
                MenuItem notgoItem = navigationView.getMenu().findItem(R.id.status_notgo);
                MenuItem nestiItem = navigationView.getMenu().findItem(R.id.status_nesti);
                MenuItem okItem = navigationView.getMenu().findItem(R.id.status_ok);
                MenuItem sdanItem = navigationView.getMenu().findItem(R.id.status_sdan);
                MenuItem outsiderItem = navigationView.getMenu().findItem(R.id.status_outsider);
                MenuItem gooutItem = navigationView.getMenu().findItem(R.id.status_goout);
                MenuItem callItem = navigationView.getMenu().findItem(R.id.status_call);

                if (goItem.isChecked()) {
                    selectedStatuses.add("Буду поступать!");
                }
                if (notgoItem.isChecked()) {
                    selectedStatuses.add("Не буду поступать");
                }
                if (nestiItem.isChecked()) {
                    selectedStatuses.add("Донести документы");
                }
                if (okItem.isChecked()) {
                    selectedStatuses.add("Уже зачислен");
                }
                if (sdanItem.isChecked()) {
                    selectedStatuses.add("Документы сданы");
                }
                if (outsiderItem.isChecked()) {
                    selectedStatuses.add("Иногородние");
                }
                if (gooutItem.isChecked()) {
                    selectedStatuses.add("Временно в отъезде");
                }
                if (callItem.isChecked()) {
                    selectedStatuses.add("Перезвонить");
                }

                filterByStatus(selectedStatuses, data);
            } else if (id == R.id.nav_filter_by_score) {
                filterByScore();
            } else if (id == R.id.nav_filter_by_priority) {
                filterByPriority();
            } else if (id == R.id.nav_found_applicant) {
                filterByApplicant();
            }

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
       /* phoneCallReceiver = new PhoneCallReceiver();*/
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED) {
            startCallInterceptor();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_CALL_LOG},
                    PERMISSION_REQUEST_CODE);
        }
    }

    private void startCallInterceptor() {
        if (isApplicantButtonClicked) {
            callReceiver = new CallReceiver();
            IntentFilter intentFilter = new IntentFilter(TelephonyManager.ACTION_PHONE_STATE_CHANGED);
            registerReceiver(callReceiver, intentFilter);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (callReceiver != null) {
            unregisterReceiver(callReceiver);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                startCallInterceptor();
            } else {
                Toast.makeText(this, "Permission denied for reading phone state and call log", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onItemClick(Applicant applicant) {
        Log.d("ApplicantClick", "Clicked on Applicant: " + applicant.getLastName() + " " + applicant.getFirstName() + " " + applicant.getMiddleName());
        Log.d("ApplicantClick", "ID: " + applicant.getId());
        Log.d("ApplicantClick", "Profile: " + applicant.getProfile());
        Log.d("ApplicantClick", "Score: " + applicant.geteGE());
        Log.d("ApplicantClick", "Priority: " + applicant.getPriority());
    }

    private void filterByStatus(List<String> selectedStatuses, List<Map<String, Object>> data) {
        List<Map<String, Object>> matchedApplicants = new ArrayList<>();
        List<Map<String, Object>> remainingApplicants = new ArrayList<>();

        for (int i = 0; i < data.size(); i++) {
            Map<String, Object> applicant = data.get(i);
            List<String> statuses = (List<String>) applicant.get("statuses");

            boolean matched = false;
            for (String status : statuses) {
                if (selectedStatuses.isEmpty() || selectedStatuses.contains(status)) {
                    matched = true;
                    break;
                }
            }

            if (matched) {
                matchedApplicants.add(applicant);
            } else {
                remainingApplicants.add(applicant);
            }
        }

        data.clear();
        data.addAll(matchedApplicants);
        data.addAll(remainingApplicants);

        runOnUiThread(() -> {
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void filterByScore() {
        List<Map<String, Object>> newData = new ArrayList<>(data);
        newData.sort(Comparator.comparingInt((Map<String, Object> contact) -> (int) contact.get("eGE")).reversed());
        adapter.updateData(newData);
    }

    private void filterByPriority() {
        List<Map<String, Object>> filteredData = new ArrayList<>(data);

        Comparator<Map<String, Object>> priorityComparator = Comparator.comparingInt(contact -> (int) contact.get("priority"));

        filteredData.sort(priorityComparator);
        adapter.updateData(filteredData);
    }

    private void filterByApplicant() {
        isApplicantButtonClicked = true;

        startCallInterceptor();

    }
}