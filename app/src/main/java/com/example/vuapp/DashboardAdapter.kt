package com.example.vuapp.viewmodel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vuapp.R


class DashboardAdapter(
    private var items: List<Map<String, Any>>,
    private val onItemClick: (Map<String, Any>) -> Unit
) : RecyclerView.Adapter<DashboardAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleText: TextView = itemView.findViewById(R.id.property1Text)
        val subtitleText: TextView = itemView.findViewById(R.id.property2Text)

        init {
            itemView.setOnClickListener {
                onItemClick(items[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_dashboard, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        // Dynamically pick any 2 non-description fields
        val keys = item.keys.filterNot { it == "description" }
        val key1 = keys.getOrNull(0)
        val key2 = keys.getOrNull(1)

        holder.titleText.text = key1?.let { item[it].toString() } ?: "N/A"
        holder.subtitleText.text = key2?.let { item[it].toString() } ?: ""
    }

    override fun getItemCount(): Int = items.size

    fun updateData(newItems: List<Map<String, Any>>) {
        items = newItems
        notifyDataSetChanged()
    }
}
