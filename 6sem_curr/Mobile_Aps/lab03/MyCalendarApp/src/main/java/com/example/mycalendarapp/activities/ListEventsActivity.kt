package com.example.mycalendarapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mycalendarapp.*

class ListEventsActivity : AppCompatActivity(), OnDateEventEditListener, OnDateEventDeleteListener {
    private lateinit var eventsRecyclerView: RecyclerView
    private lateinit var recyclerAdapter: EventsRecyclerAdapter
    private lateinit var addEventButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_events)

        eventsRecyclerView = findViewById(R.id.events_recycler_view)
        eventsRecyclerView.layoutManager = LinearLayoutManager(this).apply {
            this.orientation = LinearLayoutManager.VERTICAL
        }
        recyclerAdapter = EventsRecyclerAdapter(this, this, this)
        eventsRecyclerView.adapter = recyclerAdapter
    }

    fun addEvent(view: View) {
        val addEventIntent = Intent(this, CreateEventActivity::class.java)
        startActivity(addEventIntent)
    }

    override fun onDateEventEdit(dateEvent: DateEvent) {
        val addEventIntent = Intent(this, CreateEventActivity::class.java)
        addEventIntent.putExtra("hasTexts", "YES")
        addEventIntent.putExtra("title", dateEvent.title)
        addEventIntent.putExtra("description", dateEvent.description)
        addEventIntent.putExtra("year", dateEvent.date.year)
        addEventIntent.putExtra("month", dateEvent.date.month)
        addEventIntent.putExtra("day", dateEvent.date.day)
        DateEvents.eventList.removeAt(dateEvent.id)
        startActivity(addEventIntent)
    }

    override fun onDateEventDelete(dateEvent: DateEvent) {
        DateEvents.eventList.removeAt(dateEvent.id)
        recyclerAdapter.notifyItemRemoved(dateEvent.id)
    }

    override fun onResume() {
        super.onResume()
        recyclerAdapter.notifyDataSetChanged()
    }
}