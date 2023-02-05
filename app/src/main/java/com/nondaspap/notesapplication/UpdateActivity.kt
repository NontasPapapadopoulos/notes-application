package com.nondaspap.notesapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class UpdateActivity : AppCompatActivity() {

    private lateinit var updateTitleEditText: EditText
    private lateinit var updateDescriptionMultilineEditText: EditText
    private lateinit var cancelUpdateButton: Button
    private lateinit var saveUpdateButton: Button
    private var currentId: Int = -1



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)

        initComponents()
        attachListeners()
        getAndSetData()
        supportActionBar?.title  = "Add Note"

    }



    private fun initComponents() {
        updateTitleEditText = findViewById(R.id.updateTitleEditText)
        updateDescriptionMultilineEditText = findViewById(R.id.updateEditTextTextMultiLine)
        cancelUpdateButton = findViewById(R.id.cancelUpdateButton)
        saveUpdateButton = findViewById(R.id.saveUpdateButton)
    }



    private fun attachListeners() {
        cancelUpdateButton.setOnClickListener {
            finish()
        }

        saveUpdateButton.setOnClickListener {
            updateNote()
        }
    }


    private fun getAndSetData() {
        // get
        val currentTitle = intent.getStringExtra("currentTitle").toString()
        val currentDescription = intent.getStringExtra("currentDescription").toString()
        currentId = intent.getIntExtra("currentId", -1).toInt()

        // set
        updateTitleEditText.setText(currentTitle)
        updateDescriptionMultilineEditText.setText(currentDescription)
    }


    private fun updateNote() {
        val title = updateTitleEditText.text.toString()
        val description = updateDescriptionMultilineEditText.text.toString()

        val intent = Intent()
        intent.putExtra("updatedTitle", title)
        intent.putExtra("updatedDescription", description)

        if (currentId != -1) {
            intent.putExtra("noteId", currentId)
            setResult(RESULT_OK, intent)
        }
        finish()
    }
}