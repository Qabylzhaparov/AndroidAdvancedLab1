package com.example.lab1.fragment

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.RECEIVER_EXPORTED
import androidx.core.content.ContextCompat.RECEIVER_NOT_EXPORTED
import androidx.core.content.ContextCompat.registerReceiver
import com.example.lab1.R
import com.example.lab1.databinding.FragmentBroadcastBinding

class BroadcastFragment : Fragment() {

    private lateinit var binding: FragmentBroadcastBinding
    private lateinit var airplaneModeReceiver: BroadcastReceiver

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBroadcastBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        registerAirplaneModeReceiver()
    }

    override fun onPause() {
        super.onPause()
        requireContext().unregisterReceiver(airplaneModeReceiver)
    }

    private fun registerAirplaneModeReceiver() {
        airplaneModeReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent?.action == Intent.ACTION_AIRPLANE_MODE_CHANGED) {
                    val isAirplaneModeOn = intent.getBooleanExtra("state", false)
                    Toast.makeText(requireContext(), if (isAirplaneModeOn) "Airplane Mode: ON" else "Airplane Mode: OFF", Toast.LENGTH_SHORT).show()
                }
            }
        }

        val filter = IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED)
        requireContext().registerReceiver(airplaneModeReceiver, filter)
    }
}
