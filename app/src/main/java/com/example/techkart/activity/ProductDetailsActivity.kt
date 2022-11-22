package com.example.techkart.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.techkart.MainActivity
import com.example.techkart.R
import com.example.techkart.databinding.ActivityProductDetailsBinding
import com.example.techkart.roomdb.AppDatabase
import com.example.techkart.roomdb.ProductDao
import com.example.techkart.roomdb.ProductModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
            val name = it.getString("productName")
            val productSp = it.getString("productSp")
            val productDesc = it.getString("productDescription")
            binding.textView7.text = it.getString("productName")
            binding.textView8.text = it.getString("productSp")
            binding.textView9.text = it.getString("productDescription")

            val slideList = ArrayList<SlideModel>()
            for (data in list){
                slideList.add(SlideModel(data,ScaleTypes.FIT))
            }

            cartAction(proID,name,productSp,it.getString("productCoverImg"))

            binding.imageSlider.setImageList(slideList)
        }.addOnFailureListener {
            Toast.makeText(this,"Something went Wrong",Toast.LENGTH_SHORT)
        }

    }

    private fun cartAction(proID: String, name: String?, productSp: String?, coverImg: String?) {

        val productDao = AppDatabase.getInstance(this).productDao()

        if (productDao.isExit(proID)!=null){
            binding.textView10.text = "Go To Cart"
        }else{
            binding.textView10.text = "Add To Cart"
        }

        binding.textView10.setOnClickListener {
            if (productDao.isExit(proID)!=null){
                openCart()
            }else{
                addToCart(productDao,proID,name,productSp,coverImg)
            }
        }

    }

    private fun addToCart(productDao: ProductDao, proID: String, name: String?, productSp: String?, coverImg: String?) {

        val data = ProductModel(proID,name,coverImg,productSp)
        lifecycleScope.launch(Dispatchers.IO){
            productDao.insertProduct(data)
            binding.textView10.text = "Go To Cart"
        }
    }

    private fun openCart() {

        val preference = this.getSharedPreferences("info", MODE_PRIVATE)
        val editor = preference.edit()
        editor.putBoolean("isCart",true)
        editor.apply()

        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}