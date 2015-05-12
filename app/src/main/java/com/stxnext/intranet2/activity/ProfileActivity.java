package com.stxnext.intranet2.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.stxnext.intranet2.R;
import com.stxnext.intranet2.backend.api.UserApi;
import com.stxnext.intranet2.backend.api.UserApiImpl;
import com.stxnext.intranet2.backend.callback.UserApiCallback;
import com.stxnext.intranet2.backend.model.User;
import com.stxnext.intranet2.fragment.FloatingMenuFragment;


/**
 * Created by Tomasz Konieczny on 2015-04-22.
 */
public class ProfileActivity extends AppCompatActivity
        implements UserApiCallback, FloatingMenuFragment.OnFlotingMenuItemClickListener, ListView.OnItemClickListener {

    private static final String TAG = "ProfileActivity";
    private static final int LOGIN_REQUEST = 1;
    private static final int DRAWER_ABSENCE_POSITION = 0;
    private static final int DRAWER_EMPLOYESS_POSITION = 1;

    private ActionBarDrawerToggle drawerToggle;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_root);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        configureDrawer();
        prepareFloatingButton();

        UserApi userApi = new UserApiImpl(this);
        userApi.requestForUser(null);

        if (isLogged()) {
            loadProfile();
        } else {
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivityForResult(loginIntent, LOGIN_REQUEST);
        }

    }

    private void configureDrawer() {
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ListView drawerList = (ListView) findViewById(R.id.left_drawer);
        //TODO: Trzeba to wyrzucić to jakiegoś enuma albo jako osobne opcje w adaptarze
        String[] drawerElements = {"Nieobecności", "Lista pracowników", "Settingsy"};
        ArrayAdapter<String> drawerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, drawerElements);
        drawerList.setAdapter(drawerAdapter);
        drawerList.setOnItemClickListener(this);

        drawerToggle = new ActionBarDrawerToggle(this,
                drawerLayout,
                toolbar,
                R.string.drawer_open,
                R.string.drawer_close);

        drawerLayout.setDrawerListener(drawerToggle);
    }

    private void prepareFloatingButton() {
        ViewGroup viewGroup = (ViewGroup) findViewById(R.id.floating_button);
        final View plusView = viewGroup.getChildAt(0);
        if (plusView != null) {
            viewGroup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    plusView.animate().rotationBy(135).setDuration(200).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            Fragment fragment = getFragmentManager().findFragmentByTag("Asdasd");

                            if (fragment == null || !fragment.isAdded()) {
                                getFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.floating_menu_container, new FloatingMenuFragment(), "Asdasd")
                                        .setCustomAnimations(
                                                android.R.animator.fade_in, android.R.animator.fade_out,
                                                android.R.animator.fade_in, android.R.animator.fade_out)
                                        .addToBackStack(null)
                                        .commit();
                            } else {
                                getFragmentManager().popBackStackImmediate();
                            }
                        }
                    });
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case LOGIN_REQUEST:
                if (resultCode == LoginActivity.LOGIN_OK) {
                    loadProfile();
                } else {

                }
                break;
            default:
                break;
        }
    }

    //TODO
    private void loadProfile() {
        Log.d(TAG, "loadProfile()");
    }

    //TODO
    private boolean isLogged() {
        return false;
    }

    @Override
    public void onUserReceived(User user) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return drawerToggle.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onFloatingMenuItemClick(int option) {

    }

    /**
     * Drawer click.
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case DRAWER_ABSENCE_POSITION:
                break;
            case DRAWER_EMPLOYESS_POSITION:
                Intent employeesIntent = new Intent(this, EmployeesActivity.class);
                startActivity(employeesIntent);
                break;
        }

    }
}
