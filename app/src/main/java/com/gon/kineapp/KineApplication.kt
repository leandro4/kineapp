package com.gon.kineapp

import android.app.Application
import com.gon.kineapp.utils.LockerAppCallback

class KineApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        registerActivityLifecycleCallbacks(LockerAppCallback(this))
    }
}