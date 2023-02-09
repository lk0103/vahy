package com.example.bakalarka.objects

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.bakalarka.R
import com.example.vahy.objects.ScreenObject

class ArmOfScale(context : Context) :
    ScreenObject(false){

    init {
        image = ContextCompat.getDrawable(context, R.drawable.arm_of_scale1)!!.toBitmap()
        z = 2
        width = 1000
        height = 607
        image = Bitmap.createScaledBitmap(image, width, height, true);
    }

    fun rotate(angle : Int){

    }
}