package com.example.android.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditNoteActivity extends AppCompatActivity {

    Intent data;
    EditText mEditTitle, mEditContent;
    FloatingActionButton mUpdateFab;

    Toolbar toolbar;
    ImageView mBack_EditNote;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        mEditTitle = findViewById(R.id.edit_title_note);
        mEditContent = findViewById(R.id.edit_content_note);
        mUpdateFab = findViewById(R.id.update_note_fab);
        mBack_EditNote = findViewById(R.id.back_EditNote);

        data = getIntent();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        mBack_EditNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        toolbar = findViewById(R.id.toolbar_editNote);
        setSupportActionBar(toolbar);

        String noteTitle = data.getStringExtra("title");
        String noteContent = data.getStringExtra("content");

        mEditTitle.setText(noteTitle);
        mEditContent.setText(noteContent);

        mUpdateFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(EditNoteActivity.this, "Updated note", Toast.LENGTH_SHORT).show();

                String newTitle = mEditTitle.getText().toString();
                String newContent = mEditContent.getText().toString();

                if(newTitle.isEmpty() || newContent.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Title or Note cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    DocumentReference documentReference = firebaseFirestore.collection("notes").document(firebaseUser.getUid())
                            .collection("myNotes").document(data.getStringExtra("noteId"));
                    Map<String, Object> note = new HashMap<>();
                    note.put("title",newTitle);
                    note.put("content",newContent);
                    documentReference.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(EditNoteActivity.this, "Note Updated", Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(new Intent(getApplicationContext(),NotesActivity.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(EditNoteActivity.this, "Failed to update", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });
    }
}