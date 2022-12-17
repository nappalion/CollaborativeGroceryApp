
package com.nipplelion.android.groceryapp

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.nipplelion.android.groceryapp.models.FoodItem
import com.squareup.picasso.Picasso

private const val TAG: String = "FoodAdapter"

open class FoodAdapter(var foodData: List<FoodItem>) :
    RecyclerView.Adapter<FoodAdapter.DataViewHolder>() {

    private var foodList: List<FoodItem> = ArrayList()

    init {
        this.foodList = foodData
    }

    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ivFood: ImageView = itemView.findViewById(R.id.ivFood)


        fun bind(food: FoodItem) {
            Log.i(TAG, food.label)
            Picasso.get().load(food.image).into(ivFood)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = DataViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.item_food, parent,
            false
        )
    )

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(foodList[position])
    }

    override fun getItemCount(): Int = foodList.size
}