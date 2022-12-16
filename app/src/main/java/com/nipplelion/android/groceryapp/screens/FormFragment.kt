package com.nipplelion.android.groceryapp.screens

import android.icu.lang.UCharacter.GraphemeClusterBreak.L
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.nipplelion.android.groceryapp.R
import com.nipplelion.android.groceryapp.models.Food
import com.nipplelion.android.groceryapp.models.FoodItem
import com.squareup.picasso.Picasso
import org.json.JSONObject
import org.w3c.dom.Text

private const val TAG: String = "FormFragment"

class FormFragment: Fragment(R.layout.fragment_form) {

    private var foodLabel: String = ""
    private var foodUPC: String = ""
    private var foodImage: String = ""
    private var foodId: String = ""


    private lateinit var etItemName: EditText
    private lateinit var etItemUPC: EditText
    private lateinit var ivItemImage: ImageView
    private lateinit var btnSubmit: Button
    private lateinit var tvStatus: TextView

    private lateinit var btnHave: Button
    private lateinit var btnRequest: Button
    private lateinit var btnGetting: Button

    var status: String = ""

    private val database = Firebase.database
    private val auth = Firebase.auth
    private val currentUser = auth.currentUser
    private val homesRef = database.getReference("homes/home1/log")
    private val usersRef = database.getReference("users/${currentUser?.uid}/home1/")



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var bundle = this.arguments
        if (bundle != null) {
            foodLabel = bundle["foodLabel"] as String
            foodUPC = bundle["foodUPC"] as String
            foodImage = bundle["foodImage"] as String
            foodId = bundle["foodId"] as String
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_form, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etItemName = view.findViewById(R.id.etItemName)
        etItemUPC = view.findViewById(R.id.etItemUPC)
        ivItemImage = view.findViewById(R.id.ivItemImage)
        btnSubmit = view.findViewById(R.id.btnSubmit)
        tvStatus = view.findViewById(R.id.tvStatus)

        btnHave = view.findViewById(R.id.btnHave)
        btnRequest = view.findViewById(R.id.btnRequest)
        btnGetting = view.findViewById(R.id.btnGetting)

        etItemName.setText(foodLabel)
        etItemUPC.setText(foodUPC)
        Picasso.get().load(foodImage).into(ivItemImage);

        btnHave.setOnClickListener {
            status = "Have"
            tvStatus.text = "Status: $status"
        }

        btnRequest.setOnClickListener {
            status = "Request"
            tvStatus.text = "Status: $status"
        }

        btnGetting.setOnClickListener {
            status = "Getting"
            tvStatus.text = "Status: $status"
        }

        btnSubmit.setOnClickListener {
            if (!etItemName.text.isNullOrEmpty() && !etItemUPC.text.isNullOrEmpty() && !status.isNullOrEmpty()) {
                var foodKey = homesRef.push().key

                var foodItem = FoodItem(
                    userId = currentUser?.uid,
                    date = System.currentTimeMillis(),
                    upc = foodUPC,
                    category = status,
                    foodId = foodId,
                    image = foodImage,
                    label = foodLabel
                )

                if (foodKey != null) {
                    homesRef.child(foodKey).setValue(foodItem)
                    usersRef.child(status).child(foodKey).setValue(foodItem)
                }
            }
        }

    }

}