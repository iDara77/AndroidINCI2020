package com.obsoft.inci2019

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.obsoft.inci2019.models.CartItem
import com.obsoft.inci2019.models.CartStore
import com.obsoft.inci2019.models.Item

class CartItemsAdapter(internal var dataSet: List<CartItem>) : RecyclerView.Adapter<CartItemsAdapter.CartItemViewHolder>() {
    class CartItemViewHolder(val cellView: View) : RecyclerView.ViewHolder(cellView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartItemViewHolder {
        val cellView = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_cartitem, parent, false) as View
        val itemViewHolder = CartItemViewHolder(cellView)
        cellView.findViewById<Button>(R.id.btn_removefromcart).setOnClickListener {
            val pos = itemViewHolder.adapterPosition
            CartStore.removeItem(pos,parent.context)
            dataSet = CartStore.getList(parent.context)
            notifyDataSetChanged()
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

    override fun onBindViewHolder(holder: CartItemViewHolder, position: Int) {
        val titleView = holder.cellView.findViewById<TextView>(R.id.cartitem_title)
        val priceView = holder.cellView.findViewById<TextView>(R.id.cartitem_price)
        val qtyView = holder.cellView.findViewById<TextView>(R.id.cartitem_quantity)
        val index = position
        val item = dataSet[index]

        titleView.text = item.item.title
        priceView.text = item.item.price.toString()
        qtyView.text = item.qty.toString()
    }

    override fun getItemCount(): Int = dataSet.size

    fun updateData(list:List<CartItem>) {
        dataSet = list
        notifyDataSetChanged()
    }
}