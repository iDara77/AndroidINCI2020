package com.obsoft.inci2019

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.obsoft.inci2019.models.CartStore
import com.obsoft.inci2019.models.Item

class ItemsAdapter(internal var dataSet: List<Item>) : RecyclerView.Adapter<ItemsAdapter.ItemViewHolder>() {
    class ItemViewHolder(val cellView:View) : RecyclerView.ViewHolder(cellView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val cellView = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_item, parent, false) as View
        val itemViewHolder = ItemViewHolder(cellView)
        cellView.findViewById<Button>(R.id.btn_removefromcart).setOnClickListener {
            val pos = itemViewHolder.adapterPosition
            val item = dataSet[pos]
            CartStore.addItem(item)
        }

        cellView.setOnClickListener {
            val intent = Intent(parent.context, MainActivity::class.java)
            val pos = itemViewHolder.adapterPosition
            val index = pos
            intent.putExtra("ITEM_ID",index)
            parent.context.startActivity(intent)
        }
        return itemViewHolder
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val titleView = holder.cellView.findViewById<TextView>(R.id.cartitem_title)
        val priceView = holder.cellView.findViewById<TextView>(R.id.cartitem_price)
        val index = position
        val item = dataSet[index]

        titleView.text = item.title
        priceView.text = item.price.toString()
    }

    override fun getItemCount(): Int = dataSet.size

    fun updateData(list:List<Item>) {
        dataSet = list
        notifyDataSetChanged()
    }
}
