package com.sanjana.bookapp.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

class ConnectionManager {

    //context is passing as a parameter here
    infix fun checkConnectivity(context: Context): Boolean {

        //This line will give us the information about currently active network
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        //the network present in the device is active or not or the hadware of the active network is working fine
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo

        if (activeNetwork?.isConnected != null){
            return activeNetwork.isConnected
        } else {
            return false
        }
    }
}