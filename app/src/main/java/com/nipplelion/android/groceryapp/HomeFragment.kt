package com.nipplelion.android.groceryapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HomeFragment: Fragment(R.layout.fragment_home) {

    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<FoodAdapter.ViewHolder>? = null
    private lateinit var rvItems: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvItems = view.findViewById(R.id.rvItems)

        layoutManager = LinearLayoutManager(requireContext())
        adapter = FoodAdapter()

        rvItems.layoutManager = layoutManager
        rvItems.adapter = adapter
    }

}