package com.nipplelion.android.groceryapp

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.fragment.app.replace

class MainActivity : AppCompatActivity() {

    private lateinit var cameraButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        cameraButton = findViewById(R.id.cameraButton)

        openCameraFragment(supportFragmentManager)

        cameraButton.setOnClickListener {
            openCameraFragment(supportFragmentManager)
        }
    }
}

fun openCameraFragment(supportFragmentManager: FragmentManager) {
    supportFragmentManager.commit {
        val barcodeFragment = BarcodeFragment()
        replace(R.id.fragmentContainerView, barcodeFragment)
        setReorderingAllowed(true)
        addToBackStack("camera")
    }
}