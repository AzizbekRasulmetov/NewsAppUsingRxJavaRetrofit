package com.aziz.noteapprxjava.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
import android.widget.Toast;

import com.aziz.noteapprxjava.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.chip.Chip;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class FragmentContainerActivity extends AppCompatActivity implements ChipNavigationBar.OnItemSelectedListener, View.OnClickListener {

    private ChipNavigationBar chipNavigationBar;

    private GridLayout gridLayout;
    private BottomSheetBehavior bottomSheetBehavior;
    private Chip sportChip, businessChip, healthChip, technologyChip, entertainmentChip, scienceChip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_container);

        initViews();
        bottomSheetBehavior = BottomSheetBehavior.from(gridLayout);
    }

    private void initViews(){
        chipNavigationBar = findViewById(R.id.chip_navigation);
        chipNavigationBar.setOnItemSelectedListener(this);
        chipNavigationBar.setItemSelected(R.id.breaking_news, true);

        gridLayout = findViewById(R.id.bottom_sheet_layout);
        sportChip = findViewById(R.id.sport_chip);
        sportChip.setOnClickListener(this);
        businessChip = findViewById(R.id.business_chip);
        businessChip.setOnClickListener(this);
        healthChip = findViewById(R.id.health_chip);
        healthChip.setOnClickListener(this);
        technologyChip = findViewById(R.id.tech_chip);
        technologyChip.setOnClickListener(this);
        scienceChip = findViewById(R.id.science_chip);
        scienceChip.setOnClickListener(this);
        entertainmentChip = findViewById(R.id.entertainment_chip);
        entertainmentChip.setOnClickListener(this);
    }


    @Override
    public void onItemSelected(int i) {
        Fragment fragment = null;
        switch (i){
            case R.id.breaking_news:
                fragment = new BreakingNewsFragment();
                break;
            case R.id.saved_news:
                fragment = new SavedNewsFragment();
                break;
            case R.id.search_news:
                fragment = new SearchNewsFragment();
                break;
        }

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.categories_nav) {
            toggleBottomSheet();
            return true;
        }
        return false;
    }

    private void toggleBottomSheet(){
        if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }

    @Override
    public void onClick(View v) {
        if(v == sportChip){
            Toast.makeText(this, "Sports", Toast.LENGTH_SHORT).show();
            BreakingNewsFragment.CATEGORY = "sports";
        }else if(v == businessChip){
            Toast.makeText(this, "Business", Toast.LENGTH_SHORT).show();
            BreakingNewsFragment.CATEGORY = "business";
        }else if(v == healthChip){
            Toast.makeText(this, "Health", Toast.LENGTH_SHORT).show();
            BreakingNewsFragment.CATEGORY = "health";
        }else if(v == technologyChip){
            Toast.makeText(this, "Technology", Toast.LENGTH_SHORT).show();
            BreakingNewsFragment.CATEGORY = "technology";
        }else if(v == entertainmentChip){
            Toast.makeText(this, "Entertainment", Toast.LENGTH_SHORT).show();
            BreakingNewsFragment.CATEGORY = "entertainment";
        }else if(v == scienceChip){
            Toast.makeText(this, "Science", Toast.LENGTH_SHORT).show();
            BreakingNewsFragment.CATEGORY = "science";
        }
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new BreakingNewsFragment());
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();
        toggleBottomSheet();
    }
}