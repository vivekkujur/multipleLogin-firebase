package com.example.uchiha.multiplelogin_firebase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private ImageView imageView;
    private TextView textView;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth=FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());

        textView=findViewById(R.id.profileName);
        imageView=findViewById(R.id.profileImage);


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                Map<String,String> map= (Map)dataSnapshot.getValue();
                String firstname= map.get("first_name");
                String lastname=map.get("last_name");
                String imageurl=map.get("image");

                textView.setText(firstname+" "+lastname);
                Picasso.with(getApplicationContext()).load(imageurl).into(imageView);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
