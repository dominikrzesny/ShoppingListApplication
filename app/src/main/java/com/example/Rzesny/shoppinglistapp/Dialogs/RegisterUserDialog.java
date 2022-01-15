package com.example.Rzesny.shoppinglistapp.Dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.Rzesny.shoppinglistapp.Activities.LoginActivity;
import com.example.Rzesny.shoppinglistapp.Activities.MainActivity;
import com.example.Rzesny.shoppinglistapp.Models.Product;
import com.example.Rzesny.shoppinglistapp.R;
import com.example.Rzesny.shoppinglistapp.Utils.DatabaseUtils;
import com.example.Rzesny.shoppinglistapp.Utils.UserUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterUserDialog extends AppCompatDialogFragment {

    private Activity activity;
    private EditText loginEditText;
    private EditText passwordEditText;
    private EditText repeatPasswordEditText;
    private EditText emailEditText;
    private String Title;
    private FirebaseAuth firebaseAuth;

    public RegisterUserDialog(Activity activity, FirebaseAuth firebaseAuth){
        this.activity = activity;
        this.firebaseAuth = firebaseAuth;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_register_user,null);
        loginEditText = view.findViewById(R.id.LoginEditText);
        passwordEditText = view.findViewById(R.id.passwordEditText2);
        emailEditText = view.findViewById(R.id.emailEditText2);
        repeatPasswordEditText = view.findViewById(R.id.repeatPasswordEditText);
        Title = getResources().getString(R.string.RegisterUserDialogLabel);

        builder.setView(view)
                .setTitle(Title)
                .setNegativeButton(getResources().getString(R.string.CancelButton), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                }).setPositiveButton(getResources().getString(R.string.RegisterUserDialogLabel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        return builder.create();
    }

    @Override
    public void onResume() {
        super.onResume();
        AlertDialog dialog = (AlertDialog)getDialog();
        if(dialog != null)
        {
            Button positiveButton = (Button) dialog.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    String email = emailEditText.getText().toString().trim();
                    String password = passwordEditText.getText().toString().trim();
                    String repeatedPassword = repeatPasswordEditText.getText().toString().trim();
                    String login = loginEditText.getText().toString().trim();

                    String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
                    Pattern pattern = Pattern.compile(expression,Pattern.CASE_INSENSITIVE);
                    Matcher matcher = pattern.matcher(email);

                    if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(repeatedPassword) || TextUtils.isEmpty(login)){
                        Toast.makeText(activity.getBaseContext(), getResources().getString(R.string.EmptyCredentialsMessage), Toast.LENGTH_SHORT).show();
                    }
                    else if(!matcher.matches()){
                        Toast.makeText(activity.getBaseContext(), getResources().getString(R.string.InvalidEmailFormat), Toast.LENGTH_SHORT).show();
                    }
                    else if(password.length()<6){
                        Toast.makeText(activity.getBaseContext(), getResources().getString(R.string.PasswordTooShort), Toast.LENGTH_SHORT).show();
                    }
                    else if(!password.equals(repeatedPassword)){
                        Toast.makeText(activity.getBaseContext(), getResources().getString(R.string.DifferentPasswords), Toast.LENGTH_SHORT).show();
                    }
                    else {
                        firebaseAuth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(Task<AuthResult> task) {
                                        if (!task.isSuccessful()) {
                                            Toast.makeText(activity.getBaseContext(), activity.getResources().getString(R.string.RegisterError), Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                        } else {
                                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                    .setDisplayName(login).build();
                                            user.updateProfile(profileUpdates);
                                            Toast.makeText(activity.getBaseContext(), activity.getResources().getString(R.string.RegisterSuccessfull), Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                        }
                                    }
                                });
                    }
                }
            });
        }
    }
}
