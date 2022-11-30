package com.fmoreno.pokeapp.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

class Utils {
    fun isInternetAvailable(context: Context): Boolean {
        var isConnected: Boolean = false // Initial Value
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        if (activeNetwork != null && activeNetwork.isConnected)
            isConnected = true
        return isConnected
    }
}