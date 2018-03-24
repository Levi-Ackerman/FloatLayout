package com.zxlee.floatlayout

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.zxlee.layout.FloatLayout
import java.util.*

class MainActivity : AppCompatActivity() {
    var mFloatLayout: FloatLayout? = null
    val mRandom = Random()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mFloatLayout = findViewById(R.id.float_layout)
        findViewById<Button>(R.id.add_btn).setOnClickListener {
            run {
                val btn = getButton(mRandom.nextInt(4))
                mFloatLayout!!.addView(btn!!)
            }
        }
    }

    private fun getButton(index: Int):Button? {
        val buttonLayout = LayoutInflater.from(this).inflate(R.layout.buttons, null)
        val btn:Button? =  when(index){
            0-> buttonLayout!!.findViewById(R.id.btn_50)
            1-> buttonLayout!!.findViewById(R.id.btn_100)
            2-> buttonLayout!!.findViewById(R.id.btn_150)
            3-> buttonLayout!!.findViewById(R.id.btn_200)
            else-> null
        }
        (buttonLayout as ViewGroup).removeView(btn!!)
        return btn
    }
}
