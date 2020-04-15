package com.obsoft.inci2019.models

import android.content.Context
import android.content.Intent
import org.json.JSONArray
import org.json.JSONObject


object CartStore : RemoteServicesHandler {
    private var list:List<CartItem> = listOf()
    override var context: Context? = null

    enum class Action(val actionName: String, val handlerId: Int) {
        ItemsLoaded("com.obsoft.inci2019.cart.itemLoaded", 1),
        ItemAdded("com.obsoft.inci2019.cart.itemAdded", 2),
        ItemRemoved("com.obsoft.inci2019.cart.itemRemoved", 3)
    }

    fun addItem(item:Item, context: Context? = null, callerId: Int=0) : Boolean {
        this.context = context
        val d = mapOf("productId" to item.id.toString(),"qty" to "1")
        RemoteServices.post("https://5e8c8b85e61fbd00164aedcb.mockapi.io/api/v1/Cart", d,this, callerId, CartStore.Action.ItemAdded.handlerId)
        return true
    }
    fun removeItem(index: Int) : Boolean {
        this.context = context
        list -= list[index]
        return true
    }

    fun getList(context: Context? = null, callerId: Int=0) : List<CartItem> {
        this.context = context
        return list
    }

    fun findItemsByName(title: String) : List<CartItem> {
        return list.filter {
            it.item.title.contains(title, ignoreCase = true)
        }
    }

    override fun onFinishLoading(data: String, callerId: Int, handlerId: Int) {
        val action:String
        if (CartStore.Action.ItemAdded.handlerId == handlerId) {
            list += parseItem(data)
            action = CartStore.Action.ItemAdded.actionName
        } else {
            action = ""
        }

        Intent().also {
            it.setAction(action)
            it.putExtra("callerId", callerId)
            if(this.context != null)
                this.context!!.sendBroadcast(it)
        }


    }

    fun parseItem(data: String) : CartItem {
        val jsonObject = JSONObject(data)
        val item = ItemsStore.findById(jsonObject.getInt("productId"))
        return CartItem(jsonObject.getInt("id"), item, jsonObject.getInt("qty"))
    }



}
