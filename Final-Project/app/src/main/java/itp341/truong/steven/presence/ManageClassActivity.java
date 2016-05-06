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
import android.widget.EditText;
import android.widget.Toast;

import org.w3c.dom.Text;

import itp341.truong.steven.presence.dummy.DummyMemberContent;

public class ManageClassActivity extends AppCompatActivity {

    EditText name, detail;
    
    Button addClassButton, removeClassButton;

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
            name.setText(i.getStringExtra("name"));
            detail.setText(i.getStringExtra("details"));
        }
        else {
            removeClassButton.setVisibility(View.GONE);
        }

        addClassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent classIntent = new Intent();
                if (!TextUtils.isEmpty(name.getText().toString()) && !TextUtils.isEmpty(detail.getText().toString())) {
                    classIntent.putExtra("name", name.getText().toString());
                    classIntent.putExtra("detail", detail.getText().toString());

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

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.member_list);
        if (recyclerView instanceof RecyclerView) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            adapter = new MemberRecyclerViewAdapter(DummyMemberContent.ITEMS);
            recyclerView.setAdapter(adapter);
        }

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
                            break;
                        case UPDATE:
                            Log.d("UPDATE", "UPDATE TRIGGERED");
                            break;
                        case DELETE:
                            Log.d("Delete", "DELETE TRIGGERED");
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
}
