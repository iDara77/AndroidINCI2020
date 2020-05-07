package com.obsoft.inci2019

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class OldFragmentAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    private  var fragments: MutableMap<Int, BaseFragment> = mutableMapOf()

    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        val fragment:Fragment
        if (position == 0) {
            fragment = ItemsGridFragment()
        } else if (position == 1) {
            fragment = ItemsGridFragment()
        } else if (position == 2) {
            fragment = ItemsGridFragment()
        } else if (position == 3) {
            fragment = ItemsGridFragment()
        }  else {
            throw Exception("Give me a Fragment")
        }

        val id = fragment.registerReceiver()
        fragments[id] = fragment
        return fragment
    }

    fun handleBroadcast(p0: Context?, p1: Intent?) {
        val callerId = p1?.getIntExtra("callerId", 0)
        if(callerId != 0) {
            fragments[callerId!!]?.handleBroadcast(p0, p1)
        }
    }
}