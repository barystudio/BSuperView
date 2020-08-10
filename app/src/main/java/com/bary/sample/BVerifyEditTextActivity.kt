package com.bary.sample

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_bverifyedittext.*

class BVerifyEditTextActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bverifyedittext)
        AppUtils.setStatusBarColor(
            window,
            ContextCompat.getColor(this, R.color.colorPrimary),
            false
        )
        initView();
    }

    fun initView() {
        back.setColorFilter(ContextCompat.getColor(this, R.color.white))

    }

    override fun onClick(v: View?) {
        val i = v!!.id

    }

    fun backup(v: View) {
        finish()
    }


}
