package itp341.truong.steven.presence;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.ConditionVariable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

public class ManageMemberActivity extends AppCompatActivity {

    EditText nameEditText, pinEditText;
    Button saveButton, deleteButton;

    Intent i;
    boolean newMember;
    String currentClassID;
    String originalPin;
    ArrayList<String> usedPins;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_member);

        i  = getIntent();
        newMember = i.getBooleanExtra("newmember", true);
        currentClassID = i.getStringExtra("classID");
        usedPins = i.getStringArrayListExtra("usedPins");

        nameEditText = (EditText) findViewById(R.id.memberNameEditText);

        pinEditText = (EditText) findViewById(R.id.memberPinEditText);

        saveButton = (Button) findViewById(R.id.submitNewMember);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent memberIntent = new Intent();
                if (!TextUtils.isEmpty(nameEditText.getText().toString()) && !TextUtils.isEmpty(pinEditText.getText().toString()) && pinApproved() ) {
                    memberIntent.putExtra("name", nameEditText.getText().toString());
                    memberIntent.putExtra("pin", pinEditText.getText().toString());
                    memberIntent.putExtra("classID", currentClassID);
                    memberIntent.putExtra("memberID", i.getStringExtra("memberID"));
                    memberIntent.putExtra("oldPin", originalPin);

                    if (newMember) {
                        memberIntent.putExtra("action", ActivityConstants.Actions.CREATE);
                    }
                    else {
                        memberIntent.putExtra("action", ActivityConstants.Actions.UPDATE);
                    }

                    setResult(1, memberIntent);
                    finish();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Can't have empty fields!", Toast.LENGTH_LONG).show();
                }
            }
        });


        deleteButton = (Button) findViewById(R.id.removeMember);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = AskOption();
                dialog.show();
            }
        });

        if (newMember) {
            deleteButton.setVisibility(View.GONE);
        }
        else {
            nameEditText.setText(i.getStringExtra("name"));
            pinEditText.setText(i.getStringExtra("pin"));
            originalPin = i.getStringExtra("pin");
        }
    }

    //Iterate through pins and decide if it's okay
    private boolean pinApproved() {

        //Ensures you can keep your original pin on update
        if (!newMember && (pinEditText.getText().toString().length() == 4) &&originalPin.equals(pinEditText.getText().toString())) {
            return true;
        }

        if (!(pinEditText.getText().toString().length() == 4)) {
            Toast.makeText(getApplicationContext(), "Pin must be 4 numbers!", Toast.LENGTH_LONG).show();
            return false;
        }
        else {
            for (String pin : usedPins) {
                if (pin.equals(pinEditText.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Someone in class already has that pin!", Toast.LENGTH_LONG).show();
                    return false;
                }
            }
        }

        return true;
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
                        Intent memberIntent = new Intent();
                        memberIntent.putExtra("memberID", i.getStringExtra("memberID"));
                        memberIntent.putExtra("classID", i.getStringExtra("classID"));
                        memberIntent.putExtra("pin",i.getStringExtra("pin"));
                        memberIntent.putExtra("action", ActivityConstants.Actions.DELETE);
                        setResult(1, memberIntent);
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
}
