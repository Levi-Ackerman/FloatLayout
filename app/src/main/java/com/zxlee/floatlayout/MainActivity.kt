package com.zxlee.floatlayout

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.zxlee.layout.FloatLayout

class MainActivity : AppCompatActivity() {
    var mFloatLayout: FloatLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mFloatLayout = findViewById(R.id.float_layout)

    }
}
