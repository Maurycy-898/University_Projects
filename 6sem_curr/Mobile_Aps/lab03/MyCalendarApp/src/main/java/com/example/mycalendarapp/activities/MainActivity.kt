package com.example.mycalendarapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CalendarView
import android.widget.TextView
import com.example.mycalendarapp.Date
import com.example.mycalendarapp.DateEvents
import com.example.mycalendarapp.R
import com.example.mycalendarapp.UNSET_DATE
import java.util.*

class MainActivity : AppCompatActivity() {
    private var selectedDate = Date(UNSET_DATE, UNSET_DATE, UNSET_DATE)
    private lateinit var calendarView: CalendarView
    private lateinit var addEventBtn: Button
    private lateinit var showEventsBtn: Button
    private lateinit var closestEventTitle: TextView
    private lateinit var closestEventText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        calendarView = findViewById(R.id.main_calendar)
        addEventBtn = findViewById(R.id.add_event_btn)
        showEventsBtn = findViewById(R.id.show_events_btn)
        closestEventTitle = findViewById(R.id.closest_event_title)
        closestEventText = findViewById(R.id.edit_event_description)

        selectedDate = getCurrentDate()
        calendarView.setOnDateChangeListener { _, year, month, day ->
            // For some reason month is numerated from 0 - 11
            selectedDate = Date(day, month+1, year)
        }
        updateClosestEvent()
    }

    override fun onResume() {
        super.onResume()
        updateClosestEvent()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("year", selectedDate.year)
        outState.putInt("month", selectedDate.month)
        outState.putInt("day", selectedDate.day)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val year = savedInstanceState.getInt("year")
        val month = savedInstanceState.getInt("month")
        val day = savedInstanceState.getInt("day")
        selectedDate = Date(day, month, year)
        setCalendarViewDate(selectedDate)
    }

    fun addEvent(view: View) {
        val addEventIntent = Intent(this, CreateEventActivity::class.java)
        addEventIntent.putExtra("hasTexts", "NO")
        addEventIntent.putExtra("year", selectedDate.year)
        addEventIntent.putExtra("month", selectedDate.month)
        addEventIntent.putExtra("day", selectedDate.day)
        startActivity(addEventIntent)
    }

    fun showEvents(view: View) {
        val listEventsIntent = Intent(this, ListEventsActivity::class.java)
        startActivity(listEventsIntent)
    }

    private fun updateClosestEvent() {
        if (DateEvents.eventList.isNotEmpty()) {
            DateEvents.sortEventsByDate()
            val closestEvent = DateEvents.eventList.first()
            closestEventTitle.text = closestEvent.genTitle()
            closestEventText.text = closestEvent.description
        } else {
            closestEventTitle.text = "Your closest event title"
            closestEventText.text = "Your closest event description"
        }
    }

    private fun getCurrentDate() : Date {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        return Date(day, month+1, year)
    }

    private fun setCalendarViewDate(date: Date) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, date.year)
        calendar.set(Calendar.MONTH, date.month-1)
        calendar.set(Calendar.DAY_OF_MONTH, date.day)
        calendarView.setDate(calendar.timeInMillis, true, true)
    }
}
