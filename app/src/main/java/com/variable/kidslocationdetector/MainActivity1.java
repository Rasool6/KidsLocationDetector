package com.variable.kidslocationdetector;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity1 extends AppCompatActivity {

    EditText edtKey;
    TextView btnGo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);

        edtKey=findViewById(R.id.edtKey);
        btnGo=findViewById(R.id.btnEnter);



        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str_edtKey =edtKey.getText().toString();

                addToFirebase(str_edtKey);
            }
        });
    }



    private void addToFirebase(String str_edtKey) {

        FirebaseDatabase.getInstance().getReference().child("Child")
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1:snapshot.getChildren()){
                    if (snapshot1.child("pin_genrate").getValue().equals(str_edtKey) ){

                     Constants.pinID=   snapshot1.child("pin_genrate").getValue().toString();

//                        SharedPreferences preferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
//                        SharedPreferences.Editor editor = preferences.edit();
//                        editor.putString("pinId",snapshot1.child("pin_genrate").getValue().toString());
//                        editor.apply();
                        Toast.makeText(MainActivity1.this, "Login Successfully...!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity1.this,MainActivity.class)
                        .putExtra("pin",snapshot1.child("pin_genrate").getValue().toString())
                        );
                        finish();

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity1.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



    }
}