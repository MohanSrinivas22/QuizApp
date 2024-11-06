package edu.uga.cs.quizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

/**
 * Main activity of the application, containing navigation drawer and fragments.
 */
public class MainActivity extends AppCompatActivity {

    private static final String DEBUG_TAG = "MainActivity";
    private DrawerLayout drawerLayout;
    private Toolbar toolBar;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private StateCapitalsData stateCapitalsData = null;


    /**
     * Called when the activity is starting.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously
     *                           being shut down then this Bundle contains the data it most
     *                           recently supplied in onSaveInstanceState.
     *                           <p>
     *                           Note: Otherwise, it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // assigning ID of the toolbar to a variable
        toolBar = findViewById(R.id.toolBar);

        // using toolbar as ActionBar
        setSupportActionBar(toolBar);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        drawerLayout = findViewById(R.id.drawerLayout);

        toggle = setUpDrawerToggle();
        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();

        // Connect DrawerLayout events to the ActionBarToggle
        drawerLayout.addDrawerListener(toggle);
        navigationView = findViewById(R.id.navigationView);

        Fragment fragment = null;
        fragment = new MainScreen();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragmentContainerView, fragment)
                .addToBackStack("main Screen").commit();

        navigationView.setNavigationItemSelectedListener(
                item -> {
                    selectDrawerItem(item);
                    return true;
                });

        /**
         * Below commented code is to delete all the quiz records which are created while testing.
         */

//        stateCapitalsData = new StateCapitalsData(getApplicationContext());
//        stateCapitalsData.open();
//        int ids = stateCapitalsData.deleteAllQuestionsRecords();
//        int inds = stateCapitalsData.deleteAllQuizRecords();
    }

    /**
     * Selects the fragment to be displayed based on the selected item in the navigation drawer.
     *
     * @param menuItem The selected item in the navigation drawer.
     */
    public void selectDrawerItem(MenuItem menuItem) {
        Fragment fragment = null;
        // Create a new fragment based on the used selection in the nav drawer
        if (menuItem.getItemId() == R.id.home) {

            //If clicked on home button, got to MainActivity which is a splash screen.
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);

        } else if (menuItem.getItemId() == R.id.newQuiz) {

            //If clicked on new quiz, start a new NewQuizActivity where the user can answer the questions.
            Intent intent = new Intent(getApplicationContext(), NewQuizActivity.class);
            startActivity(intent);

        } else if (menuItem.getItemId() == R.id.quizHistory) {

            // Set up the fragment by replacing any existing fragment in the main activity
            fragment = new QuizHistoryFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragmentContainerView, fragment)
                    .addToBackStack("Splash Screen").commit();

        } else if (menuItem.getItemId() == R.id.help) {

            // Set up the fragment by replacing any existing fragment in the main activity
            fragment = new HelpFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragmentContainerView, fragment)
                    .addToBackStack("Splash Screen").commit();

        } else if (menuItem.getItemId() == R.id.close) {
            //Close the app
            Log.d(DEBUG_TAG,"Close called");
            // Inside your activity
            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
            homeIntent.addCategory(Intent.CATEGORY_HOME);
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(homeIntent);
        }

        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        drawerLayout.closeDrawers();
    }

    private ActionBarDrawerToggle setUpDrawerToggle() {
        // NOTE: Make sure you pass in a valid toolbar reference.  ActionBarDrawToggle() does not require it
        // and will not render the hamburger icon without it.
        return new ActionBarDrawerToggle(this, drawerLayout, toolBar, R.string.drawer_open, R.string.drawer_close);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        toggle.syncState();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        toggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(DEBUG_TAG,  "Called onDestroy" + ResultManager.getPosition());
    }
}