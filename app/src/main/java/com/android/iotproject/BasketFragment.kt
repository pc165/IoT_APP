package com.android.iotproject

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.iotproject.adapter.ProductData
import com.android.iotproject.adapter.ProductListAdapter
import com.android.iotproject.databinding.FragmentBasketBinding

class BasketFragment : Fragment() {
    private lateinit var binding: FragmentBasketBinding
    private lateinit var productListAdapter: ProductListAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentBasketBinding.inflate(inflater, container, false)
        productListAdapter = ProductListAdapter(mutableListOf())
        binding.rvProductList.adapter = productListAdapter
        binding.rvProductList.layoutManager = LinearLayoutManager(activity)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    fun addProduct(productData: ProductData) {
        productListAdapter.addProduct(productData)
        binding.tvTotal.text = "Total ${productListAdapter.price} €"

    }
}