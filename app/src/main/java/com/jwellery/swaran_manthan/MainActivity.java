package com.jwellery.swaran_manthan;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private ImageView hambargerMenu;
    private NavigationView nvDrawer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        nvDrawer =  findViewById(R.id.nvView);
        hambargerMenu = findViewById(R.id.menubar);
        mDrawer = findViewById(R.id.drawer_layout);
        setSupportActionBar(toolbar);
        hambargerMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawer.openDrawer(GravityCompat.START);
            }
        });
        setupDrawerContent(nvDrawer);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });

    }

    public void selectDrawerItem(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_login:
                startActivity(new Intent(MainActivity.this, Login.class));
                break;
            case R.id.nav_aboutus:
                startActivity(new Intent(MainActivity.this, AboutUs.class));
                break;
            case R.id.nav_contactus:
                startActivity(new Intent(MainActivity.this, ContactUs.class));
                break;
            case R.id.nav_policy:
                startActivity(new Intent(MainActivity.this, PrivacyPolicy.class));
                break;
        }
    }

}
