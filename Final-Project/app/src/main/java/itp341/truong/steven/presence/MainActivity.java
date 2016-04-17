package itp341.truong.steven.presence;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import itp341.truong.steven.presence.ClassFragment.OnListFragmentInteractionListener;
import itp341.truong.steven.presence.dummy.DummyContent;

public class MainActivity extends AppCompatActivity implements OnListFragmentInteractionListener , NavigationView.OnNavigationItemSelectedListener {

    private String currentUserID;
    private User currentUser;
    private Firebase firebaseUserRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeReferences();

        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.Fragment fragment = new ClassFragment();
        fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }


    public void initializeReferences() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Intent i = getIntent();
        currentUserID = i.getStringExtra("uid");
        if (currentUserID == null || currentUserID.length() == 0) {
            finish();
        }

        loadAccount(currentUserID);
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        if (id == R.id.nav_kiosk) {

        } else if (id == R.id.nav_log_out) {

        } else if (id == R.id.nav_manage_current_class) {

        } else if (id == R.id.nav_manage_classes) {
            android.support.v4.app.Fragment fragment = new ClassFragment();
            fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
        } else if (id == R.id.nav_options) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    void loadAccount(String currentID) {
        firebaseUserRef = new Firebase("https://presence-manager.firebaseio.com/users/"+currentID);
        currentUser = new User();
        currentUser.setUid(currentID);
        firebaseUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                ((TextView) findViewById(R.id.email)).setText(dataSnapshot.child("email").getValue().toString());


                final Object nameNode = dataSnapshot.child("name").getValue();
                if (nameNode == null) {
                    ((TextView) findViewById(R.id.initialMessage)).setText("");

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Before we start, what's your name?");

                    // Set up the input
                    final EditText input = new EditText(MainActivity.this);
                    // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                    builder.setView(input);
                    builder.setCancelable(false);
                    // Set up the buttons
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            currentUser.setName(input.getText().toString());
                            ((TextView) findViewById(R.id.initialMessage)).setText(getString(R.string.welcome) + " " + input.getText().toString() + "!");
                            ((TextView) findViewById(R.id.nameHeader)).setText(input.getText().toString());
                            firebaseUserRef.child("name").setValue(input.getText().toString());
                        }
                    });

                    builder.show();
                } else {
                    currentUser.setName(nameNode.toString());
                    ((TextView) findViewById(R.id.nameHeader)).setText(nameNode.toString());
                    ((TextView) findViewById(R.id.initialMessage)).setText(getString(R.string.welcome) + " " + nameNode.toString() + "!");
                }

                loadClasses(currentUserID);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    void loadClasses(String currentID) {
        firebaseUserRef.child("classes").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {
        Log.d("Test", "LI Pressed");
    }

}
