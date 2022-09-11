package com.nipplelion.android.groceryapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView

class FormFragment: Fragment(R.layout.fragment_form) {

    private var foodLabel: String = ""
    private var foodUPC: String = ""
    private var foodImage: String = ""


    private lateinit var etItemName: EditText
    private lateinit var etItemUPC: EditText
    private lateinit var ivItemImage: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var bundle = this.arguments
        if (bundle != null) {
            foodLabel = bundle["foodLabel"] as String
            foodUPC = bundle["foodUPC"] as String
            foodImage = bundle["foodUPC"] as String
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

        etItemName.setText(foodLabel)
        etItemUPC.setText(foodUPC)
    }

}