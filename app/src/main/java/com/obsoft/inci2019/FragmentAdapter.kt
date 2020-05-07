package com.obsoft.inci2019

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class FragmentAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    enum class TabTitles(val label: String, val drawable: Int) {
        SHOP("Shop", R.drawable.ic_explore_black),
        CART("Cart", R.drawable.ic_shopping_cart_black)
    }
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        val fragment:Fragment
        if (position == 0) {
            fragment = ItemsGridFragment()
        } else if (position == 1) {
            fragment = CartGridFragment()
        }  else {
            throw Exception("Give me a Fragment")
        }

        return fragment
    }

}