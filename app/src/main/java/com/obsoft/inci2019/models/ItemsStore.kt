package com.obsoft.inci2019.models

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import org.json.JSONArray

object ItemsStore : RemoteServicesHandler {
    val ItemsLoadedAction = "com.obsoft.inci2019.itemsLoaded"

    private var list:List<Item> = listOf()
    override var context:Context? = null

    fun loadItems(context: Context? = null, callerId: Int=0) {
        this.context = context
        RemoteServices.get("https://5e8c8b85e61fbd00164aedcb.mockapi.io/api/v1/Product", this, callerId)
    }

    fun getList(context: Context? = null, callerId: Int=0) : List<Item> {
        if(list.isEmpty()) {
            this.loadItems(context, callerId)
        }
        return list
    }

    fun findByName(name: String) : List<Item> {
        return list.filter {
            it.title.contains(name, ignoreCase = true)
        }
    }

    fun findById(id: Int) : Item {
        return list.filter {
            it.id == id
        }[0]
    }

    override fun onFinishLoading(data: String, callerId: Int) {
        updateList(data)
        Intent().also {
            it.setAction(this.ItemsLoadedAction)
            it.putExtra("callerId", callerId)
            if(this.context != null)
                this.context!!.sendBroadcast(it)
        }


    }

    fun updateList(data: String) {
        val jsonArray = JSONArray(data)

        for (jsonIndex in 0..(jsonArray.length() - 1)) {
            val it = jsonArray.getJSONObject(jsonIndex)
            list += Item((it.get("id") as String).toInt(), it.get("name") as String, it.get("description") as String, (it.get("price") as String).toFloat(), it.get("image") as String)
        }
    }
}
