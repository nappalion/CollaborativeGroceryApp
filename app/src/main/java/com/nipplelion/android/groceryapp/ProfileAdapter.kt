package com.nipplelion.android.groceryapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nipplelion.android.groceryapp.models.Profile
import com.squareup.picasso.Picasso

private const val TAG: String = "ProfileAdapter"

open class ProfileAdapter(var profileData: List<Profile>) :
    RecyclerView.Adapter<ProfileAdapter.DataViewHolder>() {

    private var profileList: List<Profile> = ArrayList()

    init {
        this.profileList = profileData
    }

    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ivProfilePic: ImageView = itemView.findViewById(R.id.ivProfilePic)
        var tvUsername: TextView = itemView.findViewById(R.id.tvUsername)


        fun bind(profile: Profile) {
            tvUsername.text = profile.username
            if (profile.image.isNotBlank()) {
                Picasso.get().load(profile.image).into(ivProfilePic)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = DataViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.item_profile, parent,
            false
        )
    )

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(profileList[position])
    }

    override fun getItemCount(): Int = profileList.size
}