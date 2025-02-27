package com.example.lab1.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lab1.R

class CalendarEventAdapter(private val eventList: List<CalendarEvent>) :
    RecyclerView.Adapter<CalendarEventAdapter.EventViewHolder>() {

        class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val titleTextView: TextView = itemView.findViewById(R.id.event_title)
            val dateTextView: TextView = itemView.findViewById(R.id.event_date)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_event, parent, false)
        return EventViewHolder(view)
    }

    override fun getItemCount(): Int = eventList.size

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event: CalendarEvent = eventList[position]
        holder.titleTextView.text = event.title
        holder.dateTextView.text = event.startDate
    }


}