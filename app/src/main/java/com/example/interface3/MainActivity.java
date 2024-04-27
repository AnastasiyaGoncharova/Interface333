package com.example.interface3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
    private PhoneCallReceiver phoneCallReceiver;

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
        });
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

            if (id == R.id.nav_filter_by_status) {
                filterByStatus();
            } else if (id == R.id.nav_filter_by_score) {
                filterByScore();
            } else if (id == R.id.nav_filter_by_priority) {
                filterByPriority();
            } else if (id == R.id.nav_add_applicant) {
                filterByApplicant();
            }

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }

   /* @Override
    public void onApplicantClicked(Applicant applicant) {
        Log.d("ApplicantClick", "Clicked on Applicant: " + applicant.getLastName() + " " + applicant.getFirstName() + " " + applicant.getMiddleName());
        Log.d("ApplicantClick", "ID: " + applicant.getId());
        Log.d("ApplicantClick", "Profile: " + applicant.getProfile());
        Log.d("ApplicantClick", "Score: " + applicant.geteGE());
        Log.d("ApplicantClick", "Priority: " + applicant.getPriority());
    }*/

    @Override
    public void onItemClick(Applicant applicant) {
        Log.d("ApplicantClick", "Clicked on Applicant: " + applicant.getLastName() + " " + applicant.getFirstName() + " " + applicant.getMiddleName());
        Log.d("ApplicantClick", "ID: " + applicant.getId());
        Log.d("ApplicantClick", "Profile: " + applicant.getProfile());
        Log.d("ApplicantClick", "Score: " + applicant.geteGE());
        Log.d("ApplicantClick", "Priority: " + applicant.getPriority());
    }

       /* private void initAdapterData () {
            // Заполнение списка данных (предположим, что applicants, scores, priorities, profiles, isChecked - это ваши списки)
            for (int i = 0; i < applicants.size(); i++) {
                Map<String, Object> listItem = new HashMap<>();
                listItem.put("applicant", applicants.get(i));
                listItem.put("score", scores.get(i));
                listItem.put("priority", priorities.get(i));
                listItem.put("profile", profiles.get(i));
                listItem.put("checked", isChecked.get(i));
                data.add(listItem);
            }
        }*/


    /*@Override
    public void onApplicantFound(String applicantId) {
        if (applicantId != null) {
            Log.d("ApplicantSearch", "Found Applicant ID: " + applicantId);
        } else {
            Log.d("ApplicantSearch", "Applicant not found");
        }
    }*/

        /*@Override
        protected void onActivityResult ( int requestCode, int resultCode, Intent returnedData){
            super.onActivityResult(requestCode, resultCode, returnedData);

            if (requestCode == YOUR_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
                if (returnedData != null && returnedData.hasExtra("newApplicantData")) {
                    String newApplicantData = returnedData.getStringExtra("newApplicantData");

                    String[] splitData = newApplicantData.split(" ");
                    if (splitData.length >= 7) {
                        String fullName = splitData[0] + " " + splitData[1] + " " + splitData[2];
                        String phoneNumber = splitData[3];
                        int egeScore = Integer.parseInt(splitData[4]);
                        int priority = Integer.parseInt(splitData[5]);
                        String profile = splitData[6];

                        // Форматирование ФИО
                        String[] splitFullName = fullName.split(" ");
                        if (splitFullName.length == 3) {
                            fullName = splitFullName[0] + " " + splitFullName[1] + " " + splitFullName[2];
                        }

                        // Добавление нового аппликанта к списку
                        Map<String, Object> newApplicant = new HashMap<>();
                        newApplicant.put("applicant", fullName);
                        newApplicant.put("phone", phoneNumber);
                        newApplicant.put("score", egeScore);
                        newApplicant.put("priority", priority);
                        newApplicant.put("profile", profile);
                        newApplicant.put("checked", false);

                        data.add(newApplicant); // Обновление списка данных

                        // Обновление адаптера
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        }*/

    private void filterByStatus() {
        List<Map<String, Object>> filteredData = new ArrayList<>(data);

        Comparator<Map<String, Object>> statusComparator = (contact1, contact2) -> {
            boolean checked1 = (boolean) contact1.get("isChecked");
            boolean checked2 = (boolean) contact2.get("isChecked");

            if (checked1 && !checked2) {
                return 1;
            } else if (!checked1 && checked2) {
                return -1;
            } else {
                return 0;
            }
        };

        filteredData.sort(statusComparator);
        adapter.updateData(filteredData);
    }

        private void filterByScore () {
            List<Map<String, Object>> newData = new ArrayList<>(data);
            newData.sort(Comparator.comparingInt((Map<String, Object> contact) -> (int) contact.get("eGE")).reversed());
            adapter.updateData(newData);
        }

        private void filterByPriority () {
            List<Map<String, Object>> filteredData = new ArrayList<>(data);

            Comparator<Map<String, Object>> priorityComparator = Comparator.comparingInt(contact -> (int) contact.get("priority"));

            filteredData.sort(priorityComparator);
            adapter.updateData(filteredData);
        }

        private void filterByApplicant (){
            PhoneCallReceiver phoneCallReceiver = new PhoneCallReceiver();
            phoneCallReceiver.onReceive(this, new Intent(TelephonyManager.ACTION_PHONE_STATE_CHANGED));
        }
    }