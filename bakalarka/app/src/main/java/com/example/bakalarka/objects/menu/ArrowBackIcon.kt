package com.example.bakalarka.objects.menu

import android.content.Context
import android.graphics.Bitmap
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.bakalarka.R
import com.example.vahy.objects.ScreenObject

class ArrowBackIcon(private val context: Context)
    : ScreenObject(false, false) {

    init {
        image = ContextCompat.getDrawable(context, R.drawable.arrow_back)!!.toBitmap()
        width = 300
        height = 300
        image = Bitmap.createScaledBitmap(image!!, width, height, true)
    }

    override fun sizeChanged(w: Int, h: Int, xStart: Int, yStart: Int) {
        if (image == null)
            return
        x = xStart
        y = yStart
        height = w
        width = w
        if (image!!.width < width || image!!.height < height){
            reloadImage(width, height)
            return
        }
        image = Bitmap.createScaledBitmap(image!!, width, height, true)
    }
}