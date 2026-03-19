package com.example.app_totem

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kotlin.math.roundToInt

class ProductAdapter(
    context: Context,
    private val products: List<Product>
) : ArrayAdapter<Product>(context, 0, products) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_product, parent, false)

        val product = products[position]

        val name = view.findViewById<TextView>(R.id.txt_name)
        val price = view.findViewById<TextView>(R.id.txt_price)

        name.text = product.name
        price.text = "R$ %.2f".format(product.price)

        return view
    }
}