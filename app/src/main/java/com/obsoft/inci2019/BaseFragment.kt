package com.obsoft.inci2019

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment

interface BaseFragment {
    public fun registerReceiver() : Int
    public fun handleBroadcast(p0: Context?, p1: Intent?)
}