package com.example.userstask

import android.app.Application
import com.example.userstask.data.network.AppContainer
import com.example.userstask.data.network.DefaultAppContainer

class UsersTaskApplication : Application() {

    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(applicationContext)
    }
}
