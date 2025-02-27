package com.example.lab1.fragment

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.lab1.databinding.FragmentIntentBinding

class IntentFragment : Fragment() {
    private lateinit var binding: FragmentIntentBinding

    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                shareToInstagramStory(requireContext(), it)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentIntentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonShare.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }
    }
}

fun shareToInstagramStory(context: Context, imageUri: Uri) {
    val intent = Intent("com.instagram.share.ADD_TO_STORY").apply {
        setDataAndType(imageUri, "image/*")
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }

    val packageManager = context.packageManager

    val instagramPackage = "com.instagram.android"
    val disePackage = "io.dise"

    val installedPackages = packageManager.getInstalledPackages(0).map { it.packageName }
    val targetPackage = when {
        instagramPackage in installedPackages -> instagramPackage
        disePackage in installedPackages -> disePackage
        else -> null
    }

    if (targetPackage != null) {
        intent.setPackage(targetPackage)
        context.startActivity(intent)
    } else {
        Toast.makeText(context, "Instagram (или The Dise) не установлен", Toast.LENGTH_SHORT).show()
    }
}

