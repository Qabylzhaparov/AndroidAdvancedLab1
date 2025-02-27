package com.example.lab1.fragment

import android.content.ContentResolver
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.provider.CalendarContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lab1.R
import com.example.lab1.adapter.CalendarEvent
import com.example.lab1.adapter.CalendarEventAdapter
import java.text.SimpleDateFormat
import java.util.*

class ContentFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CalendarEventAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_content, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val permissions = arrayOf(android.Manifest.permission.READ_CALENDAR)
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.READ_CALENDAR
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                permissions,
                REQUEST_CALENDAR_PERMISSION
            )
        } else {
            loadCalendarEvents()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CALENDAR_PERMISSION && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            loadCalendarEvents()
        } else {
            Log.e("CalendarProvider", "Permission denied")
        }
    }

    private fun loadCalendarEvents() {
        val eventList = mutableListOf<CalendarEvent>()
        val contentResolver: ContentResolver? = context?.contentResolver
        val uri = CalendarContract.Events.CONTENT_URI
        val projection = arrayOf(
            CalendarContract.Events.TITLE,
            CalendarContract.Events.DTSTART
        )

        val cursor: Cursor? = contentResolver?.query(uri, projection, null, null, null)

        cursor?.use {
            val titleIndex = it.getColumnIndex(CalendarContract.Events.TITLE)
            val dtStartIndex = it.getColumnIndex(CalendarContract.Events.DTSTART)

            val dateFormat = SimpleDateFormat("d MMMM", Locale("ru"))

            while (it.moveToNext()) {
                val title = it.getString(titleIndex)
                val dtStartMillis = it.getLong(dtStartIndex)

                val dtStart = dateFormat.format(Date(dtStartMillis))

                eventList.add(CalendarEvent(title, dtStart))
            }
        }

        adapter = CalendarEventAdapter(eventList)
        recyclerView.adapter = adapter

        Log.d("CalendarProvider", eventList.joinToString("\n"))
    }

    companion object {
        private const val REQUEST_CALENDAR_PERMISSION = 1
    }
}
