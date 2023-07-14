package com.example.mycalendarapp.activities

import android.os.Bundle
import android.view.View
import android.widget.CalendarView
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mycalendarapp.*
import com.example.mycalendarapp.Date
import java.util.*

class CreateEventActivity : AppCompatActivity() {
    private var selectedDate = Date(UNSET_DATE, UNSET_DATE, UNSET_DATE)
    private lateinit var titleTextInput: EditText
    private lateinit var descriptionTextInput: EditText
    private lateinit var calendarView: CalendarView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_event)

        titleTextInput = findViewById(R.id.edit_event_title)
        descriptionTextInput = findViewById(R.id.edit_event_description)
        calendarView = findViewById(R.id.create_calendar)

        val year = intent.getIntExtra("year", UNSET_DATE)
        val month = intent.getIntExtra("month", UNSET_DATE)
        val day = intent.getIntExtra("day", UNSET_DATE)
        if (day != UNSET_DATE && month != UNSET_DATE && year != UNSET_DATE) {
            selectedDate = Date(day, month, year)
            println(selectedDate)
            setCalendarViewDate(selectedDate)
        } else { selectedDate = getCurrentDate() }

        val hasTexts = intent.getStringExtra("hasTexts")
        if (hasTexts == "YES") {
            val title = intent.getStringExtra("title")
            val description = intent.getStringExtra("description")
            this.titleTextInput.setText(title)
            this.descriptionTextInput.setText(description)
        }

        calendarView.setOnDateChangeListener { _, y, m, d ->
            // For some reason month is numerated from 0 - 11
            selectedDate = Date(d, m+1, y)
        }
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

    fun confirmAddingEvent(view: View) {
        val title: String = titleTextInput.text.toString()
        val description: String = descriptionTextInput.text.toString()
        if (title == "" || description == "") {
            Toast.makeText(this,
                "Fill the title and description!",
                Toast.LENGTH_SHORT).show()
            return
        }
        DateEvents.eventList.add(DateEvent(selectedDate, title, description))
        DateEvents.updateIds()
        Toast.makeText(this, "Added new event!", Toast.LENGTH_SHORT).show()
        resetTextInputs()
    }

    private fun resetTextInputs() {
        titleTextInput.text.clear()
        descriptionTextInput.text.clear()
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
