package com.example.mmmmeeting.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.example.mmmmeeting.R
import com.google.firebase.auth.FirebaseAuth

class IntroActivity constructor() : Activity() {
    private var mAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.intro_layout)
        mAuth = FirebaseAuth.getInstance()
        val handler: Handler = Handler()
        handler.postDelayed(object : Runnable {
            public override fun run() {
                if (mAuth!!.getCurrentUser() != null) {
                    val intent: Intent = Intent(getApplication(), MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    val intent: Intent = Intent(getApplication(), SignActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }, 1000) //2초 뒤에 Runner객체 실행하도록 함
    }

    override fun onPause() {
        super.onPause()
        finish()
    }
}