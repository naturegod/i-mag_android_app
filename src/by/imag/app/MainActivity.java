package by.imag.app;


import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import by.imag.app.classes.Constants;


public class MainActivity extends FragmentActivity {
    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private boolean isUpdated = false;
    private FragmentManager fragmentManager;

    private CharSequence drawerTitle;
    private CharSequence title;
    private String[] menuTitles;
    private SharedPreferences preferences;
    private Menu menu;
    private ActionBar actionBar;
//    private Fragment fragment;
    private int menuPosition;
    private static final long DRAWER_CLOSE_WAIT = 300;

    @Override
    protected void onStart() {
        super.onStart();
//        logMsg("onStart");
    }


    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
//        logMsg("onCreate");
        actionBar = getActionBar();
        fragmentManager = getSupportFragmentManager();
        preferences = getPreferences(MODE_PRIVATE);
//        isUpdated = preferences.getBoolean(Constants.IS_UPDATED, false);

        title = drawerTitle = getTitle();
        menuTitles = getResources().getStringArray(R.array.menu_items);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) findViewById(R.id.left_drawer);

        // set a custom shadow that overlays the main content when the drawer opens
        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener
//        drawerList.setAdapter(new ArrayAdapter<String>(this,
//                R.layout.drawer_list_item, menuTitles));

        drawerList.setAdapter(new DrawerAdapter(this, menuTitles));

        drawerList.setOnItemClickListener(new DrawerItemClickListener());

        // enable ActionBar app icon to behave as action to toggle nav drawer
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

//        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.retina_wood));
//        fragmentManager.beginTransaction().replace(R.id.content_frame,
//                new FragmentDefault()).commit();
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close) {

            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
//                logMsg("on drawer closed");
//                hideMenuItem(menu);
                actionBar.setTitle(title);
//                if (fragment != null) {
//                    new FragmentLoader().execute(fragment);
//                }

            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
//                logMsg("on drawer opened");
                actionBar.setTitle(drawerTitle);
//                hideMenuItem(menu);
                invalidateOptionsMenu();
            }

        };
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        if (savedInstanceState == null) {
            selectItem(0);
        }
	}

    @Override
    protected void onResume() {
        super.onResume();
//        drawerLayout.openDrawer(Gravity.LEFT);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        this.finish();
//        logMsg("onDestroy");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        logMsg("onBackPressed");
        savePreferences();
        System.exit(0);
        android.os.Process.killProcess(Process.myPid());
        this.finish();
    }

    private void savePreferences() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(Constants.UPDATE_POSTS, true);
        editor.putBoolean(Constants.UPDATE_TAGS, true);
        editor.putBoolean(Constants.UPDATE_ARCHIVES, true);
        editor.putBoolean(Constants.UPDATE_MAGS, true);
        editor.commit();
    }

    private void selectItem(int position) {
        drawerList.setItemChecked(position, true);
        menuPosition = position;
//        setTitle(menuTitles[position]);
//        logMsg("position: "+position);
        switch (position) {
            case 0: // posts
//                fragmentManager.beginTransaction().replace(R.id.content_frame,
//                        new FragmentPosts()).commit();
//                new FragmentLoader().execute(new FragmentPosts());
                replaceFragment(new FragmentPosts());
                break;
            case 1: //tags
//                fragmentManager.beginTransaction().replace(R.id.content_frame,
//                        new FragmentTags()).commit();
//                new FragmentLoader().execute(new FragmentTags());
                replaceFragment(new FragmentTags());
            break;
            case 2: //archives
//                fragmentManager.beginTransaction().replace(R.id.content_frame,
//                        new FragmentArchives()).commit();
//                new FragmentLoader().execute(new FragmentArchives());
                replaceFragment(new FragmentArchives());
            break;
            case 3: // mags
//                fragmentManager.beginTransaction().replace(R.id.content_frame,
//                        new FragmentAbout()).commit();
//                new FragmentLoader().execute(new FragmentMag());
                replaceFragment(new FragmentMag());
            break;
            case 4: // about
//                fragmentManager.beginTransaction().replace(R.id.content_frame,
//                        new FragmentContacts()).commit();
//                new FragmentLoader().execute(new FragmentAbout());
                replaceFragment(new FragmentAbout());
            break;
            case 5: // contacts
//                fragmentManager.beginTransaction().replace(R.id.content_frame,
//                        new FragmentAdv()).commit();
//                new FragmentLoader().execute(new FragmentContacts());
                replaceFragment(new FragmentContacts());
            break;
            case 6: // adv
//                fragmentManager.beginTransaction().replace(R.id.content_frame,
//                        new FragmentMag()).commit();
//                new FragmentLoader().execute(new FragmentAdv());
                replaceFragment(new FragmentAdv());
                break;
            default: actionBar.setSubtitle(menuTitles[position]);
        }
        actionBar.setSubtitle(menuTitles[position]);
        drawerLayout.closeDrawer(drawerList);
    }



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
        this.menu = menu;
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        return super.onOptionsItemSelected(item);
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        } else {
            switch (item.getItemId()) {
                case R.id.action_settings:
                    Intent intent = new Intent(this, ActivitySettings.class);
                    startActivity(intent);
//                case R.id.action_update:
//                    logMsg("action update");
//                    serviceUpdate();
//                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    private void logMsg(String msg) {
        Log.d(Constants.LOG_TAG, getClass().getSimpleName() + ": " + msg);
    }

    private void replaceFragment(Fragment fragment) {
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
    }

//    private class FragmentLoader extends AsyncTask<Fragment, Void, Void> {
//
//        @Override
//        protected Void doInBackground(Fragment... fragments) {
//            Fragment fragment = fragments[0];
//            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            super.onPostExecute(aVoid);
//            drawerLayout.closeDrawer(Gravity.LEFT);
//        }
//    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
            drawerLayout.closeDrawer(drawerList);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    selectItem(position);
                }
            }, DRAWER_CLOSE_WAIT);
        }
    }
}
