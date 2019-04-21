package us.cm.trabajodecurso;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {

    // Firebase instance variables
    private FirebaseAuth mAuth;
    private GoogleApiClient mGoogleApiClient;

    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;

    private SignInButton mSignInButton;
    private ProgressBar progressfeedback;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Inicio de sesión");
        actionBar.setDisplayHomeAsUpEnabled(true); //Activa boton atras

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        progressfeedback = (ProgressBar) findViewById(R.id.login_progressBar);
        progressfeedback.setVisibility(View.GONE);


        Button emailpassloginbutt = findViewById(R.id.emailpassloginbutt);
        emailpassloginbutt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = ((EditText) findViewById(R.id.loginMail)).getText().toString();
                String pass = ((EditText) findViewById(R.id.loginpass)).getText().toString();

                if (checkPass(mail,pass))
                    firebaseAuthContraseñaMailLogin(mail, pass);
            }
        });

        TextView infoCreaCuenta = (TextView) findViewById(R.id.infoCreacuenta);
        infoCreaCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = ((EditText) findViewById(R.id.loginMail)).getText().toString();
                String pass = ((EditText) findViewById(R.id.loginpass)).getText().toString();

                if (checkPass(mail, pass)){
                    firebaseAuthContraseñaMailCreate(mail,pass);
                }
            }
        });


        //Firebase

        // Assign fields
        mSignInButton = (SignInButton) findViewById(R.id.sign_in_button);

        // Set click listeners
        mSignInButton.setOnClickListener(this);

        //TODO: QUITAR LA STRING ID, NO SOLO COMERTARLA!!!!
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        // Initialize FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

    }


    private boolean checkPass(String mail, String pass) {
        if (!mail.isEmpty() && pass.length()>=8)
            return true;
        else
        if (mail.isEmpty())
            ((EditText) findViewById(R.id.loginMail)).setError("El mail no puede estar vacio");
        if (pass.isEmpty())
            ((EditText) findViewById(R.id.loginpass)).setError("La contraseña no puede estar vacia");
        if (pass.length()<8)
            ((EditText) findViewById(R.id.loginpass)).setError("La logitud de la contraseña tiene que ser mayor de 8 caracteres");
        return false;
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    private void handleFirebaseAuthResult(AuthResult authResult) {
        if (authResult != null) {
            // Welcome the user
            FirebaseUser user = authResult.getUser();
            Toast.makeText(this, "Welcome " + user.getEmail(), Toast.LENGTH_SHORT).show();

            // Go back to the main activity
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                Log.i("SignIn","SignIn button pressed");
                progressfeedback.setVisibility(View.VISIBLE);
                signIn();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign-In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);


                // Creacion del nodo para el nuevo usuario en la base de datos
                String userID = mAuth.getCurrentUser().getUid();
                DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("usuarios").child(userID).child("susreservas");
                Map newPost = new HashMap<>();
                newPost.put("0",0);
                current_user_db.setValue(newPost);


            } else {
                // Google Sign-In failed
                Log.e(TAG, "Google Sign-In failed.");
            }
        }
    }


    private void firebaseAuthContraseñaMailLogin(String email, String password){
        progressfeedback.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            handleFirebaseAuthResult(task.getResult());

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "No se ha podido iniciar sesión.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    private void firebaseAuthContraseñaMailCreate(final String email, String password){
        progressfeedback.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {


                            // Creacion del nodo para el nuevo usuario en la base de datos

                            String userID = mAuth.getCurrentUser().getUid();
                            DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("usuarios").child(userID);
                            Map newPost = new HashMap<>();
                            Map reservasVacias = new HashMap<>();
                            reservasVacias.put("0",0);
                            //La reserva inicial es 0, reserva nula

                            newPost.put("susreservas",reservasVacias);
                            current_user_db.setValue(newPost);





                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            Toast.makeText(LoginActivity.this, "Se ha creado la cuenta para "+email,
                                    Toast.LENGTH_SHORT).show();


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "No se ha podido crear la cuenta",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        progressfeedback.setVisibility(View.VISIBLE);
        Log.d(TAG, "firebaseAuthWithGooogle:" + acct.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        }
                    }
                });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }
    public boolean onSupportNavigateUp(){
        finish();
        return super.onSupportNavigateUp();
    }
}
