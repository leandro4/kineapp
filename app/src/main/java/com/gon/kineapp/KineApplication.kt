package com.gon.kineapp

import android.app.Application
import com.gon.kineapp.utils.LockerAppCallback
import com.gon.kineapp.utils.Notification

class KineApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        registerActivityLifecycleCallbacks(LockerAppCallback(this))
        Notification.createNotificationChannel(this)
    }
}