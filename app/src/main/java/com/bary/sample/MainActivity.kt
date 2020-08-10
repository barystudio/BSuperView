package com.bary.sample

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AppUtils.setStatusBarColor(
            window,
            ContextCompat.getColor(this, R.color.colorPrimary),
            false
        )
        logo.setColorFilter(ContextCompat.getColor(this,R.color.white))

    }


    fun showBEditText(v: View) {
        startActivity(Intent(this,BEditTextActivity::class.java))
    }
    fun showBVerifyEditText(v: View) {
        startActivity(Intent(this,BVerifyEditTextActivity::class.java))
    }
}
