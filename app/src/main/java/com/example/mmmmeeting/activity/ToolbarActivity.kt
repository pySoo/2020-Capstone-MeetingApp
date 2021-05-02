package com.example.mmmmeeting.activity

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.mmmmeeting.R

//툴바 UI를 제공하는 Activity
open class ToolbarActivity constructor() : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
    }

    public override fun setContentView(@LayoutRes layoutResID: Int) {
        super.setContentView(layoutResID)
        val myToolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(myToolbar)
    }

    fun setToolbarTitle(title: String?) {
        val actionBar: ActionBar? = getSupportActionBar()
        if (actionBar != null) {
            actionBar.setTitle(title)
        }
    }
}