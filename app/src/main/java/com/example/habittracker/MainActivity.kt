package com.example.habittracker

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import kotlinx.android.synthetic.main.activity_main.*

interface HabitsClickListener {
    fun onListItemClick(clickedItem: Int)
}

class MainActivity : AppCompatActivity(), HabitsClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: Adapter
    private var lastClicked = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        title = "Эти привычки"

        recyclerView = findViewById(R.id.habits_view)
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                recyclerView.context,
                DividerItemDecoration.VERTICAL
            )
        )

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = Adapter(mutableListOf(), this)
        recyclerView.adapter = adapter

        fab.setOnClickListener {
            val intent = Intent(this, HabitCreator::class.java).apply {
                this.putExtra("Mode", "Create")
            }
            startActivityForResult(intent, CREATE_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        if (requestCode == CREATE_CODE && data != null) {
            val newHabit: Habit = data.extras?.getSerializable("NEW_HABIT") as Habit
            adapter.addElement(newHabit)
        }
        if (requestCode == EDIT_CODE && data != null) {
            val editedHabit: Habit = data.extras?.getSerializable("NEW_HABIT") as Habit
            adapter.editElement(editedHabit, lastClicked)
        }
        lastClicked = -1
    }
    override fun onListItemClick(clickedItem: Int) {
        lastClicked = clickedItem
        val clicked = adapter.getElement(clickedItem)
        val intent = Intent(this, HabitCreator::class.java).apply {
            this.putExtra("Mode", "Edit")
            this.putExtra("Editable", clicked)
        }
        startActivityForResult(intent, EDIT_CODE)
    }

    class Adapter(
        private val values: MutableList<Habit>,
        private val listener: HabitsClickListener
    ) :
        RecyclerView.Adapter<Adapter.ViewHolder>() {

        class ViewHolder(itemView: View, private val listener: HabitsClickListener) :
            RecyclerView.ViewHolder(itemView), View.OnClickListener {

            val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
            val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
            val priorityTextView: TextView = itemView.findViewById(R.id.priorityTextView)
            val typeTextView: TextView = itemView.findViewById(R.id.typeTextView)
            val repeatsTextView: TextView = itemView.findViewById(R.id.repeatsTextView)
            val periodTextView: TextView = itemView.findViewById(R.id.periodTextView)

            override fun onClick(v: View?) {
                val clicked = adapterPosition
                listener.onListItemClick(clicked)
            }

            init {
                itemView.setOnClickListener(this)
            }
        }

        fun addElement(habit: Habit) {
            values.add(habit)
            notifyItemInserted(values.indexOf(habit))
        }

        fun getElement(id: Int): Habit = values[id]

        fun editElement(new: Habit, position: Int) {
            val edited = values[position]
            edited.name = new.name
            edited.description = new.description
            edited.priority = new.priority
            edited.type = new.type
            edited.repeats = new.repeats
            edited.period = new.period
            notifyItemChanged(position)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.habit_list_element, parent, false)
            return ViewHolder(itemView, listener)
        }

        override fun getItemCount(): Int = values.size

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val habit = values[position]
            holder.nameTextView.text = habit.name
            holder.descriptionTextView.text = when {
                habit.description.isEmpty() -> "Описание: нет"
                else -> habit.description
            }
            holder.priorityTextView.text = "Приоритет: ${prioritiesMap[habit.priority]}"
            holder.typeTextView.text = typesMap[habit.type]
            holder.repeatsTextView.text = "Повторения: ${habit.repeats}"
            holder.periodTextView.text = "Период: ${habit.period}"
        }
    }
}
