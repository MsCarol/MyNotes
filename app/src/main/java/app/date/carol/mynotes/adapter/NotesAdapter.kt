package app.date.carol.mynotes.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import app.date.carol.mynotes.R
import app.date.carol.mynotes.models.Note
import kotlin.random.Random

class NotesAdapter(private val context: Context,
                   val listener : NotesClickListener) : RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {

    private val notesList = ArrayList<Note>()
    private val fullList = ArrayList<Note>()

    class NotesViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val title = itemView.findViewById<TextView>(R.id.tvTitle)
        val notes = itemView.findViewById<TextView>(R.id.tvNote)
        val dated = itemView.findViewById<TextView>(R.id.tvDate)
        val notes_layout = itemView.findViewById<CardView>(R.id.cardview)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return NotesViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        holder.title.text = notesList[position].title
        holder.title.isSelected = true
        holder.notes.text = notesList[position].note
        holder.dated.text = notesList[position].date
        holder.dated.isSelected = true

        holder.notes_layout.setBackgroundColor(holder.itemView.resources.getColor(randomColor(), null))

        holder.notes_layout.setOnClickListener {
            listener.onItemClicked(notesList[holder.adapterPosition])
        }

        holder.notes_layout.setOnLongClickListener {
            listener.onLongItemClicked(notesList[holder.adapterPosition], holder.notes_layout)
            true
        }
    }

    override fun getItemCount(): Int {
        return notesList.size
    }

    fun updateList(newList: List<Note>){
        fullList.clear()
        fullList.addAll(newList)

        notesList.clear()
        notesList.addAll(fullList)
        notifyDataSetChanged()
    }

    fun filterList(search : String){

        notesList.clear()
        for (item in fullList){
            if (item.title?.lowercase()?.contains(search.lowercase()) == true ||
                    item.note?.lowercase()?.contains(search.lowercase()) == true) {
                notesList.add(item)
            }

        }
        notifyDataSetChanged()

    }

    fun randomColor () : Int {
        val list = ArrayList<Int>()
        list.add(R.color.Algae_Green)
        list.add(R.color.Army_Brown)
        list.add(R.color.Baby_Blue)
        list.add(R.color.Basket_Ball_Orange)
        list.add(R.color.Blood_Red)
        list.add(R.color.Caramel)
        list.add(R.color.Bright_Neon_Pink)
        list.add(R.color.Chilli_Pepper)
        list.add(R.color.Deep_Peach)
        list.add(R.color.Eggplant)

        val seed = System.currentTimeMillis().toInt()
        val randomIndex = Random(seed).nextInt(list.size)
        return list[randomIndex]

    }

    interface NotesClickListener {
        fun onItemClicked(note: Note)
        fun onLongItemClicked(note: Note, cardView: CardView)
    }
}