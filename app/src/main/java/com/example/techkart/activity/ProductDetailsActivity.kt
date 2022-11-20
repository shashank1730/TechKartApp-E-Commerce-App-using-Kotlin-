package com.example.techkart.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.techkart.R
import com.example.techkart.databinding.ActivityProductDetailsBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ProductDetailsActivity : AppCompatActivity() {

    private lateinit var binding : ActivityProductDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailsBinding.inflate(layoutInflater)

        getProductDetails(intent.getStringExtra("id"))
        setContentView(binding.root)


    }

    private fun getProductDetails(proID: String?) {

        Firebase.firestore.collection("products").document(proID!!).get().addOnSuccessListener {


            val list = it.get("productImages") as ArrayList<String>
            binding.textView7.text = it.getString("productName")
            binding.textView8.text = it.getString("productSp ")
            binding.textView9.text = it.getString("productDescription")

            val slideList = ArrayList<SlideModel>()
            for (data in list){
                slideList.add(SlideModel(data,ScaleTypes.FIT))
            }

            binding.imageSlider.setImageList(slideList)
        }.addOnFailureListener {
            Toast.makeText(this,"Something went Wrong",Toast.LENGTH_SHORT)
        }

    }
}