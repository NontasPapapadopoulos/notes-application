package com.nondaspap.notesapplication

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var noteViewModel: NoteViewModel
    private lateinit var recyclerView: RecyclerView

    lateinit var addActivityResultLauncher: ActivityResultLauncher<Intent>
    lateinit var updateActivityResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        registerActivityResultLauncher()


        this.recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)


        val notesAdapter = NoteAdapter(this)
        recyclerView.adapter = notesAdapter


        val viewModelFactory = NoteViewModelFactory((application as NoteApplication).repository)

        noteViewModel = ViewModelProvider(this, viewModelFactory).get(NoteViewModel::class.java)

        noteViewModel.myAllNotes.observe(this, Observer { notes ->
            // update UI
            notesAdapter.setNote(notes)
        })

        ItemTouchHelper(object: ItemTouchHelper.SimpleCallback(0,
                                                                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                TODO("Not yet implemented")
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                noteViewModel.delete(notesAdapter.getNote(viewHolder.adapterPosition))
                Toast.makeText(applicationContext, " note deleted", Toast.LENGTH_SHORT).show()
            }

        }).attachToRecyclerView(recyclerView)

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.new_menu, menu)
        return  true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.itemAddNote -> {
                val intent = Intent(this, NoteAddActivity::class.java)
                addActivityResultLauncher.launch(intent)
            }
            R.id.itemDeleteNote -> {
                showDialogMessage()
            }
        }
        return true
    }

    private fun showDialogMessage() {
        val dialogMessage = AlertDialog.Builder(this)
        dialogMessage.setTitle("Delete All Notes")
        dialogMessage.setMessage("Are u sure?")
        dialogMessage.setPositiveButton("Yes, delete", DialogInterface.OnClickListener { dialog, which ->
            noteViewModel.deleteAllNotes()
        })
        dialogMessage.setNegativeButton("No", DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() })

        dialogMessage.create().show()
    }

    fun registerActivityResultLauncher() {
        addActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult(),
              ActivityResultCallback {
                  resultAddNote ->
                  val resultCode = resultAddNote.resultCode
                  val data = resultAddNote.data

                  if (resultCode == RESULT_OK && data != null) {
                      val noteTitle: String = data.getStringExtra("title").toString()
                      val noteDescription: String = data.getStringExtra("description").toString()

                      val note = Note(noteTitle, noteDescription)
                      noteViewModel.insert(note)
                  }
              })


        updateActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult(),
            ActivityResultCallback {
                    resultUpdateCode ->
                val resultCode = resultUpdateCode.resultCode
                val data = resultUpdateCode.data

                if (resultCode == RESULT_OK && data != null) {
                    val noteTitle: String = data.getStringExtra("updatedTitle").toString()
                    val noteDescription: String = data.getStringExtra("updatedDescription").toString()
                    val noteId: Int = data.getIntExtra("noteId", -1)

                    val newNote = Note(noteTitle, noteDescription)
                    newNote.id = noteId

                    noteViewModel.update(newNote)
                }
            })
    }

}