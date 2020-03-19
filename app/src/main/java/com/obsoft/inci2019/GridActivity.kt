package com.obsoft.inci2019

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.obsoft.inci2019.models.ItemsStore

class GridActivity : AppCompatActivity() {
    private var recyclerView:RecyclerView? = null
    private var recyclerAdapter:ItemsAdapter? = null
    private var itemsList = ItemsStore.getList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grid)
        setSupportActionBar(findViewById(R.id.main_toolbar))

        recyclerView = findViewById<RecyclerView>(R.id.cartItemsGrid)
        var recyclerLayout = GridLayoutManager(this, 2)
        recyclerAdapter = ItemsAdapter(itemsList)

        recyclerView!!.apply {
            layoutManager = recyclerLayout
            adapter = recyclerAdapter
        }
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

        R.id.action_favorite -> {
            val intent = Intent(this, CartActivity::class.java)
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
        itemsList = ItemsStore.findByName(title)
        recyclerAdapter!!.updateData(itemsList)
    }

}
