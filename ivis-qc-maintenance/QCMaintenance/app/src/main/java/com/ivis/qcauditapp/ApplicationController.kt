package com.qcauditapp

import android.annotation.SuppressLint
import android.app.Application
import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import com.qcauditapp.di.ApplicationComponent
import com.qcauditapp.di.DaggerApplicationComponent

class ApplicationController : Application() {

    lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()

         applicationComponent = DaggerApplicationComponent.factory().create(this)
    }
    @SuppressLint("ResourceType")
    fun showLoadingDialog(context: Context, strLoadingMsg: String): Dialog {
        val dialog = Dialog(context, R.style.CustomDialog)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.loading_layout)

        val v: View = dialog.window!!.decorView
        v.setBackgroundColor(android.R.color.transparent)

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog.window!!.attributes = lp
        dialog.setCancelable(false)

        val imageView = dialog.findViewById<ImageView>(R.id.img_loading)
        val txtLoadingMsg = dialog.findViewById<TextView>(R.id.txt_loading_msg)
        txtLoadingMsg.text = strLoadingMsg

        val rotationClock: Animation = AnimationUtils.loadAnimation(context, R.anim.rotator)
        rotationClock.repeatCount = Animation.INFINITE
        imageView.startAnimation(rotationClock)

        return dialog
    }
}