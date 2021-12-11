package com.example.Rzesny.shoppinglistapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.Rzesny.shoppinglistapp.AddProductDialog;
import com.example.Rzesny.shoppinglistapp.R;
import com.example.Rzesny.shoppinglistapp.RegisterUserDialog;
import com.example.Rzesny.shoppinglistapp.Utils.SharedPreferencesUtils;
import com.example.Rzesny.shoppinglistapp.Utils.ThemeUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferencesUtils.loadTheme(this);
        ThemeUtils.onActivityCreateSetTheme(this);
        SharedPreferencesUtils.loadAndSetLocale(this);
        setContentView(R.layout.activity_login);
        ImageView imageView = findViewById(R.id.logoImageView);
        if(ThemeUtils.cTheme==ThemeUtils.BLACK){
            imageView.setImageResource(R.drawable.list);
        }
        if(ThemeUtils.cTheme==ThemeUtils.GREY) {
            imageView.setImageResource(R.drawable.listgrey);
        }
        firebaseAuth = FirebaseAuth.getInstance();
        emailEditText = (EditText) findViewById(R.id.emailEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
    }

    @Override
    public void onBackPressed() {
        //do nothing
    }

    public void onExitAppClickMethod(View view){
        finish();
        System.exit(0);
    }

    public void onLoginButtonClickMethod(View view){

        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
            Toast.makeText(getBaseContext(), getResources().getString(R.string.EmptyCredentialsMessage), Toast.LENGTH_SHORT).show();
        }
        else {
            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                {
                                    Toast.makeText(getBaseContext(), getResources().getString(R.string.AutorisationFailedMessage), Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.putExtra("user", firebaseAuth.getCurrentUser());
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
        }
    }

    public void onAddAccountButtonClickMethod(View view){
        emailEditText.getText().clear();
        passwordEditText.getText().clear();
        RegisterUserDialog dialog= new RegisterUserDialog(LoginActivity.this,firebaseAuth);
        dialog.show(getSupportFragmentManager(),"Lauching dialog");
    }

}
