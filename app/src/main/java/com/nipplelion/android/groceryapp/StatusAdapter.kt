package com.nipplelion.android.groceryapp

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.nipplelion.android.groceryapp.models.FoodItem

//class StatusAdapter: RecyclerView.Adapter<StatusAdapter.ViewHolder>() {
//
//    private var foodLabels = arrayOf("Have", "Want", "Getting")
//    private var layoutManager: RecyclerView.LayoutManager? = null
//    private var adapter: RecyclerView.Adapter<FoodAdapter.ViewHolder>? = null
//
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatusAdapter.ViewHolder {
//        val view = LayoutInflater.from(parent.context)
//            .inflate(R.layout.item_status, parent, false)
//
//        layoutManager = LinearLayoutManager(parent.context)
//        adapter = FoodAdapter()
//
////        rvItems.layoutManager = layoutManager
////        rvItems.adapter = adapter
//
//        return ViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: StatusAdapter.ViewHolder, position: Int) {
//        holder.tvFoodLabel.text = foodLabels[position]
//    }
//
//    override fun getItemCount(): Int {
//        return foodLabels.size
//    }
//
//    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
//        var tvFoodLabel: TextView = itemView.findViewById(R.id.tvFoodLabel)
//        var rvItems: RecyclerView = itemView.findViewById(R.id.rvItems)
//    }
//}
private const val TAG: String = "StatusAdapter"

private val database = Firebase.database



open class StatusAdapter(var userId: String) :
    RecyclerView.Adapter<StatusAdapter.DataViewHolder>() {
    private val usersRef = database.getReference("users/${userId}/home1/")

    private var statuses = arrayOf("Have", "Want", "Getting")

    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var tvFoodLabel: TextView = itemView.findViewById(R.id.tvFoodLabel)
        private var rvItems: RecyclerView = itemView.findViewById(R.id.rvItems)

        fun bind(status: String) {
            var foodList: MutableList<FoodItem> = mutableListOf()

            tvFoodLabel.text = status

            rvItems.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL,false)
            var foodAdapter = FoodAdapter(foodList as List<FoodItem>)
            rvItems.adapter = foodAdapter

            val foodListener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (data in snapshot.children) {
                        var food = FoodItem(
                            category = data.child("category").value as String,
                            date = data.child("date").value as Long,
                            image = data.child("image").value as String,
                            label = data.child("label").value as String,
                            upc = data.child("upc").value as String,
                            userId = data.child("userId").value as String
                        )

                        Log.i(TAG, "Added $food to $status.")
                        foodList.add(food)
                    }
                    foodAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e(TAG, "loadPost:onCancelled", error.toException())
                }
            }

            usersRef.child(status).addValueEventListener(foodListener)

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = DataViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.item_status, parent,
            false
        )
    )

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(statuses[position])
    }

    override fun getItemCount(): Int = statuses.size
}