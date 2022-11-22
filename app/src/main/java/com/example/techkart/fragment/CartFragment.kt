package com.example.techkart.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.techkart.R
import com.example.techkart.adapter.CartAdapter
import com.example.techkart.databinding.FragmentCartBinding
import com.example.techkart.databinding.FragmentHomeBinding
import com.example.techkart.roomdb.AppDatabase


class CartFragment : Fragment() {

    private lateinit var binding: FragmentCartBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentCartBinding.inflate(layoutInflater)

        val preference = requireContext().getSharedPreferences("info", AppCompatActivity.MODE_PRIVATE)
        val editor = preference.edit()
        editor.putBoolean("isCart",false)
        editor.apply()

        val dao = AppDatabase.getInstance(requireContext()).productDao()

        dao.getAllProducts().observe(requireActivity()){
            binding.cartRecycler.adapter = CartAdapter(requireContext(),it)
        }
        return binding.root
    }

}