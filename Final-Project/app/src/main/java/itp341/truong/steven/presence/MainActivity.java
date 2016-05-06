package itp341.truong.steven.presence;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
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

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import itp341.truong.steven.presence.ClassesInfoFragment.OnListFragmentInteractionListener;

public class MainActivity extends AppCompatActivity implements OnListFragmentInteractionListener , NavigationView.OnNavigationItemSelectedListener {

    private String currentUserID;
    private User currentUser;

    private List<String> currentUserClassesIDs; //Array of classes, but just the IDs for now
    private ArrayList<Class> currentUserClasses;

    private Firebase firebaseUserRef;

    private ValueEventListener firebaseUserRefListener;

    private android.support.v4.app.FragmentManager fragmentManager;
    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeReferences();

        fragmentManager = getSupportFragmentManager();
        currentFragment = ClassesInfoFragment.newInstance(currentUserID);
        fragmentManager.beginTransaction().replace(R.id.fragment_container, currentFragment, "classfragment").commit();
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

        currentUserClassesIDs = new ArrayList<String>();
        currentUserClasses = new ArrayList<Class>();

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
            firebaseUserRef.removeEventListener(firebaseUserRefListener);
            fragmentManager.beginTransaction().remove(currentFragment).commit();
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
            if (currentFragment instanceof ClassesInfoFragment) {

            }
            else {
                android.support.v4.app.Fragment fragment = new ClassesInfoFragment();
                fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
            }
        } else if (id == R.id.nav_options) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    void loadAccount(String currentID) {
        firebaseUserRef = new Firebase(FirebaseConstants.USERS +currentID);
        currentUser = new User();
        currentUser.setUid(currentID);

        firebaseUserRefListener = new ValueEventListener() {
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
                            firebaseUserRef.child(FirebaseConstants.USERS_NAME_KEY).setValue(input.getText().toString());
                        }
                    });

                    builder.show();
                } else {
                    currentUser.setName(nameNode.toString());
                    ((TextView) findViewById(R.id.nameHeader)).setText(nameNode.toString());
                    ((TextView) findViewById(R.id.initialMessage)).setText(getString(R.string.welcome) + " " + nameNode.toString() + "!");
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        };

        firebaseUserRef.addValueEventListener(firebaseUserRefListener);

    }


    @Override
    public void onListFragmentInteraction(Class item) {
        Log.d("Test", item.name + "LI Pressed");
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode) {
            case 1:
                if (resultCode != RESULT_CANCELED) {
                    ActivityConstants.Actions action = (ActivityConstants.Actions) data.getSerializableExtra("action");
                    switch (action) {
                        case CREATE:
                            addClass(data.getStringExtra("name"), data.getStringExtra("detail"));
                            break;
                        case UPDATE:
                            Log.d("UPDATE", "UPDATE TRIGGERED");
                            updateClass(data.getStringExtra("classID"), data.getStringExtra("name"), data.getStringExtra("detail"));
                            break;
                        case DELETE:
                            Log.d("Delete", "DELETE TRIGGERED");
                            deleteClass(data.getStringExtra("classID"));
                            break;
                        default:
                            break;
                    }
                }
                break;
            default:
                Log.d("Test", "REQ CODE: " + requestCode + " RESULT CODE: " + resultCode);
        }
    }

    protected void addClass(String name, String detail) {
        Firebase ref = new Firebase(FirebaseConstants.CLASSES);
        ref = ref.push();
        ref.child("name").setValue(name);
        ref.child("detail").setValue(detail);
        ref.child("owners").child(currentUserID).setValue(currentUserID);
        firebaseUserRef.child("classes").child(ref.getKey()).setValue(ref.getKey());
    }

    protected void updateClass(String id, String name, String detail) {
        Firebase classesRef = new Firebase(FirebaseConstants.CLASSES + id);
        classesRef.child("name").setValue(name);
        classesRef.child("detail").setValue(detail);
        if (currentFragment instanceof ClassesInfoFragment) {
            ((ClassesInfoFragment) currentFragment).updateClassFromID(id);
        }
    }

    protected  void deleteClass(String id) {
        //Remove the class instance
        Firebase classesRef = new Firebase(FirebaseConstants.CLASSES + id);
        classesRef.removeValue();

        //Remove the class from the owners' list of classes
        Firebase userClassRef = new Firebase(FirebaseConstants.USERS + "/" +currentUserID + "/" + FirebaseConstants.USERS_CLASSES_KEY + "/" + id);
        userClassRef.removeValue();
    }

}
