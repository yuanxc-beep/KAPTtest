package com.xicai.cfgtest

import android.os.Bundle
import android.os.PersistableBundle
import android.view.LayoutInflater
import androidx.activity.ComponentActivity
import com.xicai.cfgtest.databinding.ActivityMainBinding

class MainActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        val inflate = ActivityMainBinding.inflate(LayoutInflater.from(this))

    }
}