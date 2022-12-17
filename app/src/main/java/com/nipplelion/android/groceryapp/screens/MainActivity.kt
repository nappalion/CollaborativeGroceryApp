package com.nipplelion.android.groceryapp.screens

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.nipplelion.android.groceryapp.ProfileAdapter
import com.nipplelion.android.groceryapp.R
import com.nipplelion.android.groceryapp.StatusAdapter
import com.nipplelion.android.groceryapp.models.FoodItem
import com.nipplelion.android.groceryapp.models.Profile

private const val TAG: String = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var fabCamera: FloatingActionButton
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var rvProfiles: RecyclerView
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var profileAdapter: RecyclerView.Adapter<ProfileAdapter.DataViewHolder>? = null

    private val auth = Firebase.auth
    private val currentUser = auth.currentUser
    private val userId = currentUser!!.uid

    private val database = Firebase.database

    private val homesRef = database.getReference("homes/home1/users")
    private val profilesRef = database.getReference("profiles/")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        // Recycler View for Profiles
        rvProfiles = findViewById(R.id.rvProfiles)
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        var profileList: MutableList<Profile> = mutableListOf()
        profileAdapter = ProfileAdapter(profileList as List<Profile>)
        rvProfiles.layoutManager = layoutManager
        rvProfiles.adapter = profileAdapter

        val profileListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (data in snapshot.children) {
                    profilesRef.child(data.toString()).get().addOnSuccessListener {
                        var profile = Profile(
                            username = it.child("username").value as String,
                            image = it.child("image").value as String,
                            accountCreated = it.child("accountCreated").value as Long,
                            email = it.child("email").value as String
                        )

                        Log.i(TAG, "Added $profile.")
                        profileList.add(profile)
                    }
                }

                (profileAdapter as ProfileAdapter).notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(TAG, "loadPost:onCancelled", error.toException())
            }
        }

        homesRef.addValueEventListener(profileListener)

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
