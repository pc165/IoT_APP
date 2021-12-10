package com.android.iotproject.adapter

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.iotproject.R
import com.android.iotproject.databinding.ProductItemBinding
import org.json.JSONArray
import org.json.JSONObject

data class ProductData(
    val name: String = "Unknown Product",
    val id: Int = 0,
    val price: Float = 0.0f,
    val quantitaty: Int = 0,
    val icon: Bitmap = Bitmap.createBitmap(50, 50, Bitmap.Config.RGB_565)
)

class ProductListAdapter(private val products: MutableList<ProductData>) :
    RecyclerView.Adapter<ProductListAdapter.ProdudctListViewHolder>() {
    var price = 0.0f

    class ProdudctListViewHolder(view: View) : RecyclerView.ViewHolder(view)

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
        price += productData.price * productData.quantitaty
        notifyItemInserted(products.size - 1)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ProdudctListViewHolder, position: Int) {
        val product = products[position]
        holder.itemView.apply {
            ProductItemBinding.bind(this).apply {
                tvProductName.text = product.name
                tvQuantityPrice.text = "${product.price} * ${product.quantitaty}"
                tvTotal.text = (product.quantitaty * product.price).toString()
                iconProduct.setImageBitmap(product.icon)
            }
        }
    }

    override fun getItemCount(): Int {
        return products.size
    }

    fun getAsJSON(): JSONObject {
        val item = JSONObject()
        val orderDetails = JSONArray()
        for (i in products) {
            item.put("item_id", i.id)
            item.put("quantity", i.quantitaty)
            orderDetails.put(item)
        }
        val order = JSONObject()
        order.put("order_details", orderDetails)
        return order
    }
}