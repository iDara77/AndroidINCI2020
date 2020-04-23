package com.obsoft.inci2019

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.obsoft.inci2019.models.CartStore
import com.obsoft.inci2019.models.ItemsStore


class CartActivity : AppCompatActivity() {
    private var recyclerView:RecyclerView? = null
    private var recyclerAdapter:CartItemsAdapter? = null
    private var itemsList = CartStore.getList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        setSupportActionBar(findViewById(R.id.main_toolbar))

        configureReceiver()
        recyclerView = findViewById<RecyclerView>(R.id.cartItemsGrid)
        var recyclerLayout = LinearLayoutManager(this)
        recyclerAdapter = CartItemsAdapter(itemsList)

        recyclerView!!.apply {
            layoutManager = recyclerLayout
            adapter = recyclerAdapter
        }


    }

    fun goToCheckout(view: View) {
        val intent : Intent = Intent(this, CheckoutActivity::class.java)
        startActivity(intent)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.appbar, menu)

        val searchItem = menu?.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as SearchView

        val ctl = this

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                filterItemsByTitle(newText)
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                Toast.makeText(ctl,"Text Submitted"+query, Toast.LENGTH_SHORT).show()
                return false
            }
        })


        return true
    }
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_search -> {
            true
        }

        R.id.action_cart -> {
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
            true
        }

        R.id.action_profile -> {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
            true
        }

        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }

    private fun filterItemsByTitle(title: String) {
        itemsList = CartStore.findItemsByName(title)
        recyclerAdapter!!.updateData(itemsList)
    }

    private fun configureReceiver() {
        val filter = IntentFilter()
        filter.addAction(CartStore.Action.ItemRemoved.actionName)
        val receiver = CartActivityBroadcastReceiver()
        registerReceiver(receiver, filter)
    }

    inner class CartActivityBroadcastReceiver: BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            if (p1!!.action == CartStore.Action.ItemRemoved.actionName ) {
                itemsList = CartStore.getList()
                recyclerAdapter!!.updateData(itemsList)
            }
        }
    }

}
