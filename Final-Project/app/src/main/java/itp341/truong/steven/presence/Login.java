package itp341.truong.steven.presence;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.Map;

public class Login extends AppCompatActivity {


    //Firebase
    Firebase firebaseReference;

    //UI
    EditText usernameEditText, passwordEditText;
    Button signInButton, signUpButton;
    TextView forgotPasswordTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initializeReferences();

        Firebase.setAndroidContext(this);
        firebaseReference = new Firebase("https://presence-manager.firebaseio.com/");
    }

    private void initializeReferences() {
        usernameEditText = (EditText) findViewById(R.id.username);
        passwordEditText = (EditText) findViewById(R.id.password);

        signInButton = (Button) findViewById(R.id.signIn);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkConnected() && !TextUtils.isEmpty(usernameEditText.getText().toString()) && Patterns.EMAIL_ADDRESS.matcher(usernameEditText.getText().toString()).matches() && passwordEditText.getText().toString().length() >= 6) {
                    firebaseReference.authWithPassword(usernameEditText.getText().toString(), passwordEditText.getText().toString(), new Firebase.AuthResultHandler() {
                        @Override
                        public void onAuthenticated(AuthData authData) {
                            Intent i = new Intent(getApplication(), MainActivity.class);
                            i.putExtra("uid", authData.getUid());
                            startActivityForResult(i, 1);
                        }

                        @Override
                        public void onAuthenticationError(FirebaseError firebaseError) {
                            Toast.makeText(getApplicationContext(), firebaseError.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    realizeErrors();
                }
            }
        });

        signUpButton = (Button) findViewById(R.id.signUp);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkConnected() && !TextUtils.isEmpty(usernameEditText.getText().toString()) && Patterns.EMAIL_ADDRESS.matcher(usernameEditText.getText().toString()).matches() && passwordEditText.getText().toString().length() >= 6) {
                    firebaseReference.createUser(usernameEditText.getText().toString(), passwordEditText.getText().toString(), new Firebase.ValueResultHandler<Map<String, Object>>() {
                        @Override
                        public void onSuccess(Map<String, Object> result) {
                            Firebase ref = new Firebase("https://presence-manager.firebaseio.com/users/"+result.get("uid").toString());
                            ref.child("email").setValue(usernameEditText.getText().toString());

                            Intent i = new Intent(getApplication(), MainActivity.class);
                            i.putExtra("uid", result.get("uid").toString());
                            startActivityForResult(i, 1);                        }

                        @Override
                        public void onError(FirebaseError firebaseError) {
                            Toast.makeText(getApplicationContext(), firebaseError.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    realizeErrors();
                }
            }
        });

        forgotPasswordTextView = (TextView) findViewById(R.id.forgotPassword);
        forgotPasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkConnected() && !TextUtils.isEmpty(usernameEditText.getText().toString()) && Patterns.EMAIL_ADDRESS.matcher(usernameEditText.getText().toString()).matches()) {
                    firebaseReference.resetPassword(usernameEditText.getText().toString(), new Firebase.ResultHandler() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(getApplicationContext(), "E-mail sent to " + usernameEditText.getText().toString() + ".", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onError(FirebaseError firebaseError) {
                            Toast.makeText(getApplicationContext(), firebaseError.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
                else {
                    if (!isNetworkConnected()) {
                        Toast.makeText(getApplicationContext(), "No internet connection.", Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Please enter e-mail address associated with your account.", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    private void realizeErrors() {
        if (!isNetworkConnected()) {
            Toast.makeText(getApplicationContext(), "No internet connection.", Toast.LENGTH_LONG).show();
        }
        else if (TextUtils.isEmpty(usernameEditText.getText().toString()) || !Patterns.EMAIL_ADDRESS.matcher(usernameEditText.getText().toString()).matches()) {
            Toast.makeText(getApplicationContext(), "The e-mail address is invalid.", Toast.LENGTH_LONG).show();
        }
        else if (passwordEditText.getText().toString().length() < 6) {
            Toast.makeText(getApplicationContext(), "The password isn't long enough.", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(getApplicationContext(), "Something is wrong.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        usernameEditText.setText("");
        passwordEditText.setText("");

        Toast.makeText(getApplicationContext(), "You've been logged out. ", Toast.LENGTH_LONG).show();
        Log.d("Test", "REQ CODE: " + requestCode + " RESULT CODE: " + resultCode);
    }
}
