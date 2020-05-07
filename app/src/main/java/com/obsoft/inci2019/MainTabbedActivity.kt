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
import com.google.android.material.badge.BadgeDrawable
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

        pagerAdapter = FragmentAdapter(this)
        viewPager = findViewById(R.id.pager)
        viewPager.adapter = pagerAdapter
        tabLayout = findViewById<TabLayout>(R.id.tab_layout)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = FragmentAdapter.TabTitles.values()[position].label
            tab.icon = getDrawable(FragmentAdapter.TabTitles.values()[position].drawable)
        }.attach()

        configureReceiver()
        CartStore.loadItems(this)
    }

    fun updateCartBadge() {

        var badge = tabLayout.getTabAt(1)?.orCreateBadge
        badge?.number = CartStore.itemsCount
    }

    private fun configureReceiver() {
        val filter = IntentFilter()
        filter.addAction(CartStore.Action.ItemsLoaded.actionName)
        filter.addAction(CartStore.Action.ItemRemoved.actionName)
        filter.addAction(CartStore.Action.ItemAdded.actionName)
        val receiver = ItemsGridBroadcastReceiver()
        registerReceiver(receiver, filter)
    }

    inner class ItemsGridBroadcastReceiver: BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            if (p1!!.action == CartStore.Action.ItemsLoaded.actionName ||
                p1!!.action == CartStore.Action.ItemRemoved.actionName ||
                p1!!.action == CartStore.Action.ItemAdded.actionName) {
                updateCartBadge()
            }
        }
    }


}
