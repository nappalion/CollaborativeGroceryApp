package com.nipplelion.android.groceryapp.screens

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.nipplelion.android.groceryapp.R

class MainActivity : AppCompatActivity() {

    private lateinit var fabCamera: FloatingActionButton
    private lateinit var bottomNavigationView: BottomNavigationView
    private val auth = Firebase.auth
    private val currentUser = auth.currentUser
    private val userId = currentUser!!.uid

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
                R.id.home -> {
                    var bundle = Bundle()
                    bundle.putString("uid", userId)
                    homeFragment.arguments = bundle
                    setCurrentFragment(homeFragment)
                }
                R.id.groups -> {
                    FirebaseAuth.getInstance().signOut()
                    var intent: Intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
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
