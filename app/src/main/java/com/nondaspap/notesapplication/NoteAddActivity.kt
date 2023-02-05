package com.nondaspap.notesapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class NoteAddActivity : AppCompatActivity() {

    private lateinit var titleEditText: EditText
    private lateinit var descriptionMultilineEditText: EditText
    private lateinit var cancelButton: Button
    private lateinit var saveButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_add)

        supportActionBar?.title  = "Add Note"

        initComponents()
        attachListeners()

    }



    private fun initComponents() {
        titleEditText = findViewById(R.id.titleEditText)
        descriptionMultilineEditText = findViewById(R.id.editTextTextMultiLine)
        cancelButton = findViewById(R.id.cancelButton)
        saveButton = findViewById(R.id.saveButton)
    }



    private fun attachListeners() {
        cancelButton.setOnClickListener {
            finish()
        }

        saveButton.setOnClickListener {
           saveNote()
        }
    }


    private fun saveNote() {
        val title = titleEditText.text.toString()
        val description = descriptionMultilineEditText.text.toString()

        val intent = Intent()
        intent.putExtra("title", title)
        intent.putExtra("description", description)

        setResult(RESULT_OK, intent)
        finish()
    }
}