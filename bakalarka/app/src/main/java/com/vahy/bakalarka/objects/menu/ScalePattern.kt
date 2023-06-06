package com.vahy.bakalarka.objects.menu

import android.content.Context
import android.graphics.Bitmap
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.vahy.bakalarka.R

class ScalePattern(context: Context) : Icon(context) {

    init {
        imageType = R.drawable.blue_pattern
        image = ContextCompat.getDrawable(context, imageType)!!.toBitmap()
        width = 300
        height = 182
        image = Bitmap.createScaledBitmap(image!!, width, height, true)
    }

    override fun sizeChanged(w : Int, h : Int, xStart : Int, yStart : Int){
        if (image == null)
            return
        x = xStart
        y = yStart
        height = w * height / width
        width = w
        while (height >= h){
            width -= 5
            height -= 5
        }
        image = Bitmap.createScaledBitmap(image!!, width, height, true)
    }

    fun changePattern(pattern : Int){
        image = ContextCompat.getDrawable(context, pattern)!!.toBitmap()
        image = Bitmap.createScaledBitmap(image!!, width, height, true)
    }
}