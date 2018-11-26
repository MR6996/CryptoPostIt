package com.randazzo.mario.cryptopost_it;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    /**
     * TAG used on saving instance state of activity.
     */
    private static final String LAST_ID_TAG = "last_id_tag";


    /**
     * Reference to the {@link DrawerLayout}
     */
    private DrawerLayout mDrawerLayout;

    /**
     * Reference to the {@link FragmentManager} of the activity.
     */
    private FragmentManager mFragmentManager;

    /**
     * The last {@link MenuItem} id selected.
     */
    private Integer mLastItemId = R.id.bezout;

    /**
     * Reference to app {@link Toolbar}
     */
    private Toolbar mAppToolbar;

    /**
     * The listener for item selection, used in the {@link NavigationView}
     */
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
            mAppToolbar.setTitle(item.getTitle());
            mLastItemId = item.getItemId();
            return true;
        }

        /**
         * Replace the current fragment with the fragment f.
         *
         * @param f a fragment to replace.
         */
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

        mAppToolbar = findViewById(R.id.app_toolbar);
        mAppToolbar.setTitle("");
        setSupportActionBar(mAppToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(Gravity.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void showErrorDialog(String message) {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.error_dialog_title))
                .setMessage(message)
                .setPositiveButton(getString(R.string.error_dialog_ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setCancelable(false)
                .show();
    }
}
