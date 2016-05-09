package itp341.truong.steven.presence;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

import itp341.truong.steven.presence.dummy.DummyMemberContent;

public class ManageClassActivity extends AppCompatActivity {

    //UI
    EditText name, detail;
    Button addClassButton, removeClassButton;

    String currentClassID;
    ArrayList<CheckBox> checkBoxArrayList;
    ArrayList<Member> memberArrayList;

    boolean newClass;

    Intent i;
    MemberRecyclerViewAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_class);

        name  = (EditText) findViewById(R.id.newClassNameEditText);
        detail = (EditText) findViewById(R.id.newClassDetailEditText);
        addClassButton = (Button) findViewById(R.id.submitNewClass);
        removeClassButton = (Button) findViewById(R.id.removeClass);

        i = getIntent();
        newClass = i.getBooleanExtra("newclass", true);
        if (!newClass) {
            //Fill in old account info
            currentClassID = i.getStringExtra("classID");
            name.setText(i.getStringExtra("name"));
            detail.setText(i.getStringExtra("details"));

            ArrayList<String> checkedDays = i.getStringArrayListExtra("meeting_days");
            for(String c : checkedDays) {
                switch(c) {
                    case "Monday":
                        ((CheckBox) findViewById(R.id.monday_checkbox)).setChecked(true);
                        break;
                    case "Tuesday":
                        ((CheckBox) findViewById(R.id.tuesday_checkbox)).setChecked(true);
                        break;
                    case "Wednesday":
                        ((CheckBox) findViewById(R.id.wednesday_checkbox)).setChecked(true);
                        break;
                    case "Thursday":
                        ((CheckBox) findViewById(R.id.thursday_checkbox)).setChecked(true);
                        break;
                    case "Friday":
                        ((CheckBox) findViewById(R.id.friday_checkbox)).setChecked(true);
                        break;
                    case "Saturday":
                        ((CheckBox) findViewById(R.id.saturday_checkbox)).setChecked(true);
                        break;
                    case "Sunday":
                        ((CheckBox) findViewById(R.id.sunday_checkbox)).setChecked(true);
                        break;
                    default:
                        break;
                }
            }
        }
        else {
            removeClassButton.setVisibility(View.GONE);
        }

        addClassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent classIntent = new Intent();
                if (!TextUtils.isEmpty(name.getText().toString()) && !TextUtils.isEmpty(detail.getText().toString()) && AtLeastOneDaySelected()) {
                    classIntent.putExtra("name", name.getText().toString());
                    classIntent.putExtra("detail", detail.getText().toString());

                    ArrayList<String> daysToMeet = new ArrayList<String>();
                    for(CheckBox c : checkBoxArrayList) {
                        if (c.isChecked()) {
                            daysToMeet.add(c.getText().toString());
                        }
                    }

                    classIntent.putStringArrayListExtra("meeting_days", daysToMeet);

                    if (newClass) {
                        classIntent.putExtra("action", ActivityConstants.Actions.CREATE);
                    }
                    else {
                        classIntent.putExtra("classID", i.getStringExtra("classID"));
                        classIntent.putExtra("action", ActivityConstants.Actions.UPDATE);
                    }

                    setResult(1, classIntent);
                    finish();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Can't have empty fields!", Toast.LENGTH_LONG).show();
                }
            }
        });

        removeClassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = AskOption();
                dialog.show();
            }
        });

        checkBoxArrayList = new ArrayList<CheckBox>();
        checkBoxArrayList.add((CheckBox)findViewById(R.id.monday_checkbox));
        checkBoxArrayList.add((CheckBox)findViewById(R.id.tuesday_checkbox));
        checkBoxArrayList.add((CheckBox)findViewById(R.id.wednesday_checkbox));
        checkBoxArrayList.add((CheckBox)findViewById(R.id.thursday_checkbox));
        checkBoxArrayList.add((CheckBox)findViewById(R.id.friday_checkbox));
        checkBoxArrayList.add((CheckBox)findViewById(R.id.saturday_checkbox));
        checkBoxArrayList.add((CheckBox)findViewById(R.id.sunday_checkbox));


        ((CheckBox) findViewById(R.id.all_checkbox)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(CheckBox c : checkBoxArrayList) {
                    c.setChecked(!c.isChecked());
                }
            }
        });

        memberArrayList = new ArrayList<Member>();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.member_list);
        if (recyclerView instanceof RecyclerView) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            adapter = new MemberRecyclerViewAdapter(memberArrayList, i.getStringExtra("classID"));
            recyclerView.setAdapter(adapter);
        }


        //If you're not a new class, you must have members!
        if(!newClass) {
            loadMembers();
        }
    }

    //aka Load Students
    private void loadMembers() {
        Firebase firebaseClassRef = new Firebase(FirebaseConstants.CLASSES + currentClassID);
        firebaseClassRef.child("students").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                addStudentFromID(dataSnapshot.getValue().toString());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                removeMember(currentClassID, dataSnapshot.getValue().toString());
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    private void addStudentFromID(final String id) {
        Firebase firebaseMemberRef = new Firebase(FirebaseConstants.MEMBERS  + id);
        firebaseMemberRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Member m = new Member();
                m.memberID = id;
                m.name = dataSnapshot.child("name").getValue().toString();
                m.pin = dataSnapshot.child("pin").getValue().toString();

                memberArrayList.add(m);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    private boolean AtLeastOneDaySelected() {
        if (checkBoxArrayList != null) {
            for (CheckBox c : checkBoxArrayList) {
                if (c.isChecked()) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        Intent mIntent = new Intent();
        setResult(RESULT_CANCELED, mIntent);
        super.onBackPressed();
    }


    private Intent returnNewClass(String name, String detail) {
        Intent i = new Intent();
        return i;
    }

    private Intent returnDeleteClass(String id) {
        Intent i = new Intent();
        return i;
    }

    private AlertDialog AskOption()
    {
        AlertDialog myQuittingDialogBox =new AlertDialog.Builder(this)
                //set message, title, and icon
                .setTitle("Delete")
                .setMessage("Do you want to Delete")
                .setIcon(R.drawable.ic_delete_forever_white_24dp)

                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                        Intent classIntent = new Intent();
                        classIntent.putExtra("classID", i.getStringExtra("classID"));
                        classIntent.putExtra("action", ActivityConstants.Actions.DELETE);
                        setResult(1, classIntent);
                        finish();
                    }
                })

                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        return myQuittingDialogBox;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch((ActivityConstants.Activities.fromInteger(requestCode)))
        {
            case MANAGE_CLASS_MEMBER_ACTIVITY:
                if (resultCode != RESULT_CANCELED) {
                    ActivityConstants.Actions action = (ActivityConstants.Actions) data.getSerializableExtra("action");
                    switch (action) {
                        case CREATE:
                            addMember(data.getStringExtra("classID"), data.getStringExtra("name"), data.getStringExtra("pin"));
                            break;
                        case UPDATE:
                            updateMember(data.getStringExtra("memberID"), data.getStringExtra("name"), data.getStringExtra("pin"), data.getStringExtra("oldPin"));
                            break;
                        case DELETE:
                            removeMember(data.getStringExtra("classID"), data.getStringExtra("memberID"));
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

    private void updateMember(String memberID, String name, String pin, String oldPin) {
        Firebase memberRef = new Firebase(FirebaseConstants.MEMBERS + memberID);
        memberRef.child("name").setValue(name);
        memberRef.child("pin").setValue(pin);

        Firebase classRef = new Firebase(FirebaseConstants.CLASSES + currentClassID + "/students/");
        classRef.child(pin).setValue(memberID);
        classRef.child(oldPin).removeValue();

        Member toBeUpdated = null;
        for(Member m : memberArrayList) {
            if(m.memberID.equals(memberID)) {
                toBeUpdated = m;
            }
        }

        if (toBeUpdated != null) {
            toBeUpdated.pin = pin;
            toBeUpdated.name = name;
        }

        adapter.notifyDataSetChanged();
    }

    private void removeMember(String classID, String memberID) {
        Firebase classRef = new Firebase(FirebaseConstants.CLASSES + classID);
        classRef.child("students").child(memberID).removeValue();

        Member toBeRemoved = null;
        for(Member m : memberArrayList) {
            if(m.memberID.equals(memberID)) {
                toBeRemoved = m;
            }
        }

        if (toBeRemoved != null) {
            memberArrayList.remove(toBeRemoved);
        }

        adapter.notifyDataSetChanged();
    }

    protected void addMember(String classID, String name, String pin) {
        Firebase ref = new Firebase(FirebaseConstants.MEMBERS);
        ref = ref.push();
        ref.child("name").setValue(name);
        ref.child("classes").child(classID).setValue(classID);
        ref.child("pin").setValue(pin);

        Firebase classRef = new Firebase(FirebaseConstants.CLASSES + classID);
        classRef.child("students").child(pin).setValue(ref.getKey());

        adapter.notifyDataSetChanged();

    }
}
