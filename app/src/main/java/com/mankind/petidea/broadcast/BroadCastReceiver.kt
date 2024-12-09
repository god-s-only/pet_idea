package com.mankind.petidea.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class BroadCastReceiver : BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        if(p1?.action != null && p1.action.equals(Intent.ACTION_AIRPLANE_MODE_CHANGED)){
            val state = p1.getBooleanExtra("state", false)
            if(state){
                Toast.makeText(p0, "Airplane mode is on", Toast.LENGTH_LONG).show()
            }
        }
    }
}