package com.example.app_totem

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.totem_hospital.R

class EspecialidadeAdapter(
    context: Context,
    private val items: List<Especialidade>
) : ArrayAdapter<Especialidade>(context, 0, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_especialidade, parent, false)

        val item = items[position]

        val nome = view.findViewById<TextView>(R.id.txt_nome)
        nome.text = item.nome

        return view
    }
}