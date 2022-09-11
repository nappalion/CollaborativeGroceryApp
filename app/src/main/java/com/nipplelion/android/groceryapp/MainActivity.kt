package com.nipplelion.android.groceryapp

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var fabCamera: FloatingActionButton
    private lateinit var bottomNavigationView: BottomNavigationView

    private var tempData: List<String> = listOf("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        val barcodeFragment = BarcodeFragment()
        val homeFragment = HomeFragment()

        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        fabCamera = findViewById(R.id.fabCamera)

        setCurrentFragment(barcodeFragment)

        fabCamera.setOnClickListener {
            setCurrentFragment(barcodeFragment)
        }

        bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.home -> setCurrentFragment(homeFragment)
            }
            true
        }


    }

    private fun setCurrentFragment(fragment: Fragment) {
        supportFragmentManager.commit {
            replace(R.id.fragmentContainerView, fragment)
            setReorderingAllowed(true)
            addToBackStack("")
        }
    }
}
