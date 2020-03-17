package com.lsc.navigator;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.lsc.navigator.utils.NavGraphBuilder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private NavController mNavController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navView = findViewById(R.id.nav_view);

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        mNavController = Navigation.findNavController(this, R.id.nav_host_fragment);

        NavigationUI.setupWithNavController(navView, mNavController);

        NavGraphBuilder.build(mNavController,this,fragment.getId());
        navView.setOnNavigationItemSelectedListener(this);
    }

    /**
     *
     *
     * @param item
     * @return  true 代表选中，会着色。false未选中，不会着色。
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        mNavController.navigate(item.getItemId());
        return !TextUtils.isEmpty(item.getTitle());
    }
}
