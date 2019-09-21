package com.gon.kineapp.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.gon.kineapp.model.Exercise
import com.gon.kineapp.utils.Notification

class AlarmReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

        context?.let {
            Notification.sendNotification(it,
                intent?.getStringExtra("TITLE")?:"¡Vamos!",
                intent?.getStringExtra("TITLE")?:"Hora de hacer ejercicios")
        }
    }
}