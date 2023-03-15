package com.example.bakalarka.objects.menu

import android.content.Context
import android.graphics.Bitmap
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.bakalarka.R
import com.example.vahy.objects.ScreenObject

class DownIcon(context: Context)
    : Icon(context) {

    init {
        image = ContextCompat.getDrawable(context, R.drawable.down_black)!!.toBitmap()
        width = 150
        height = 150
        image = Bitmap.createScaledBitmap(image!!, width, height, true)
    }

    override fun reloadImage(w : Int, h : Int){
        image = ContextCompat.getDrawable(context, R.drawable.down_black)!!.toBitmap()
        image = Bitmap.createScaledBitmap(image!!, w, h, true)
    }

    override fun sizeChanged(w: Int, h: Int, xStart: Int, yStart: Int) {
        if (image == null)
            return
        x = xStart
        y = yStart
        height = h
        width = h
        if (image!!.width < width || image!!.height < height){
            reloadImage(width, height)
            return
        }
        image = Bitmap.createScaledBitmap(image!!, width, height, true)
    }
}