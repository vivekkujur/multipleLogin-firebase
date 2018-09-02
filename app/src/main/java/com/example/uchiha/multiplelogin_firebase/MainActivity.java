package com.example.uchiha.multiplelogin_firebase;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {


    private DatabaseReference mdatabaseuser;
    private FirebaseAuth mAuth;
    private  FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mdatabaseuser= FirebaseDatabase.getInstance().getReference().child("Users");
        mdatabaseuser.keepSynced(true);


        mAuth = FirebaseAuth.getInstance();

        authStateListener= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()== null){
                    Intent login_intent= new Intent(MainActivity.this,LoginActivity.class) ;
                    login_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(login_intent);

                }
            }
        };

        mAuth.addAuthStateListener(authStateListener);


    }


    @Override
    protected void onStart() {
        super.onStart();

        checkUserExist();

        mAuth.addAuthStateListener(authStateListener);
    }


   // check if user exist ...............................................
    private void checkUserExist() {

        if(mAuth.getCurrentUser()!=null) {
            final String user_id = mAuth.getCurrentUser().getUid();

            mdatabaseuser.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (!dataSnapshot.hasChild(user_id)) {

                        Intent setup_inten = new Intent(MainActivity.this, SetupActivity.class);
                        setup_inten.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(setup_inten);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.item_menu,menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()== R.id.signout){

            logout();
        }

        if(item.getItemId()== R.id.profile){

            profile();
        }
        return super.onOptionsItemSelected(item);
    }

    private void profile() {

        Intent intent= new Intent(MainActivity.this,ProfileActivity.class);
        startActivity(intent);


    }

    private void logout() {
        // mAuth.getCurrentUser().delete();
        mAuth.signOut();
    }
}
