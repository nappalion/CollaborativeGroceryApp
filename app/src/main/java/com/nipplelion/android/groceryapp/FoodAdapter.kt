package com.nipplelion.android.groceryapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FoodAdapter: RecyclerView.Adapter<FoodAdapter.ViewHolder>() {

    private var foodLabels = arrayOf("Apple", "Water")
    private var foodImages = arrayOf("Yum", "Yum1")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_food, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: FoodAdapter.ViewHolder, position: Int) {
        holder.tvFoodLabel.text = foodLabels[position]
    }

    override fun getItemCount(): Int {
        return foodLabels.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var tvFoodLabel: TextView
        var ivFoodImage: ImageView

        init {
            tvFoodLabel = itemView.findViewById(R.id.tvFoodLabel)
            ivFoodImage = itemView.findViewById(R.id.ivFoodImage)
        }
    }
}