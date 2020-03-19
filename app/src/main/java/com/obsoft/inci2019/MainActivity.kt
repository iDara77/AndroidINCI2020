package com.obsoft.inci2019

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextClock
import android.widget.TextView
import android.widget.Toast
import com.obsoft.inci2019.models.CartStore
import com.obsoft.inci2019.models.Item
import com.obsoft.inci2019.models.ItemsStore

class MainActivity : AppCompatActivity() {

    var currentItem:Item? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.main_toolbar))

        val index = intent.getIntExtra("ITEM_ID",0)
        currentItem = ItemsStore.getList()[index]

    }

    fun goToAct2(view : View) {
        Toast.makeText(this,"Been Clicked", Toast.LENGTH_LONG).show()
    }

    fun addToCart(view: View) {
        val msg:String;
        if(CartStore.addItem(currentItem!!)) {
            msg = "Item added to cart"
        } else {
            msg = "WARNING: Item was NOT added to cart"
        }
        Toast.makeText(this, msg, Toast.LENGTH_LONG)
    }

}
