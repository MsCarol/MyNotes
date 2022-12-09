package app.date.carol.mynotes

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.SearchView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import app.date.carol.mynotes.adapter.NotesAdapter
import app.date.carol.mynotes.database.NoteDatabase
import app.date.carol.mynotes.databinding.ActivityMainBinding
import app.date.carol.mynotes.models.Note
import app.date.carol.mynotes.models.NoteViewModel

class MainActivity : AppCompatActivity() , NotesAdapter.NotesClickListener, PopupMenu.OnMenuItemClickListener{

    private lateinit var binding: ActivityMainBinding
    private lateinit var database: NoteDatabase
     lateinit var adapter: NotesAdapter
     lateinit var viewModel: NoteViewModel
     lateinit var selectedNote : Note
     private val updateNote  = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
         result ->
         if (result.resultCode == Activity.RESULT_OK){
             val note = result.data?.getSerializableExtra("note") as? Note
             if (note != null) {
                 viewModel.updateNote(note)
             }
         }

     }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()
        viewModel = ViewModelProvider(this,
        ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(NoteViewModel::class.java)

        viewModel.allNotes.observe(this) { list ->
            list?.let {
                adapter.updateList(list)
            }

        }
        database = NoteDatabase.getDatabase(this)



    }

    private fun initUI() {
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = StaggeredGridLayoutManager(2, LinearLayout.VERTICAL)
        adapter = NotesAdapter(this, this)
        binding.recyclerView.adapter = adapter

        val getContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result ->
            if (result.resultCode == Activity.RESULT_OK){
                val note = result.data?.getSerializableExtra("note") as? Note
                if (note != null){
                    viewModel.insertNote(note)
                }

            }
        }

        binding.floatingActionButton.setOnClickListener {
            val intent = Intent(this, AddNote::class.java)
            getContent.launch(intent)

        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if(newText != null){
                    adapter.filterList(newText)
                }
                return true
            }

        })
    }

    override fun onItemClicked(note: Note) {
        val intent = Intent(this, AddNote::class.java)
        intent.putExtra("current_note", note)
        updateNote.launch(intent)
    }

    override fun onLongItemClicked(note: Note, cardView: CardView) {
        selectedNote = note
        popUpDisplay(cardView)
    }

    private fun popUpDisplay(cardView: CardView) {
        val popup = PopupMenu(this, cardView)
        popup.setOnMenuItemClickListener(this@MainActivity)
        popup.inflate(R.menu.pop_up_menu)
        popup.show()

    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.delete) {
            viewModel.deleteNote(selectedNote)
            return true
        }
        return false
    }

}