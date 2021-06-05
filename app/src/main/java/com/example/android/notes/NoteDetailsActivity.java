package com.example.android.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class NoteDetailsActivity extends AppCompatActivity {

    private TextView mTitleNoteDetail, mContentNoteDetail;
    private FloatingActionButton mEditNoteFab;
    private ImageView mBack_noteDetails;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);

        mTitleNoteDetail = findViewById(R.id.title_noteDetails);
        mContentNoteDetail = findViewById(R.id.content_noteDetails);
        mEditNoteFab = findViewById(R.id.edit_note_fab);
        mBack_noteDetails = findViewById(R.id.back_NoteDetails);

        mBack_noteDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        toolbar = findViewById(R.id.toolbar_noteDetails);
        setSupportActionBar(toolbar);


        Intent data = getIntent();

        mEditNoteFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), EditNoteActivity.class);
                intent.putExtra("title",data.getStringExtra("title"));
                intent.putExtra("content",data.getStringExtra("content"));
                intent.putExtra("noteId",data.getStringExtra("noteId"));
                view.getContext().startActivity(intent);
            }
        });

        mContentNoteDetail.setText(data.getStringExtra("content"));
        mTitleNoteDetail.setText(data.getStringExtra("title"));

    }
}