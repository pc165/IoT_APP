package com.android.iotproject.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.iotproject.R

data class ProductData(
    val name: String,
    val id: Int,
    val price: Float,
    val quantitaty: Int
)

class ProductListAdapter(private val products: MutableList<ProductData>) :
    RecyclerView.Adapter<ProductListAdapter.ProdudctListViewHolder>() {
    class ProdudctListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProdudctListViewHolder {
        return ProdudctListViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.product_item,
                parent,
                false
            )
        )
    }

    fun addProduct(productData: ProductData) {
        products.add(productData)
        notifyItemInserted(products.size - 1)
    }


    override fun onBindViewHolder(holder: ProdudctListViewHolder, position: Int) {
        val curTodo = products[position]
//        holder.
    }

    override fun getItemCount(): Int {
        return products.size
    }

}