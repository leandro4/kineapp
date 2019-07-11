package com.gon.kineapp.utils

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.Log

class LockerAppCallback(private val app: Application): Application.ActivityLifecycleCallbacks {

    private val MAX_TIME_SLEEP_LAP = 3 * 60 * 1000

    override fun onActivityPaused(activity: Activity?) {
        TimerSessionClient.registerPausedApp(app, getCurrentTimeMillis())
    }

    override fun onActivityResumed(activity: Activity?) {
        val inactiveLap = getCurrentTimeMillis() - TimerSessionClient.lastInactiveTime(app)
        val mustLock = inactiveLap > MAX_TIME_SLEEP_LAP || TimerSessionClient.unlockedApp(app)
        TimerSessionClient.setLockedApp(app, mustLock)
    }

    override fun onActivityStarted(activity: Activity?) {}

    override fun onActivityDestroyed(activity: Activity?) {}

    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {}

    override fun onActivityStopped(activity: Activity?) {}

    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {}

    private fun getCurrentTimeMillis(): Long {
        return System.currentTimeMillis()
    }

    object TimerSessionClient {

        private const val PREFIX = "timer_session"
        private const val INACTIVE = "session_inactive"
        private const val LOCKED = "session_locked"
        private const val UNLOCKED = "session_unlocked"

        fun registerPausedApp(context: Context, time: Long) {
            SharedPreferencesEditor(context, PREFIX).setValueForKey(INACTIVE, time)
        }

        fun lastInactiveTime(context: Context): Long {
            return SharedPreferencesEditor(context, PREFIX).valueForKey(INACTIVE, 0L)
        }

        fun lockedApp(context: Context): Boolean {
            return SharedPreferencesEditor(context, PREFIX).valueForKey(LOCKED, true)
        }

        fun setLockedApp(context: Context, locked: Boolean) {
            SharedPreferencesEditor(context, PREFIX).setValueForKey(LOCKED, locked)
        }

        fun unlockedApp(context: Context): Boolean {
            return SharedPreferencesEditor(context, PREFIX).valueForKey(UNLOCKED, true)
        }

        fun setUnlockedApp(context: Context, unlocked: Boolean) {
            SharedPreferencesEditor(context, PREFIX).setValueForKey(UNLOCKED, unlocked)
        }
    }
}