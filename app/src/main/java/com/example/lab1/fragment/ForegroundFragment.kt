package com.example.lab1.fragment

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.lab1.R
import com.example.lab1.service.ForegroundService

class ForegroundFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_foreground, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requestPermissions()

        view.findViewById<ImageButton>(R.id.btnPlay).setOnClickListener {
            requireContext().startService(Intent(requireContext(), ForegroundService::class.java).apply {
                action = "PLAY"
            })
        }

        view.findViewById<ImageButton>(R.id.btnPause).setOnClickListener {
            requireContext().startService(Intent(requireContext(), ForegroundService::class.java).apply {
                action = "PAUSE"
            })
        }

        view.findViewById<ImageButton>(R.id.btnStop).setOnClickListener {
            requireContext().stopService(Intent(requireContext(), ForegroundService::class.java))
        }
    }

    private fun requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13+
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (!isGranted) {
            }
        }
}
