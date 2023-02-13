package com.example.bakalarka.objects

import android.content.Context
import android.graphics.Bitmap
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.bakalarka.R
import com.example.vahy.objects.ScreenObject

class BaseOfScale(context : Context) :
    ScreenObject(false){

    init {
        image = ContextCompat.getDrawable(context, R.drawable.base_of_scale1)!!.toBitmap()
        z = 2
        width = 1000
        height = 607
        image = Bitmap.createScaledBitmap(image, width, height, true)
    }
}