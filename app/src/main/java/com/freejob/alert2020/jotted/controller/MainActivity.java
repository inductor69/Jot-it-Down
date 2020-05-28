package com.freejob.alert2020.jotted.controller;

import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.freejob.alert2020.jotted.R;
import com.freejob.alert2020.jotted.model.Note;
import com.freejob.alert2020.jotted.persistence.JSONSerializer;

import java.util.ArrayList;
// orientation changes reference:
// https://medium.com/hootsuite-engineering/handling-orientation-changes-on-android-41a6b62cb43f

public class MainActivity extends AppCompatActivity {

    private Fragment calendarFragment;
    private Fragment homeFragment;
    private Fragment journalFragment;
    private ArrayList<Note> notes;          // ArrayList implements Serializable, List does not

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment;

            switch (item.getItemId()) {
                case R.id.navigation_calendar:
                    selectedFragment = calendarFragment; // new CalendarFragment();
                    break;
                case R.id.navigation_home:
                    selectedFragment = homeFragment; // new HomeFragment();
                    break;
                case R.id.navigation_journal:
                    selectedFragment = journalFragment; // new JournalFragment();
                    break;
                default:
                    selectedFragment = homeFragment;
            }

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            //ft.setCustomAnimations(R.anim.cross_fade_in, R.anim.fade_out);
            ft.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
            ft.replace(R.id.mainFragmentHolder, selectedFragment, "curr").commit();
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calendarFragment = new CalendarFragment();
        homeFragment = new HomeFragment();
        journalFragment = new JournalFragment();

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        // When MainActivity is created for the first time
        if (savedInstanceState == null) {
            navigation.setSelectedItemId(R.id.navigation_home);
        }

        JSONSerializer serializer = new JSONSerializer("Notes.json", getApplicationContext());
        try {
            notes = serializer.loadNotes();
        } catch (Exception e) {
            notes = new ArrayList<>();
            Log.e("error", "Error loading notes: ", e);
        }
    }
    public void switchToJournalFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.delayed_fade_in, R.anim.fade_out);
        ft.replace(R.id.mainFragmentHolder, journalFragment, "curr").commit();

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.navigation_journal);
    }

    public ArrayList<Note> getNotes() {
        return notes;
    }

    public int getIndexOfNote(Note note) {
        if (notes.contains(note)) {
            return notes.indexOf(note);
        } else {
            return -1;
        }
    }

}
