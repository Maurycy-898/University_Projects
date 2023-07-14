package com.example.mycalendarapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

const val UNSET_DATE = -1

class EventsRecyclerAdapter(
    private val editListener: OnDateEventEditListener,
    private val deleteListener: OnDateEventDeleteListener,
    context: Context
) : RecyclerView.Adapter<EventsRecyclerAdapter.ViewHolder>()
{
    private val mInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = mInflater.inflate(R.layout.event_item_cell, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(DateEvents.eventList[position])
    }

    override fun getItemCount(): Int {
        return DateEvents.eventList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val dateEventTitle: TextView = itemView.findViewById(R.id.closest_event_title)
        private val dateEventText: TextView = itemView.findViewById(R.id.edit_event_description)
        private val dateEventDeleteBtn: Button = itemView.findViewById(R.id.delete_date_event_btn)
        private val dateEventEditBtn: Button = itemView.findViewById(R.id.edit_date_event_btn)

        fun bind(dateEvent: DateEvent) {
            dateEventEditBtn.setOnClickListener { editListener.onDateEventEdit(dateEvent) }
            dateEventDeleteBtn.setOnClickListener { deleteListener.onDateEventDelete(dateEvent) }

            dateEventText.text = dateEvent.description
            dateEventTitle.text = dateEvent.genTitle()
        }
    }
}

class Date(var day: Int, var month: Int, var year: Int) {
    fun genString() : String {
        return "${if (day >= 10) "$day" else "0$day"}/${if (month >= 10) "$month" else "0$month"}/$year"
    }
}

class DateEvent(var date: Date, var title: String, var description: String, var id: Int = 0) {
    fun genTitle() : String {
        return "$title,    ${date.genString()}"
    }
}

object DateEvents {
    val eventList = ArrayList<DateEvent>()

    fun updateIds() {
        eventList.forEachIndexed { idx, event -> event.id = idx }
    }

    fun sortEventsByDate() {
        eventList.sortWith(compareBy<DateEvent> { it.date.year }.thenBy { it.date.month }
            .thenBy { it.date.day })
    }

    fun sortEventsById() {
        eventList.sortBy { it.id }
    }

    fun getClosestEvent() : DateEvent? {
        return if (eventList.size > 0) eventList[0] else null
    }
}

interface OnDateEventDeleteListener {
    fun onDateEventDelete(dateEvent: DateEvent)
}

interface OnDateEventEditListener {
    fun onDateEventEdit(dateEvent: DateEvent)
}
