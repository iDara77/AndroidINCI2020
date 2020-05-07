package com.obsoft.inci2019.models

import android.content.Context
import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import org.json.JSONArray
import org.json.JSONObject


object CartStore : RemoteServicesHandler {
    private var list:List<CartItem> = listOf()
    override var context: Context? = null
    public var itemsCount : Int = 0
        get() = list.count()

    enum class Action(val actionName: String, val handlerId: Int) {
        ItemsLoaded("com.obsoft.inci2019.cart.itemLoaded", 1),
        ItemAdded("com.obsoft.inci2019.cart.itemAdded", 2),
        ItemRemoved("com.obsoft.inci2019.cart.itemRemoved", 3),
        ItemUpdated("com.obsoft.inci2019.cart.itemUpdated", 4)
    }

    fun addItem(item:Item, context: Context? = null, callerId: Int=0) : Boolean {
        this.context = context
        val d = mapOf("productId" to item.id.toString(),"qty" to "1")
        RemoteServices.post("https://5e8c8b85e61fbd00164aedcb.mockapi.io/api/v1/Cart", d,this, callerId, CartStore.Action.ItemAdded.handlerId)
        return true
    }
    fun removeItem(itemIndex:Int, context: Context? = null, callerId: Int=0) : Boolean {
        val item = list[itemIndex]
        this.context = context
        RemoteServices.delete("https://5e8c8b85e61fbd00164aedcb.mockapi.io/api/v1/Cart/"+item.id, null,this, callerId, CartStore.Action.ItemRemoved.handlerId)
        return true
    }
    fun updateItemQty(itemIndex: Int, qty: Int, context: Context? = null, callerId: Int=0) : Boolean {
        val item = list[itemIndex]
        this.context = context
        val d = mapOf("qty" to (item.qty+qty).toString())
        RemoteServices.put("https://5e8c8b85e61fbd00164aedcb.mockapi.io/api/v1/Cart/"+item.id, d,this, callerId, CartStore.Action.ItemUpdated.handlerId)
        return true
    }
    fun loadItems(context: Context? = null, callerId: Int=0) {
        this.context = context
        RemoteServices.get("https://5e8c8b85e61fbd00164aedcb.mockapi.io/api/v1/Cart", this, callerId,
            ItemsStore.ItemsLoadedHandlerId
        )

    }

    fun getList(context: Context? = null, callerId: Int=0) : List<CartItem> {
        this.context = context
        if(list.isEmpty()) {
            this.loadItems(context, callerId)
        }
        return list
    }

    fun findItemById(id: Int) : CartItem {
        return list.filter {
            it.id == id
        }[0]
    }
    fun findItemsByName(title: String) : List<CartItem> {
        return list.filter {
            it.getItem().title.contains(title, ignoreCase = true)
        }
    }

    override fun onFinishLoading(data: String, callerId: Int, handlerId: Int) {
        val action:String
        if (CartStore.Action.ItemsLoaded.handlerId == handlerId) {
            updateList(data)
            action = CartStore.Action.ItemsLoaded.actionName
        } else if (CartStore.Action.ItemAdded.handlerId == handlerId) {
            list += parseItem(data)
            action = CartStore.Action.ItemAdded.actionName
        } else if (CartStore.Action.ItemRemoved.handlerId == handlerId) {
            list -= parseItem(data)
            action = CartStore.Action.ItemRemoved.actionName
        } else if (CartStore.Action.ItemUpdated.handlerId == handlerId) {
            val item = parseItem(data)
            val cItem = findItemById(item.id)
            cItem.qty = item.qty
            action = CartStore.Action.ItemUpdated.actionName
        } else {
            action = ""
        }
        Intent().also {
            it.setAction(action)
            it.putExtra("callerId", callerId)
            if(this.context != null) {
                this.context!!.sendBroadcast(it)
                LocalBroadcastManager.getInstance(this.context!!).sendBroadcast(it)
            }
        }


    }

    fun parseItem(data: String) : CartItem {
        val jsonObject = JSONObject(data)
        val itemId = jsonObject.getInt("productId")
        return CartItem(jsonObject.getInt("id"), itemId, jsonObject.getInt("qty"))
    }


    fun updateList(data: String) {
        val jsonArray = JSONArray(data)

        for (jsonIndex in 0..(jsonArray.length() - 1)) {
            val it = jsonArray.getJSONObject(jsonIndex)
            val itemId = it.getInt("productId")
            list += CartItem((it.get("id") as String).toInt(), itemId, it.getInt("qty"))
        }
    }

}
