package com.obsoft.inci2019

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.obsoft.inci2019.models.CartStore
import com.obsoft.inci2019.models.ItemsStore

import kotlinx.android.synthetic.main.activity_fragment.*

class MainTabbedActivity : AppCompatActivity() {
    private lateinit var viewPager : ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var pagerAdapter: FragmentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment)
        setSupportActionBar(findViewById(R.id.main_toolbar))

        configureReceiver()
        pagerAdapter = FragmentAdapter(this)
        viewPager = findViewById(R.id.pager)
        viewPager.adapter = pagerAdapter
        tabLayout = findViewById<TabLayout>(R.id.tab_layout)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = "OBJECT ${(position + 1)}"
        }.attach()
    }

    private fun configureReceiver() {
        val filter = IntentFilter()
        filter.addAction(ItemsStore.ItemsLoadedAction)
        filter.addAction(CartStore.Action.ItemAdded.actionName)
        val receiver = TabbedActivityBroadcastReceiver()
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, filter)
//        registerReceiver(receiver, filter)
    }

    inner class TabbedActivityBroadcastReceiver: BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            if (p1!!.action == ItemsStore.ItemsLoadedAction ) {
                pagerAdapter.notifyDataSetChanged()
            } else if (p1!!.action == CartStore.Action.ItemAdded.actionName) {
                Toast.makeText(p0, "Item Added", Toast.LENGTH_LONG).show()
            }
        }
    }
}
