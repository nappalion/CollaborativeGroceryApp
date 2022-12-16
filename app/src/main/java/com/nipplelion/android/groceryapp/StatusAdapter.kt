package com.nipplelion.android.groceryapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class StatusAdapter: RecyclerView.Adapter<StatusAdapter.ViewHolder>() {

    private var foodLabels = arrayOf("Have", "Want", "Getting")
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<FoodAdapter.ViewHolder>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatusAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_status, parent, false)

        layoutManager = LinearLayoutManager(parent.context)
        adapter = FoodAdapter()

//        rvItems.layoutManager = layoutManager
//        rvItems.adapter = adapter

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: StatusAdapter.ViewHolder, position: Int) {
        holder.tvFoodLabel.text = foodLabels[position]
    }

    override fun getItemCount(): Int {
        return foodLabels.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var tvFoodLabel: TextView = itemView.findViewById(R.id.tvFoodLabel)
        var rvItems: RecyclerView = itemView.findViewById(R.id.rvItems)
    }
}