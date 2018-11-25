package com.randazzo.mario.cryptopost_it;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private static final String LAST_ID_TAG = "last_id_tag";

    private DrawerLayout mDrawerLayout;
    private FragmentManager mFragmentManager;
    private Integer mLastItemId = R.id.bezout;

    private NavigationView.OnNavigationItemSelectedListener mSelectionListener = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.bezout:
                    changeFragment(new BezoutFragment());
                    break;

                case R.id.discrete_log:
                    changeFragment(new DicreteLogFragment());
                    break;

                case R.id.rsa:
                    changeFragment(new RSAFragment());
                    break;

                case R.id.el_gamal:
                    changeFragment(new ElGamalFragment());
                    break;

                default:
                    return false;
            }

            item.setChecked(true);
            mDrawerLayout.closeDrawers();
            mLastItemId = item.getItemId();
            return true;
        }

        private void changeFragment(Fragment f) {
            mFragmentManager
                    .beginTransaction()
                    .replace(R.id.main_frame, f)
                    .commit();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawerLayout = findViewById(R.id.menu_drawer);
        mFragmentManager = getSupportFragmentManager();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(mSelectionListener);

        if (savedInstanceState != null)
            mLastItemId = savedInstanceState.getInt(LAST_ID_TAG);

        navigationView.setCheckedItem(mLastItemId);
        mSelectionListener.onNavigationItemSelected(navigationView.getMenu().findItem(mLastItemId));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(LAST_ID_TAG, mLastItemId);
    }

}