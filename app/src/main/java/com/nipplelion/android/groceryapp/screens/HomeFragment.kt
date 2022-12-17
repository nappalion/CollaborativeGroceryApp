package com.nipplelion.android.groceryapp.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.nipplelion.android.groceryapp.R
import com.nipplelion.android.groceryapp.StatusAdapter

class HomeFragment: Fragment(R.layout.fragment_home) {

    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<StatusAdapter.DataViewHolder>? = null
    private lateinit var rvItems: RecyclerView
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userId = arguments?.getString("uid").toString()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvItems = view.findViewById(R.id.rvItems)

        layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        adapter = StatusAdapter(userId)

        rvItems.layoutManager = layoutManager
        rvItems.adapter = adapter
    }
}