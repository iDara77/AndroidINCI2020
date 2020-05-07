package com.obsoft.inci2019

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.obsoft.inci2019.models.CartItem
import com.obsoft.inci2019.models.CartStore
import com.obsoft.inci2019.models.Item
import com.obsoft.inci2019.models.ItemsStore

class CartGridFragment: Fragment(), BaseFragment {
    private val CALLERID = 2
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerAdapter:CartItemsAdapter
    private lateinit var itemsList:List<CartItem>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.activity_cart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        itemsList = CartStore.getList(context)
        recyclerView = view.findViewById<RecyclerView>(R.id.cartItemsGrid)
        var recyclerLayout = LinearLayoutManager(context)
        recyclerAdapter = CartItemsAdapter(itemsList)

        recyclerView!!.apply {
            layoutManager = recyclerLayout
            adapter = recyclerAdapter
        }

        configureReceiver()

        view.findViewById<Button>(R.id.cart_checkout).setOnClickListener{
            goToCheckout(it)
        }

    }
    override public fun registerReceiver() : Int { return CALLERID }
    override public fun handleBroadcast(p0: Context?, p1: Intent?) {
        if (p1!!.action == CartStore.Action.ItemRemoved.actionName || p1!!.action == CartStore.Action.ItemUpdated.actionName) {
            itemsList = CartStore.getList()
            recyclerAdapter!!.updateData(itemsList)
        }
    }
    private fun configureReceiver() {
        val filter = IntentFilter()
        filter.addAction(CartStore.Action.ItemRemoved.actionName)
        filter.addAction(CartStore.Action.ItemUpdated.actionName)
        val receiver = CartFragmentBroadcastReceiver()
        context?.let { LocalBroadcastManager.getInstance(it).registerReceiver(receiver, filter) }
//        registerReceiver(receiver, filter)
    }

    inner class CartFragmentBroadcastReceiver: BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            handleBroadcast(p0, p1)
        }
    }

    fun goToCheckout(view: View) {
        val intent : Intent = Intent(context, CheckoutActivity::class.java)
        startActivity(intent)
    }
}